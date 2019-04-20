package edu.bu.ec504.group9;

import java.io.File;
import java.util.HashMap;

public class ChunkDB {
    /** static variable instance of type ChunkDB */
    private static ChunkDB instance = null;

    /** chunk's reference count
     * key: chunk's hash
     * value: how many files in all lockers are using this chunk*/
    private HashMap<String, Integer> chunkRef;

    /** make constructor private
     * you should use getInstance() instead to get a instance of ChunkDB
     */
    private ChunkDB() {
        chunkRef = retrieveChunkInfo();
    }

    /** retrieve all chunks information from disk */
    private HashMap<String, Integer> retrieveChunkInfo() {
        if (FileIO.existsChunksInfo()) {
            return FileIO.extractChunksInfo();
        } else {
            HashMap<String, Integer> map =  new HashMap<>();
            FileIO.saveChunksInfo(map);
            return map;
        }
    }

    /** update all chunks information to disk*/
    public void updateChunkInfo() {
        FileIO.saveChunksInfo(chunkRef);
    }

    /** static method to create single instance of ChunkDB */
    public static ChunkDB getInstance() {
        if (instance == null)
            instance = new ChunkDB();

        return instance;
    }

    /** check chunk by hash */
    public boolean containsChunk(String hash) {
        return chunkRef.containsKey(hash);
    }

    /** add new chunk */
    public void addChunk(Chunk chunk) {
        if (!containsChunk(chunk.chunkHash)) {
            chunkRef.put(chunk.chunkHash, 1);
            /** save the chunk's data to disk */
            FileIO.saveChunk(chunk);
        } else {
            Integer ref = chunkRef.get(chunk.chunkHash);
            chunkRef.put(chunk.chunkHash, ref + 1);
        }
    }

    /** get chunk */
    public Chunk getChunk(String hash) {
        if (containsChunk(hash))
            return FileIO.getChunk(hash);
        else
            return null;
    }

    /** chunk's referCount -= 1 */
    public void deleteChunk(String hash) {
        if (containsChunk(hash)) {
            Integer ref = chunkRef.get(hash);
            if (ref - 1 <= 0) {
                chunkRef.remove(hash);
                FileIO.deleteChunk(hash);
            } else {
                chunkRef.put(hash, ref - 1);
            }
        }
    }

    /** add reference count */
    public void increChunk(String hash) {
        if (containsChunk(hash)) {
            Integer ref = chunkRef.get(hash);
            chunkRef.put(hash, ref + 1);
        }
    }
}
