package com.example.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class Serialize {

    private static final Logger logger = LoggerFactory.getLogger(Serialize.class);

    public static <T> byte[] serialize(T object) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {

            objectOutputStream.writeObject(object);
            return byteArrayOutputStream.toByteArray();

        } catch (IOException e) {
            logger.info("Exception during serialization for object: {}", object);
            throw e;
        }
    }

    public static <T> T deserialize(byte[] byteArray) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {

            return (T) objectInputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            logger.info("Exception during deserialization: {}", e.getMessage());
            throw e;
        }
    }
}
