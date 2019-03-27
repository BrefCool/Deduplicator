package edu.bu.ec504.group9.Chunking;

import edu.bu.ec504.group9.Chunk;

import java.io.FileInputStream;

public abstract class Chunking {
    /** Default Constructor */
    Chunking() {

    }

    /** return the next chunk of this file */
    public abstract Chunk getNextChunk(FileInputStream fis);
}
