package com.example.io;

import com.example.io.Serialize;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.Serializable;

@SpringBootTest
public class SerializationTests {

    @Test
    void test() throws IOException, ClassNotFoundException {
        MyClass myClass = new MyClass("test", 1);
        byte[] bytes = Serialize.serialize(myClass);
        System.err.println(new String(bytes));

        myClass = Serialize.deserialize(bytes);
        System.err.println(myClass.toString());
    }

    private static class MyClass implements Serializable {
        private static final long serialVersionUID = 1L;

        private final String name;
        private final int value;

        public MyClass(String name, int value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public String toString() {
            return "MyClass{" +
                    "name='" + name + '\'' +
                    ", value=" + value +
                    '}';
        }
    }
}
