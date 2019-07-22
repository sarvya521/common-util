package com.sp.common.util;

import java.text.MessageFormat;

import org.owasp.encoder.Encode;

/**
 * Utility methods for constructing messages for application logs.
 * This must be used whenever you are logging request.
 *
 * @author sarvesh
 */
public final class LoggerUtilities {

    /**
     * Added to hide the default public constructor.
     */
    private LoggerUtilities() {
        throw new AssertionError(); // just being extra defensive. Nothing's wrong in it, right!!!
    }

    /**
     * Empty string replacement.
     */
    private static final String EMPTY = "";

    /**
     * Returns a formatted message string based on the supplied format and
     * parameters. The format and the parameters will be cleansed by replacing the
     * CR and LF characters with the Java strings "\\r" and "\\n". The parameters
     * are merged within the messages format through the MessageFormat class.
     *
     * @see MessageFormat
     * @param messageFormat the message pattern with place holders; both indexed and
     *                      non-indexed place holders are accepted. Examples:
     *                      "Message first text. Message second text {0} {1}"
     *                      "Message first text. Message second text {} {}"
     * @param formatParams  the formatting arguments. If the message is fixed text
     *                      and doesn't have place holders, formatParams shall be
     *                      null or empty array.
     * @return formatted message string
     */
    public static String getMessage(final String messageFormat, final Object... formatParams) {
        if (messageFormat != null && !messageFormat.isEmpty()) {
            final Object[] params = sanitiseParams(false, formatParams);
            String cleanMsg = Encode.forJava(MessageFormat.format(getFormat(true, messageFormat), params));
            if (cleanMsg.contains("\"")) {
                // preserve the double quotes
                cleanMsg = cleanMsg.replace("\\\"", "\"");
            }
            return cleanMsg;
        } else {
            return LoggerUtilities.EMPTY;
        }
    }

    /**
     * Prepares the parameters for inserting in the message template. Optionally
     * could apply OWASP Encoder for Java to replace the CR and the LF characters by
     * the Java strings "\\r" and "\\n".
     *
     * @param cleanCRLF    - if true will call Encoder. Encoder shall not be called
     *                     if the entire message is being cleansed.
     * @param formatParams params to sanitise
     * @return cleansed versions of params.
     */
    private static Object[] sanitiseParams(boolean cleanCRLF, final Object... formatParams) {
        final int numParams = formatParams == null ? 0 : formatParams.length;
        final String[] sanitisedParams = new String[numParams];

        for (int i = 0; i < numParams; i++) {
            sanitisedParams[i] = (formatParams[i] == null ? EMPTY : formatParams[i].toString());
            if (cleanCRLF) {
                sanitisedParams[i] = Encode.forJava(sanitisedParams[i]);
            }
        }

        return sanitisedParams;
    }

    /**
     * Replaces all occurrences of "{}" with "{0}", "{1}", etc. so that the text can
     * be message pattern compliant to MessageFormat.format. The method doesn't
     * apply CRLF cleaning. DO NOT use the method outside of the class, its
     * package-visible only for the sake of the unit testing.
     *
     * @see MessageFormat
     * @param addIndexes if place holders shall be indexed or not;
     * @param format     string to update
     * @return updated string
     */
    private static String getFormat(boolean addIndexes, final String format) {
        String result = format;
        if (!addIndexes) {
            return result;
        }

        int i = 0;
        while (result.contains("{}")) {
            String replacement = "{" + i++ + "}";
            if (!result.contains(replacement)) {
                result = result.replaceFirst("\\{\\}", replacement);
            }
        }
        return result;
    }
}
