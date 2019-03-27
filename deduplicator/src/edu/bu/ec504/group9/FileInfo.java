package edu.bu.ec504.group9;

import java.io.Serializable;
import java.util.LinkedList;

public class FileInfo implements Serializable {
    /** a linkedlist contains all the chunks' hash */
    public LinkedList<String> hashes;

    /** record file's type and format */
    public String format;

    /** record the file size */
    public long fileSize;

    public FileInfo() {
        format = "undefined";
        hashes = new LinkedList<>();
        fileSize = 0;
    }

    /** add the hash of next chunk to the tail of linked list */
    public void addNextChunk(String hash, long ChunkSize) {
        hashes.add(hash);
        fileSize += ChunkSize;
    }

    /** set file format */
    public void setFormat(String f) {
        format = f;
    }
}
