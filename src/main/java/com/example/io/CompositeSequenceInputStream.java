package com.example.io;

import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * The CompositeSequenceInputStream class provides a convenient way to create a composite
 * SequenceInputStream by allowing dynamic addition of individual InputStreams. It manages
 * a list of InputStreams, and clients can sequentially add InputStreams to be concatenated.
 * <p>
 * This class is useful when there is a need to concatenate multiple InputStreams
 * into a single stream. The order of addition determines the order of concatenation.
 * <p>
 * Usage Example:
 * ```java
 * CompositeSequenceInputStream compositeStream = new CompositeSequenceInputStream();
 * compositeStream.addInputStream(inputStream1)
 * .addInputStream(inputStream2)
 * .addInputStream(inputStream3);
 * <p>
 * SequenceInputStream resultStream = compositeStream.getCompositeSequenceInputStream();
 * // Use resultStream as needed...
 * ```
 */
public class CompositeSequenceInputStream {

    private List<InputStream> inputStreams;

    public CompositeSequenceInputStream() {
        this.inputStreams = new ArrayList<>();
    }

    public CompositeSequenceInputStream addInputStream(InputStream inputStream) {
        inputStreams.add(inputStream);
        return this;
    }

    public SequenceInputStream getCompositeSequenceInputStream() {
        Enumeration<InputStream> enumeration = Collections.enumeration(inputStreams);
        return new SequenceInputStream(enumeration);
    }
}
