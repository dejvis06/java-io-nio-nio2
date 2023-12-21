package com.example;

import com.example.io.ReadFile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.io.IOException;

@SpringBootTest
class JavaIoNioNio2ApplicationTests {

    @Value("classpath:roman_empire_history.txt")
    Resource resource;

    @Test
    void contextLoads() throws IOException {

        new ReadFile(1024, resource.getFile().getPath())
                .perform(bytes -> System.err.println("reading bytes: " + new String(bytes)));
    }

}
