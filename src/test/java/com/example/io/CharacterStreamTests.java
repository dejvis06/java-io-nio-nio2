package com.example.io;

import com.example.io.FileReaderCharacterStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.io.IOException;

@SpringBootTest
class CharacterStreamTests {

    @Value("classpath:roman_empire_history.txt")
    Resource inputResource;

    @Test
    void test() throws IOException {
        FileReaderCharacterStream characterStreamReader
                = new FileReaderCharacterStream(10, inputResource.getFile().getPath());

        characterStreamReader.perform(System.err::println);
        characterStreamReader.performEnhanced(System.err::println);
    }
}