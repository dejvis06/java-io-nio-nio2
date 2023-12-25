package com.example.io;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;

@SpringBootTest
public class DataStreams {

    /**
     * This method demonstrates the usage of DataOutputStream and DataInputStream for writing
     * and reading binary data, respectively. The example focuses on creating a binary file named
     * "data.bin" to store information related to employees.
     *
     * Purpose:
     * - Utilizes DataOutputStream for writing metadata and data records to the binary file.
     * - Utilizes DataInputStream for reading metadata and data records from the binary file.
     */
    @Test
    void test() throws IOException {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream("data.bin"))) {
            // Writing Metadata
            dos.writeUTF("EmployeeID");
            dos.writeUTF("Name");
            dos.writeUTF("Salary");

            // Writing Data Records
            dos.writeInt(1);
            dos.writeUTF("John Doe");
            dos.writeDouble(50000.0);

            dos.writeInt(2);
            dos.writeUTF("Jane Smith");
            dos.writeDouble(60000.0);
        } catch (IOException e) {
            throw e;
        }
        try (DataInputStream dis = new DataInputStream(new FileInputStream("data.bin"))) {
            // Reading Metadata
            String field1 = dis.readUTF();
            String field2 = dis.readUTF();
            String field3 = dis.readUTF();

            System.out.println("Metadata: [" + field1 + ", " + field2 + ", " + field3 + "]");

            // Reading Data Records
            int employeeID = dis.readInt();
            String name = dis.readUTF();
            double salary = dis.readDouble();

            System.out.println("Employee ID: " + employeeID);
            System.out.println("Name: " + name);
            System.out.println("Salary: " + salary);
        } catch (IOException e) {
            throw e;
        }
    }
}
