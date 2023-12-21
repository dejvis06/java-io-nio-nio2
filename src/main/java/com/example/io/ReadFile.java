package com.example.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

public class ReadFile {

    private static final Logger logger = LoggerFactory.getLogger(ReadFile.class);

    private byte[] bytes;
    private String path;

    public ReadFile(int length, String path) {
        this.bytes = new byte[length];
        this.path = path;
    }

    /**
     * Reads data from the specified file using a FileInputStream and processes it using a Consumer<byte[]>.
     * <p>
     * The method reads the file in chunks, each represented by a byte array, and passes each chunk to
     * the provided Consumer for further processing. The FileInputStream is automatically closed after
     * the read operation is complete.
     *
     * @param consumer A Consumer<byte[]> that defines the operation to be performed on each chunk of data.
     *                 The actual data read from the file is passed to the consumer.
     * @throws IOException If an I/O error occurs while reading from the file. The exception is logged,
     *                     and the original exception is rethrown to allow for handling at a higher level.
     */
    public void perform(Consumer<byte[]> consumer) throws IOException {
        try (InputStream inputStream = new FileInputStream(path)) {
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

    /**
     * Performs enhanced reading from an input stream using buffering for improved efficiency.
     * <p>
     * This method reads data from the specified file path using a FileInputStream wrapped in a
     * BufferedInputStream. The BufferedInputStream efficiently reads data into a buffer, minimizing
     * the number of direct read operations from the underlying stream (a file or other type). The read data is then processed
     * by the provided Consumer<byte[]> callback.
     *
     * @param consumer The consumer callback to process the read byte data.
     * @throws IOException If an I/O error occurs during the reading process.
     */
    public void performEnhanced(Consumer<byte[]> consumer) throws IOException {
        try (InputStream inputStream = new FileInputStream(path);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {

            int bytesRead;
            while ((bytesRead = bufferedInputStream.read(bytes)) != -1) {

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
