package org.bugbusters.database;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;

public class ImageSave {

    public static void save(String filePath) {

        // Directory where the image will be saved
        String outputDir = "src/main/java/org/bugbusters/database/images/";   // Ex: "C:/images/saved/"
        try {
            Path outputPath = Paths.get(outputDir);
            if (!Files.exists(outputPath)) {
                Files.createDirectories(outputPath);
            }

            int idPhoto = countFilesInDirectory();

            // Output file name with the new extension/format
            String newImageName = idPhoto+1 + ".jpeg";

            // Read the original image
            BufferedImage image = ImageIO.read(new File(filePath));

            // Extract the format from the new file name (the extension after the dot)
            String format = newImageName.substring(newImageName.lastIndexOf('.') + 1);

            // Full path to save the image with the new name and format
            File outputFile = new File(outputDir + newImageName);

            // Save the image in the specified directory with the determined format
            boolean result = ImageIO.write(image, format, outputFile);

            if (result) {
                System.out.println("Image successfully saved at: " + outputFile.getAbsolutePath());
            } else {
                System.out.println("Error: Unsupported image format.");
            }
        } catch (IOException e) {
            System.out.println("Error saving the image: " + e.getMessage());
        }
    }

    private static int countFilesInDirectory() {
        // Directory path
        String directoryPath = "src/main/java/org/bugbusters/database/images/";

        // Create a File object for the directory
        File directory = new File(directoryPath);

        // List all files in the directory (including subdirectories)
        int fileCount = directory.listFiles().length;

        // Return the total number of files
        return fileCount;
    }
}


