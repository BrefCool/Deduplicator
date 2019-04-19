package edu.bu.ec504.group9.Chunking;

import edu.bu.ec504.group9.Chunk;
import edu.bu.ec504.group9.ChunkDB;
import edu.bu.ec504.group9.FileIO;
import edu.bu.ec504.group9.FileInfo;

import java.io.*;
import java.util.Arrays;

public class FixedSizeChunking extends Chunking {
    /** different types of Chunking module should implement this function.
     * Chunking module should decide next chunk of this file.
     *  input: the file to be saved in locker. type: FileInputStream
     *  output: Chunk object,*/

    public void getChunk(File file, ChunkDB db, FileInfo metadata) throws Exception {

        Chunk chunk;
        try {
            FileInputStream fis = new FileInputStream(file);
            while (fis.available() != 0) {
                chunk = helper(fis);
                db.addChunk(chunk);
                metadata.addNextChunk(chunk.chunkHash, chunk.chunkSize);
            }
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
            /** clear the file metedata & chunks created so far */
            for (int i = 0; i < metadata.hashes.size(); i++) {
                FileIO.deleteChunk(metadata.hashes.get(i));
            }
            return;
        }

    }

    public Chunk helper(FileInputStream fis) {
        byte[] data = new byte[8192]; // Set chunk size into 8kB;
        int len = 0;
        int hashcode;
        try {
            len = fis.read(data);
            if (len != 8192) // In the case when we reach the end of file
                data = Arrays.copyOfRange(data, 0, len);
            String s = new String(data, "ISO-8859-1"); // transfer the byte array to string
            hashcode = s.hashCode(); // get hashcode
            //System.out.println(len);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return new Chunk(Integer.toString(hashcode), data);
    }
}
