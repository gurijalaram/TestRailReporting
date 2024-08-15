package com.apriori.trr.api.testrail.utils;

import com.apriori.trr.api.testrail.exceptions.ResourceLoadException;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.stream.Stream;

@Slf4j
public class FileResourceUtil {

    private static final int TEMP_DIR_ATTEMPTS = 50;

    /**
     * Get resource file stream from a jar file. {getResource}
     *
     * @param resourceFileName - the file name
     * @return input stream
     */
    public static InputStream getResourceFileStream(String resourceFileName) {
        return ClassLoader.getSystemResourceAsStream(resourceFileName);
    }

    /**
     * Get file from resource folder current module.
     *
     * @param resourceFileName
     * @return file object
     */
    public static File getLocalResourceFile(String resourceFileName) {
        try {
            return new File(
                URLDecoder.decode(
                    ClassLoader.getSystemResource(resourceFileName).getFile(),
                    "UTF-8"
                )
            );
        } catch (UnsupportedEncodingException e) {
            log.error(String.format("Resource file: %s was not found", resourceFileName));
            throw new IllegalArgumentException();
        }
    }


    /**
     * Get file from resource folder
     * <p>
     * Subfolders should be separated by a comma eg. cad-files, files
     *
     * @param resourceFileName - the file name
     * @return file object
     */
    @Deprecated
    public static File getResourceAsFile(String path, String resourceFileName) {
        try {
            InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(path.replace(",", File.separator).trim() + File.separator + resourceFileName);

            return copyIntoTempFile(in, path, resourceFileName);
        } catch (RuntimeException e) {
            throw new ResourceLoadException(String.format("File with name '%s' does not exist: ", resourceFileName, e));

        }
    }

    /**
     * Get file from resource folder
     *
     * @param resourceFileName - the file name
     * @return file object
     */
    public static File getResourceAsFile(String resourceFileName) {
        try {
            InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(resourceFileName);
            return copyIntoTempFile(in, null, resourceFileName);
        } catch (RuntimeException e) {
            throw new ResourceLoadException(String.format("File with name '%s' does not exist: ", resourceFileName, e));
        }
    }

    public static File copyIntoTempFile(final InputStream inputStreamOfOriginalFile, final String additionalPath, final String fileName) {
        if (inputStreamOfOriginalFile == null) {
            return null;
        }

        File tempFile = new File(createTempDir(additionalPath), fileName);
        tempFile.deleteOnExit();

        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            //copy stream
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStreamOfOriginalFile.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            return tempFile;

        } catch (RuntimeException | IOException e) {
            throw new ResourceLoadException(String.format("File with name '%s' cannot be saved: ", fileName, e));
        }

    }

    public static File copyIntoTempFile(final String contentBytes, final String additionalPath, final String fileName) {
        File tempFile = new File(createTempDir(additionalPath), fileName);
        tempFile.deleteOnExit();

        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            byte[] data = Base64.getMimeDecoder().decode(contentBytes.getBytes(StandardCharsets.UTF_8));
            out.write(data);
        } catch (Exception e) {
            throw new ResourceLoadException(String.format("File with name '%s' cannot be saved: ", fileName, e));
        }
        return tempFile;
    }

    public static File createTempDir(String path) {

        File baseDir = new File(System.getProperty("java.io.tmpdir"));
        String baseName = System.currentTimeMillis() + "-";

        String childFilePath = "Automation-" + baseName + "%s";

        if (path != null) {
            childFilePath = childFilePath + File.separator + path.replace(",", File.separator).trim();
        }

        for (int counter = 0; counter < TEMP_DIR_ATTEMPTS; counter++) {
            File tempDir = new File(baseDir, String.format(childFilePath, counter));

            if (tempDir.mkdirs()) {
                return tempDir;
            }
        }
        throw new IllegalStateException(
            "Failed to create directory within "
                + TEMP_DIR_ATTEMPTS
                + " attempts (tried "
                + baseName
                + "0 to "
                + baseName
                + (TEMP_DIR_ATTEMPTS - 1)
                + ')');
    }

    /**
     * Convert JSON file into string
     *
     * @param file
     * @return String
     * @throws Exception
     */
    public static String convertFileIntoString(String file) {
        // declare a variable in which we store the JSON data as a string and return it to the main() method
        String jsonFileContent = null;
        try {
            jsonFileContent = new String(Files.readAllBytes(Paths.get(file)));
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        return jsonFileContent;
    }

    /**
     * Reads a file from an input stream as string
     *
     * @param fileName - the file name
     * @return String
     */
    @SneakyThrows
    public static String readFileToString(String fileName) {
        return FileUtils.readFileToString(FileResourceUtil.getResourceAsFile(fileName), StandardCharsets.UTF_8);
    }

    /**
     * Find the downloaded file with zip extension
     *
     * @param path          path to search for file
     * @param fileExtension extension of file to search
     * @return file with complete path
     */
    public static String findFileWithExtension(Path path, String fileExtension) {
        String fileWithExtension = "";
        try {
            if (!Files.isDirectory(path)) {
                throw new IllegalArgumentException("Path must be a directory!");
            }
        } catch (Exception ioException) {
            log.error("PATH NOT FOUND!!");
        }

        try (Stream<Path> walk = Files.walk(Paths.get(String.valueOf(path)))) {
            fileWithExtension = walk
                .filter(p -> !Files.isDirectory(p))   // not a directory
                .map(p -> p.toString().toLowerCase()) // convert path to string
                .filter(f -> f.endsWith(fileExtension))       // check end with
                .findFirst()
                .get();
        } catch (Exception ioException) {
            log.error("FILE NOT FOUND!!");
        }
        return fileWithExtension;
    }

    public static String encodeImageToBase64(String imagePath) throws IOException {
        File file = new File(imagePath);
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fis.read(buffer)) != -1) {
            baos.write(buffer, 0, bytesRead);
        }

        fis.close();
        baos.close();

        byte[] imageBytes = baos.toByteArray();
        return Base64.getEncoder().encodeToString(imageBytes);
    }
}