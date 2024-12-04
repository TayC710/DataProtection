package util;

import java.io.*;

import org.apache.commons.io.IOUtils;

public class FileUtils {
    public static void write(byte[] data, String filePath) throws IOException {
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath))) {
            IOUtils.write(data, outputStream);
        }
    }

    public static byte[] read(String filePath) throws IOException {
        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(filePath))) {
            return IOUtils.toByteArray(inputStream);
        }
    }
}
