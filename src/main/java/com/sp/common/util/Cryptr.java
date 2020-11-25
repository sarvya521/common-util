package com.sp.common.util;
/*
 *               Cryptr
 *
 * Cryptr is a java encryption toolset
 * that can be used to encrypt/decrypt files
 * and keys locally, allowing for files to be
 * shared securely over the world wide web
 *
 * Cryptr provides the following functions:
 *	 1. Generating a secret key
 *   2. Encrypting a file with a secret key
 *   3. Decrypting a file with a secret key
 *   4. Encrypting a secret key with a public key
 *   5. Decrypting a secret key with a private key
 *
 *  To Generate Public and Private key:
 *  openssl genrsa -out keypair.pem 2048
 *  openssl rsa -in keypair.pem -outform DER -pubout -out public.der
 *  openssl pkcs8 -topk8 -nocrypt -in keypair.pem -outform DER -out private.der
 */

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

public class Cryptr {
    private static final String SECRET_KEY_ALGORITHM = "AES";
    private static final int SECRET_KEY_ALGORITHM_BIT = 128;
    private static final String PUBLIC_PRIVATE_KEY_ALGORITHM = "RSA";
    private static final String SECRET_KEY_ENCRYPT_DECRYPT_CIPHER = "RSA/ECB/OAEPWithSHA1AndMGF1Padding";

    /**
     * Generates an 128-bit AES secret key and writes it to a file
     *
     * @param secKeyFile name of file to store secret key
     */
    static void generateKey(String secKeyFile) throws Exception {
        //Java normally doesn't support 256-bit key sizes without an extra installation so stick with a 128-bit key
        KeyGenerator keygen = KeyGenerator.getInstance(SECRET_KEY_ALGORITHM);
        keygen.init(SECRET_KEY_ALGORITHM_BIT);
        SecretKey secretKey = keygen.generateKey();

        //Saving secretKey in a file
        FileOutputStream file = new FileOutputStream(secKeyFile);
        file.write(secretKey.getEncoded());
        file.close();
    }

    /**
     * Extracts secret key from a file, generates an
     * initialization vector, uses them to encrypt the original
     * file, and writes an encrypted file containing the initialization
     * vector followed by the encrypted file data
     *
     * @param originalFile  name of file to encrypt
     * @param secKeyFile    name of file storing secret key
     * @param encryptedFile name of file to write iv and encrypted file data
     */
    static void encryptFile(String originalFile, String secKeyFile, String encryptedFile) throws Exception {
        byte[] fileContent = readFileBytes(originalFile);

        byte[] encodedSecretKey = readFileBytes(secKeyFile);
        SecretKey secretKey = new SecretKeySpec(encodedSecretKey, SECRET_KEY_ALGORITHM);

        byte[] iv = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);
        IvParameterSpec ivspec = new IvParameterSpec(iv);

        Cipher ci = Cipher.getInstance("AES/CBC/PKCS5Padding");
        ci.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
        byte[] encyptedFileContent = ci.doFinal(fileContent);

