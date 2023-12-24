package com.example.nio.media;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class ClientAudio {

    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int PORT = 12345;

    public void process(MediaPlayer mediaPlayer) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()) {

            // Send commands to the server
            sendCommand(outputStream, "PLAY");
            /*sendCommand(outputStream, "STOP");
            sendCommand(outputStream, "SEEK");
            sendCommand(outputStream, "CLOSE");*/

            // Optionally, you can read and process audio data from the server
            processAudio(inputStream, mediaPlayer);

        } catch (IOException e) {
            throw e;
        }
    }

    private static void sendCommand(OutputStream outputStream, String command) throws IOException {
        // Send a command to the server
        outputStream.write((command + "\n").getBytes());
    }

    private static void processAudio(InputStream inputStream, MediaPlayer mediaPlayer) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        byte[] readData = new byte[1024];
        int bytesRead;

        // Read and process audio data from the server
        while ((bytesRead = inputStream.read(readData)) != -1) {
            buffer.put(readData, 0, bytesRead);
            mediaPlayer.render(buffer);
            buffer.clear();
        }
    }
}
