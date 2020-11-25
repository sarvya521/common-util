package com.sp.common.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

public final class ContractTestingUtilities {

    private ContractTestingUtilities() {
        throw new AssertionError(); //extra defensive
    }
    
    public static void generateContract(String swaggerJson, Optional<String> swaggerYamlFileName, Optional<String> outputDir) throws IOException {
        final String FILE_NAME = swaggerYamlFileName.orElse("swagger.yaml");
        final String OUTPUT_DIR = outputDir.orElse("./src/test/resources/contracts");
        Files.createDirectories(Paths.get(OUTPUT_DIR));
        String swaggerYaml = JsonUtilities.convertJsonToYaml(swaggerJson);
        if(null != swaggerYaml) {
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(OUTPUT_DIR, FILE_NAME), StandardCharsets.UTF_8)){
                writer.write(swaggerYaml);
            }
        }
    }
}
