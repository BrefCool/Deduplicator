package edu.bu.ec504.group9.Chunking;

/**
 * This algorithm based on rolling hash and Rabin Karp Fingerprint,
 * it will create variable-size chunks which have average size of 8kB.
 * We refer to some materials found in Internet like:
 * http://blog.teamleadnet.com/2012/10/rabin-karp-rolling-hash-dynamic-sized.html
 */

import edu.bu.ec504.group9.Chunk;
import edu.bu.ec504.group9.ChunkDB;
import edu.bu.ec504.group9.FileInfo;
import java.io.*;

public class RollingChunking extends Chunking {
    /**
     * init the function
     */
    public void init() {
        coef = 1;
        buffptr = 0;
        segment = 0;
    }

    /**
     *
     * @param file: target file
     * @param db: database to store the chunk and relative information
     * @param metadata: store the matadate of chunks
     * @throws Exception
     */
    public void getChunk(File file, ChunkDB db, FileInfo metadata) throws Exception {

        init();
        this.db = db;
        this.metadata = metadata;
        int mask = 1 << 13; // set the average chunk size into 8kB
        mask--;

        FileInputStream fs = new FileInputStream(file);
        FileInputStream fsChunk = new FileInputStream(file);
        this.is = new BufferedInputStream(fs);

        long length = is.available();
        long curr = length;

        int hash = 0; // Calculate the hash value of the initial window
        buffer = new int[window];

        for (int i = 0; i < window; i++) {
            int c = is.read();
            if (c == -1)
                break;

            buffer[buffptr] = c;
            buffptr++;
            buffptr = buffptr % buffer.length;

            hash *= prime;
            hash += c;

            if (i > 0)
                coef *= prime;
        }

        curr -= is.available();

        byte[] chunk;
        boolean firstChunk = true;
        while (curr < length) {
            if ((hash & mask) == 0) {

                if (firstChunk == true) {
                    chunk = new byte[(int) curr];
                    firstChunk = false;
                } else {
                    chunk = new byte[segment];
                    createChunk(fsChunk,chunk);
                }
                segment = 0;
            }
            hash = nextHash(hash);
            curr++;
            segment++;
        }
        // The rest of file will also be added as a chunk
        segment = fsChunk.available();
        chunk = new byte[segment];
        createChunk(fsChunk,chunk);

        is.close();
        fs.close();

    }

    /**
     * Funtion to create and store the chunk
     * @param fsChunk
     * @param chunk
     * @throws Exception
     */
    public void createChunk(FileInputStream fsChunk, byte[] chunk) throws Exception{
        if (fsChunk.read(chunk) != -1) {
            String s = new String(chunk, "ISO-8859-1");
            Chunk newChunk = new Chunk(Integer.toString(s.hashCode()), chunk);
            db.addChunk(newChunk);
            metadata.addNextChunk(newChunk.chunkHash, newChunk.chunkSize);
        }
    }

    /**
     * calculate the next hash value by rolling hash
     * @param prevHash
     * @return
     * @throws IOException
     */
    public int nextHash(int prevHash) throws IOException {

        int c = is.read();
        prevHash -= coef * buffer[buffptr];
        prevHash *= prime;
        prevHash += c;
        buffer[buffptr] = c;
        buffptr++;
        buffptr = buffptr % buffer.length;

        return prevHash;
    }

    public static final int prime = 69069;
    public int coef = 1;
    int[] buffer;
    int buffptr = 0;
    int segment = 0;
    InputStream is;
    int window = 64; // set the sliding window's size into 64 bytes
    ChunkDB db;
    FileInfo metadata;

}
