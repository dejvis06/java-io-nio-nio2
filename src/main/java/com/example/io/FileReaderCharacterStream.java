package com.example.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Objects;
import java.util.function.Consumer;

public class FileReaderCharacterStream {

    private static final Logger logger = LoggerFactory.getLogger(FileReaderCharacterStream.class);

    private final char[] chars;
    private final String path;

    public FileReaderCharacterStream(int length, String path) {
        if (length <= 0)
            throw new IllegalArgumentException("Length must be a positive value");
        this.chars = new char[length];
        this.path = Objects.requireNonNull(path, "Path cannot be null");
    }

    /**
     * Reads characters from a file using a character stream and provides them to the consumer.
     * This method utilizes try-with-resources to ensure the automatic closing of the character stream.
     *
     * @param consumer A consumer function that processes the read character data.
     * @throws IOException If an I/O error occurs during the read operation.
     */
    public void perform(Consumer<char[]> consumer) throws IOException {
        try (Reader reader = new FileReader(path)) {

            int charsRead = 0;
            while ((charsRead = reader.read(chars)) != -1) {

                char[] charsReadData = new char[charsRead];
                System.arraycopy(chars, 0, charsReadData, 0, charsRead);

                consumer.accept(charsReadData);
            }
        } catch (IOException e) {
            logger.info("Exception during read: {} on input data: {}", e.getMessage(), this);
            throw e;
        }
    }

    /**
     * Reads characters from a file using a character stream with buffering and provides them to the consumer.
     * This method utilizes try-with-resources to ensure the automatic closing of the character stream.
     * The buffering mechanism enhances performance by minimizing direct reads from the underlying stream.
     *
     * @param consumer A consumer function that processes the read character data.
     * @throws IOException If an I/O error occurs during the read operation.
     */
    public void performEnhanced(Consumer<char[]> consumer) throws IOException {
        try (Reader reader = new FileReader(path);
             BufferedReader bufferedReader = new BufferedReader(reader)) {

            int charsRead;
            while ((charsRead = bufferedReader.read(chars)) != -1) {

                // Create a new char array to store the exact number of characters read
                char[] charsReadData = new char[charsRead];

                // Copy the characters read from the buffer to the new char array
                System.arraycopy(chars, 0, charsReadData, 0, charsRead);

                // Provide the char array to the consumer for further processing
                consumer.accept(charsReadData);
            }
        } catch (IOException e) {
            // Log the exception and rethrow it
            logger.info("Exception during read: {} on input data: {}", e.getMessage(), this);
            throw e;
        }
    }
}
