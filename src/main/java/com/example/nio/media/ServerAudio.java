package com.example.nio.media;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ServerAudio {

    private static final String FILE_PATH = "audio.mp3";
    private static final int PORT = 12345;
    private final Selector selector;
    private final String path;

    public ServerAudio(String path) throws IOException {
        // Create a selector for handling multiple channels
        this.selector = Selector.open();
        this.path = path;
    }

    public void process() {
        /**
         * - Create a ServerSocketChannel instance to handle server-side socket operations.
         *   ServerSocketChannel is a selectable channel for stream-oriented listening sockets.
         * - Create a RandomAccessFile instance to read the audio file on the server.
         *   RandomAccessFile allows reading from a file at a specified position.
         *   The "r" mode opens the file for reading only.
         */
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
             RandomAccessFile randomAccessFile = new RandomAccessFile(path, "r")) {

            /**
             * Bind the ServerSocketChannel to a specific port to listen for incoming client connections.
             * The InetSocketAddress represents a socket address (IP address and port number).
             * The `bind` operation associates the server socket channel with the given network address.
             */
            serverSocketChannel.bind(new InetSocketAddress(PORT));

            /**
             * Configure the ServerSocketChannel as non-blocking.
             * In non-blocking mode, the channel can be used with a Selector for multiplexed I/O operations,
             * allowing the server to handle multiple channels concurrently without blocking on each operation.
             * This enables more efficient use of system resources and responsiveness.
             */
            serverSocketChannel.configureBlocking(false);

            System.out.println("Server is waiting for client connections...");

            /**
             * registers the ServerSocketChannel with the Selector for accepting incoming connections.
             * The OP_ACCEPT flag indicates that the channel is interested in accepting new connections
             */
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (true) {
                /**
                 * When a client attempts to establish a connection, the select() method will return, and readyChannels will be greater than 0
                 */
                int readyChannels = selector.select();

                if (readyChannels > 0) {
                    Set<SelectionKey> selectedKeys = selector.selectedKeys();
                    Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();

                        /**
                         * checks if ServerSocketChannel is ready to accept connections (if not might be network, resources issues or others)
                         */
                        if (key.isAcceptable()) {
                            // Accept a new connection
                            SocketChannel clientChannel = serverSocketChannel.accept();
                            clientChannel.configureBlocking(false);
                            clientChannel.register(selector, SelectionKey.OP_READ);

                            System.out.println("Client connected: " + clientChannel.getRemoteAddress());

                        } else if (key.isReadable()) {
                            // Read from the client
                            SocketChannel clientChannel = (SocketChannel) key.channel();
                            int bytesRead = clientChannel.read(buffer);

                            if (bytesRead == -1) {
                                // Client disconnected
                                key.cancel();
                                clientChannel.close();
                                System.out.println("Client disconnected");
                            } else {
                                buffer.flip(); // Prepare Buffer for reading from it
                                processClientRequest(buffer, randomAccessFile, clientChannel);
                                buffer.clear(); // Clear buffer for next iteration
                            }
                        }

                        keyIterator.remove();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processClientRequest(ByteBuffer buffer, RandomAccessFile randomAccessFile,
                                      SocketChannel clientChannel) throws IOException {
        // Process the client request based on the content of the ByteBuffer
        // This is where commands such as "PLAY," "STOP," "SEEK," etc are handled.

        byte[] data = new byte[buffer.remaining()];

        // store the buffer data into the 'data' array
        buffer.get(data);

        String command = new String(data).trim(); // Trim to remove leading/trailing whitespaces

        System.out.println("Received from client: " + command);

        switch (command) {
            case "PLAY":
                playAudio(randomAccessFile, clientChannel);
                break;
            case "STOP":
                // Implement STOP behavior (optional)
                stopPlayback();
                break;
            case "SEEK":
                // Read the next line, which should contain the position to seek
                long position = Long.parseLong(readLine(buffer));
                randomAccessFile.seek(position);
                playAudio(randomAccessFile, clientChannel);
                break;
            case "CLOSE":
                // Close the server (optional: close resources or take appropriate action)
                closeServer();
                break;
            default:
                System.out.println("Unknown command: " + command);
        }
    }

    private void playAudio(RandomAccessFile randomAccessFile, SocketChannel clientChannel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bytesRead;

        while ((bytesRead = randomAccessFile.getChannel().read(buffer)) != -1) {
            buffer.flip();
            clientChannel.write(buffer);
            buffer.clear();
        }
    }

    private void stopPlayback() {
        //TODO Implement STOP behavior
        System.out.println("Playback stopped");
    }

    private static String readLine(ByteBuffer buffer) {
        StringBuilder line = new StringBuilder();
        char currentChar;
        while (buffer.hasRemaining() && (currentChar = (char) buffer.get()) != '\n') {
            line.append(currentChar);
        }
        return line.toString();
    }

    private void closeServer() throws IOException {
        // Close the server (optional: close resources or take appropriate action)
        System.out.println("Server is closing");
        selector.close();
    }
}

