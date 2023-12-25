package com.example.io;

import com.example.io.FileReaderInputStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.io.IOException;

@SpringBootTest
class ReadFileTests {

    @Value("classpath:roman_empire_history.txt")
    Resource resource;

    @Test
    void test() throws IOException {
        FileReaderInputStream readFile = new FileReaderInputStream(1024, resource.getFile().getPath());

        readFile.perform(bytes -> System.err.println("reading bytes (not enhanced): " + new String(bytes)));
        readFile.performEnhanced(bytes -> System.err.println("reading bytes (enhanced): " + new String(bytes)));
    }
}
