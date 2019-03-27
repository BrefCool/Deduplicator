package edu.bu.ec504.group9.Chunking;

import edu.bu.ec504.group9.Chunk;

import java.io.FileInputStream;

public class RollingChunking extends Chunking {
    @Override
    public Chunk getNextChunk(FileInputStream fis) {
        return null;
    }
}
