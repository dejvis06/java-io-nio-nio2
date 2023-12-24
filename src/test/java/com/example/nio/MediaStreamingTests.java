package com.example.nio;

import com.example.nio.media.ClientAudio;
import com.example.nio.media.ServerAudio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Base64;

@SpringBootTest
public class MediaStreamingTests {

    @Value("classpath:sample.mp3")
    Resource resource;

    @Test
    void test() throws IOException, InterruptedException {
        startServerAsync(resource.getFile().getPath());

        Thread.sleep(3000);
        startClientAsync();
    }

    private static void startServerAsync(String path) {
        // Start the server asynchronously
        new Thread(() -> {
            try {
                ServerAudio serverAudio = new ServerAudio(path);
                serverAudio.process();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void startClientAsync() {
        // Start the client asynchronously
        new Thread(() -> {
            try {
                ClientAudio clientAudio = new ClientAudio();
                clientAudio.process(MediaStreamingTests::processByteBufferData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void processByteBufferData(ByteBuffer buffer) {
        System.err.println(new String(Base64.getEncoder().encode(buffer.array())));
    }
}
