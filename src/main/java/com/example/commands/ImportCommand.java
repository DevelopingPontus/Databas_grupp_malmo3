package com.example.commands;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.services.JsonService;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Component
@Command(name = "import", mixinStandardHelpOptions = true, description = "Import data from JSON files")
public class ImportCommand implements Runnable {
    @Parameters(index = "0", description = "Path to the JSON file to import")
    private String filePath;

    private final JsonService jsonService;

    public ImportCommand(JsonService jsonService) {
        this.jsonService = jsonService;
    }

    private boolean validatePath(String path) {
        if (filePath == null || filePath.isEmpty()) {
            System.out.println("Please provide a valid file path.");
            return false;
        }
        if (!filePath.endsWith(".json")) {
            System.out.println("Unsupported file format. Please provide a JSON file.");
            return false;
        }
        if (!Files.exists(Paths.get(filePath))) {
            System.out.println("File not found: " + filePath);
            return false;
        }
        return true;
    }

    private Optional<String> readJsonFile(String path) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(path)));
            return Optional.of(content);
        } catch (Exception e) {
            System.out.println("Error reading JSON file: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public void run() {
        if (!validatePath(filePath)) {
            return;
        }

        System.out.printf("Importing data from JSON file %s...\n", filePath);
        readJsonFile(filePath).ifPresentOrElse((jsonData) -> {
            try {
                jsonService.importData(jsonData);
                System.out.println("Data import completed successfully.");
            } catch (Exception e) {
                System.out.println("Error importing data: " + e.getMessage());
                return;
            }
        }, () -> {
            System.out.println("Failed to read JSON file.");
        });
    }
}
