# Java IO

## FileReaderInputStream

InputStreams are used for handling byte data.
The `FileReaderInputStream` class provides functionality for reading data from a file using a `FileInputStream`. It is designed to read the file in chunks, represented by byte arrays, and allows processing each chunk of data using a `Consumer<byte[]>` callback. The class also includes an enhanced reading method that utilizes buffering for improved efficiency.

#### Constructor
```java
public FileReaderInputStream(int length, String path)
```
length: The size of the byte array used for reading chunks of data.
path: The path of the file to be read.

#### Methods

##### 1. `perform(Consumer<byte[]> consumer)`
```java
public void perform(Consumer<byte[]> consumer) throws IOException
```
1. Reads data from the specified file using a FileInputStream.
2. Processes each chunk of data using the provided Consumer<byte[]> callback.
3. The FileInputStream is automatically closed after the read operation is complete.

##### 2. `performEnhanced(Consumer<byte[]> consumer)`
```java
public void performEnhanced(Consumer<byte[]> consumer) throws IOException
```
1. Enhanced reading method using buffering for improved efficiency.
2. Reads data from the specified file using a FileInputStream wrapped in a BufferedInputStream.
3. The BufferedInputStream efficiently reads data into a buffer, minimizing the number of direct read operations from the underlying stream.
4. Processes each chunk of read data using the provided Consumer<byte[]> callback.

## FileReaderCharacterStream

Readers are suitable for handling human readable text.

#### Methods

##### 1. `perform(Consumer<char[]> consumer)`
```java
public void perform(Consumer<char[]> consumer) throws IOException
```
1. Reads characters from a file using a character stream and provides them to the consumer.
2. Utilizes try-with-resources to ensure automatic closing of the character stream.
3. Throws IOException if an I/O error occurs during the read operation.

##### 2. `performEnhanced(Consumer<char[]> consumer)`
```java
public void performEnhanced(Consumer<char[]> consumer) throws IOException
```

1. Reads characters from a file using a character stream with buffering and provides them to the consumer.
2. Utilizes try-with-resources to ensure automatic closing of the character stream.
3. The buffering mechanism enhances performance by minimizing direct reads from the underlying stream.
4. Throws IOException if an I/O error occurs during the read operation.

## Serialize Class

The `Serialize` class provides utility methods for object serialization and deserialization. It includes methods to convert objects to byte arrays (serialization) and to reconstruct objects from byte arrays (deserialization).

This class uses Java's built-in serialization mechanism, allowing objects to be converted into a portable byte format, which can be transmitted over networks, stored, or reconstructed at a later time.

##### Usage Example

```java
// Serialization
byte[] serializedData = Serialize.serialize(myObject);

// Deserialization
MyObjectType deserializedObject = Serialize.deserialize(serializedData);
```

## CompositeSequenceInputStream Class

The `CompositeSequenceInputStream` class provides a convenient way to create a composite `SequenceInputStream` by allowing the dynamic addition of individual `InputStreams`. It manages a list of `InputStreams`, and clients can sequentially add `InputStreams` to be concatenated.

This class is useful when there is a need to concatenate multiple `InputStreams` into a single stream. The order of addition determines the order of concatenation.

##### Usage Example

```java
@Test
    void contextLoads() throws IOException {

        FileInputStream inputStream = new FileInputStream(resource.getFile().getPath());
        FileInputStream inputStream1 = new FileInputStream(resource.getFile().getPath());

        SequenceInputStream sequenceInputStream = new CompositeSequenceInputStream()
                .addInputStream(inputStream)
                .addInputStream(inputStream1)
                .getCompositeSequenceInputStream();
        read(System.err::print, sequenceInputStream);
    }

    void read(Consumer<Character> consumer, InputStream inputStream) throws IOException {
        try {
            int bytesRead = 0;
            while ((bytesRead = inputStream.read()) != -1) {
                consumer.accept((char) bytesRead);
            }
        } finally {
            inputStream.close();
        }
    }
```

## Data Streams
The DataInputStream and DataOutputStream are used to work with binary data. 

```java
@Test
    void contextLoads() throws IOException {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream("data.bin"))) {
            // Writing Metadata
            dos.writeUTF("EmployeeID");
            dos.writeUTF("Name");
            dos.writeUTF("Salary");
            
            ...
            
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream("data.bin"))) {
            // Writing Metadata
            dos.writeUTF("EmployeeID");
            dos.writeUTF("Name");
            dos.writeUTF("Salary");

            System.out.println("Metadata: [" + field1 + ", " + field2 + ", " + field3 + "]");

            // Reading Data Records
            int employeeID = dis.readInt();
            String name = dis.readUTF();
            double salary = dis.readDouble();
```

Metadata or other ways are needed to specify the structure of the bin file.