package edu.bu.ec504.group9.Chunking;

import edu.bu.ec504.group9.Chunk;
import edu.bu.ec504.group9.ChunkDB;
import edu.bu.ec504.group9.FileInfo;

import java.io.File;
import java.io.FileInputStream;

public abstract class Chunking {
    /** Default Constructor */
    Chunking() {

    }

    /** return the next chunk of this file */
    public abstract void getChunk(File file, ChunkDB db, FileInfo metadata) throws Exception;
}
