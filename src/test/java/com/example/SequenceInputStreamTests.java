package com.example;

import com.example.io.CompositeSequenceInputStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.function.Consumer;

@SpringBootTest
public class SequenceInputStreamTests {

    @Value("classpath:roman_empire_history.txt")
    Resource resource;

    @Test
    void contextLoads() throws IOException {

        FileInputStream inputStream = new FileInputStream(resource.getFile().getPath());
        FileInputStream inputStream1 = new FileInputStream(resource.getFile().getPath());

        SequenceInputStream sequenceInputStream = new CompositeSequenceInputStream()
                .addInputStream(inputStream)
                .addInputStream(inputStream1)
                .getCompositeSequenceInputStream();
        read(System.err::print, sequenceInputStream);
    }

    public void read(Consumer<Character> consumer, InputStream inputStream) throws IOException {
        try {
            int bytesRead = 0;
            while ((bytesRead = inputStream.read()) != -1) {
                consumer.accept((char) bytesRead);
            }
        } finally {
            inputStream.close();
        }
    }
}
