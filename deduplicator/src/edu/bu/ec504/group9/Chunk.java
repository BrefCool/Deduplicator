package edu.bu.ec504.group9;

import java.io.Serializable;

public class Chunk implements Serializable {
    /** record chunk's hash */
    public String chunkHash;

    /** the size of this chunk */
    public long chunkSize;

    /** chunk's data */
    private byte[] chunkData;

    public Chunk() {

    }

    public Chunk(String hash, byte[] data) {
        chunkHash = hash;
        chunkData = data;
        chunkSize = chunkData.length;
    }

    public byte[] getData() {
        return chunkData;
    }

}
