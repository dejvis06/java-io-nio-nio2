package com.example.nio.media;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface MediaPlayer {
    void render(ByteBuffer bytes);
}
