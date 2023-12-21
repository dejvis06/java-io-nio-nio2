package com.example.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.function.Consumer;

public class ReadFile {

    Logger logger = LoggerFactory.getLogger(ReadFile.class);

    private byte[] bytes;
    private String path;

    public ReadFile(int length, String path) {
        this.bytes = new byte[length];
        this.path = path;
    }

    /**
     * Reads data from the specified file using a FileInputStream and processes it using a Consumer<byte[]>.
     *
     * The method reads the file in chunks, each represented by a byte array, and passes each chunk to
     * the provided Consumer for further processing. The FileInputStream is automatically closed after
     * the read operation is complete.
     *
     * @param consumer A Consumer<byte[]> that defines the operation to be performed on each chunk of data.
     *                 The actual data read from the file is passed to the consumer.
     *
     * @throws IOException If an I/O error occurs while reading from the file. The exception is logged,
     *                     and the original exception is rethrown to allow for handling at a higher level.
     *
     */
    public void perform(Consumer<byte[]> consumer) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(path)) {
            int bytesRead;
            while ((bytesRead = inputStream.read(bytes)) != -1) {

                byte[] bytesReadData = new byte[bytesRead];
                System.arraycopy(bytes, 0, bytesReadData, 0, bytesRead);

                consumer.accept(bytesReadData);
            }
        } catch (IOException e) {
            logger.info("Exception during read: {} on input data: {}", e.getMessage(), this);
            throw e;
        }
    }
}
