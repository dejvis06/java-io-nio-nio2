package com.example.nio;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

@SpringBootTest
public class FileSystemExample {

    @Value("classpath:")
    Resource resource;

    @Test
    public void test() throws IOException {
        Path directoryPath = Paths.get(resource.getURI().getPath());
        listAndFilterDirectoryContents(directoryPath);
        traverseDirectoryTree(directoryPath);
        visitDirectoryOrFile(directoryPath);
        watchDirectory(directoryPath);
    }

    // 1. Listing/Filtering directory contents
    private static void listAndFilterDirectoryContents(Path directoryPath) throws IOException {
        System.out.println("Listing and filtering directory contents:");
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directoryPath)) {
            for (Path file : stream) {
                System.out.println(file.getFileName());
            }
        }
        System.out.println();
    }

    // 2. Traversing directory tree
    private static void traverseDirectoryTree(Path directoryPath) throws IOException {
        System.out.println("Traversing directory tree:");
        Files.walkFileTree(directoryPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println("File: " + file.getFileName());
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                System.out.println("Directory: " + dir.getFileName());
                return FileVisitResult.CONTINUE;
            }
        });
        System.out.println();
    }

    // 3. Visiting directory or file
    private static void visitDirectoryOrFile(Path directoryPath) throws IOException {
        System.out.println("Visiting directory or file:");
        Files.walkFileTree(directoryPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println("File: " + file.getFileName());
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                System.out.println("Directory: " + dir.getFileName());
                return FileVisitResult.CONTINUE;
            }
        });
        System.out.println();
    }

    // 4. Watching directory events
    private static void watchDirectory(Path directoryPath) throws IOException {
        System.out.println("Watching directory events:");
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            directoryPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);

            while (true) {
                WatchKey key;
                try {
                    key = watchService.take();
                } catch (InterruptedException e) {
                    System.err.println("Error while waiting for directory events: " + e.getMessage());
                    return;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    Path context = (Path) event.context();
                    System.out.println(kind + ": " + context);
                }

                if (!key.reset()) {
                    break; // Break if the key is no longer valid
                }
            }
        }
    }
}