        //Saving encyptedFileContent in a file
        FileOutputStream file = new FileOutputStream(encryptedFile);
        file.write(ivspec.getIV());
        file.write(encyptedFileContent);
        file.close();
    }


    /**
     * Extracts the secret key from a file, extracts the initialization vector
     * from the beginning of the encrypted file, uses both secret key and
     * initialization vector to decrypt the encrypted file data, and writes it to
     * an output file
     *
     * @param encryptedFile name of file storing iv and encrypted data
     * @param secKeyFile    name of file storing secret key
     * @param outputFile    name of file to write decrypted data to
     */
    static void decryptFile(String encryptedFile, String secKeyFile, String outputFile) throws Exception {
        byte[] fileContent = readFileBytes(encryptedFile);

        byte[] encodedSecretKey = readFileBytes(secKeyFile);
        SecretKey secretKey = new SecretKeySpec(encodedSecretKey, SECRET_KEY_ALGORITHM);

        byte[] iv = Arrays.copyOfRange(fileContent, 0, 16);
        byte[] encryptedFileContent = Arrays.copyOfRange(fileContent, 16, fileContent.length);
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        Cipher ci = Cipher.getInstance("AES/CBC/PKCS5Padding");
        ci.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
        byte[] decyptedFileContent = ci.doFinal(encryptedFileContent);

        //Saving decyptedFileContent in a file
        FileOutputStream file = new FileOutputStream(outputFile);
        file.write(decyptedFileContent);
        file.close();
    }


    /**
     * Extracts secret key from a file, encrypts a secret key file using
     * a public Key (*.der) and writes the encrypted secret key to a file
     *
     * @param secKeyFile name of file holding secret key
     * @param pubKeyFile name of public key file for encryption
     * @param encKeyFile name of file to write encrypted secret key
     */
    static void encryptKey(String secKeyFile, String pubKeyFile, String encKeyFile) throws Exception {
        byte[] encodedSecretKey = readFileBytes(secKeyFile);
        SecretKey secretKey = new SecretKeySpec(encodedSecretKey, SECRET_KEY_ALGORITHM);

        PublicKey publicKey = readPublicKey(pubKeyFile);
        Cipher cipher = Cipher.getInstance(SECRET_KEY_ENCRYPT_DECRYPT_CIPHER);
        cipher.init(Cipher.WRAP_MODE, publicKey);
        final byte[] encryptedKey = cipher.wrap(secretKey);

        //Saving encrypted secretKey in a file
        FileOutputStream file = new FileOutputStream(encKeyFile);
        file.write(encryptedKey);
        file.close();
    }


    /**
     * Decrypts an encrypted secret key file using a private Key (*.der)
     * and writes the decrypted secret key to a file
     *
     * @param encKeyFile  name of file storing encrypted secret key
     * @param privKeyFile name of private key file for decryption
     * @param secKeyFile  name of file to write decrypted secret key
     */
    static void decryptKey(String encKeyFile, String privKeyFile, String secKeyFile) throws Exception {
        byte[] encodedEncryptedSecretKey = readFileBytes(encKeyFile);
        SecretKey encryptedSecretKey = new SecretKeySpec(encodedEncryptedSecretKey, SECRET_KEY_ALGORITHM);

        PrivateKey privateKey = readPrivateKey(privKeyFile);
        Cipher cipher = Cipher.getInstance(SECRET_KEY_ENCRYPT_DECRYPT_CIPHER);
        cipher.init(Cipher.UNWRAP_MODE, privateKey);
        final Key decryptedKey =
            cipher.unwrap(
                encryptedSecretKey.getEncoded(),
                SECRET_KEY_ALGORITHM,
                Cipher.SECRET_KEY);

        //Saving decrypted secretKey in a file
        FileOutputStream file = new FileOutputStream(secKeyFile);
        file.write(decryptedKey.getEncoded());
        file.close();
    }

    static byte[] readFileBytes(String filename) throws IOException {
        Path path = Paths.get(filename);
        return Files.readAllBytes(path);
    }

    static PublicKey readPublicKey(String filename) throws Exception {
        X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(readFileBytes(filename));
        KeyFactory keyFactory = KeyFactory.getInstance(PUBLIC_PRIVATE_KEY_ALGORITHM);
        return keyFactory.generatePublic(publicSpec);
    }

    static PrivateKey readPrivateKey(String filename) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(readFileBytes(filename));
        KeyFactory keyFactory = KeyFactory.getInstance(PUBLIC_PRIVATE_KEY_ALGORITHM);
        return keyFactory.generatePrivate(keySpec);
    }


    /**
     * Main Program Runner
     */
    public static void main(String[] args) throws Exception {

        String func;

        if (args.length < 1) {
            func = "";
        } else {
            func = args[0];
        }

        switch (func) {
            case "generatekey":
                if (args.length != 2) {
                    System.out.println("Invalid Arguments.");
                    System.out.println("Usage: Cryptr generatekey <key output file>");
                    break;
                }
                System.out.println("Generating secret key and writing it to " + args[1]);
                generateKey(args[1]);
                break;
            case "encryptfile":
                if (args.length != 4) {
                    System.out.println("Invalid Arguments.");
                    System.out.println("Usage: Cryptr encryptfile <file to encrypt> <secret key file> <encrypted " +
                        "output file>");
                    break;
                }
                System.out.println("Encrypting " + args[1] + " with key " + args[2] + " to " + args[3]);
                encryptFile(args[1], args[2], args[3]);
                break;
            case "decryptfile":
                if (args.length != 4) {
                    System.out.println("Invalid Arguments.");
                    System.out.println("Usage: Cryptr decryptfile <file to decrypt> <secret key file> <decrypted " +
                        "output file>");
                    break;
                }
                System.out.println("Decrypting " + args[1] + " with key " + args[2] + " to " + args[3]);
                decryptFile(args[1], args[2], args[3]);
                break;
            case "encryptkey":
                if (args.length != 4) {
                    System.out.println("Invalid Arguments.");
                    System.out.println("Usage: Cryptr encryptkey <key to encrypt> <public key to encrypt with> " +
                        "<encrypted key file>");
                    break;
                }
                System.out.println("Encrypting key file " + args[1] + " with public key file " + args[2] + " to " + args[3]);
                encryptKey(args[1], args[2], args[3]);
                break;
            case "decryptkey":
                if (args.length != 4) {
                    System.out.println("Invalid Arguments.");
                    System.out.println("Usage: Cryptr decryptkey <key to decrypt> <private key to decrypt with> " +
                        "<decrypted key file>");
                    break;
                }
                System.out.println("Decrypting key file " + args[1] + " with private key file " + args[2] + " to " + args[3]);
                decryptKey(args[1], args[2], args[3]);
                break;
            default:
                System.out.println("Invalid Arguments.");
                System.out.println("Usage:");
                System.out.println("  Cryptr generatekey <key output file>");
                System.out.println("  Cryptr encryptfile <file to encrypt> <secret key file> <encrypted output file>");
                System.out.println("  Cryptr decryptfile <file to decrypt> <secret key file> <decrypted output file>");
                System.out.println("  Cryptr encryptkey <key to encrypt> <public key to encrypt with> <encrypted key file> ");
                System.out.println("  Cryptr decryptkey <key to decrypt> <private key to decrypt with> <decrypted key file>");
        }
        System.exit(0);
    }
}
