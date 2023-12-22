package com.example.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}
