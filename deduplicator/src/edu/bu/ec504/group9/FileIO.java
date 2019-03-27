package edu.bu.ec504.group9;

import java.io.*;
import java.util.HashMap;

public class FileIO {
    /** record the storage directory */
    private static String storageDir = "~/.dedupStore";

    /** record where all the chunks are saved */
    private static String chunksDir = "./chunks";

    /** define where all the locker objects are stored */
    private static String lockersDir = "./lockers";

    /** define where to save chunk info */
    private static String chunksInfo = "chunksInfo";

    public static boolean existsChunksInfo() {
        String chunkInfoPath = chunksDir + "/" + chunksInfo;
        return new File(chunkInfoPath).exists();
    }

    public static boolean existsFilesInfo(String lockerName) {
        String fileInfoPath = lockersDir + "/" + lockerName;
        return new File(fileInfoPath).exists();
    }

    /** create directory needed for storage */
    public static void initialize() {
        File chunksDirectory = new File(chunksDir);
        File lockerDirectory = new File(lockersDir);

        if (!chunksDirectory.exists()) {
            chunksDirectory.mkdir();
        }

        if (!lockerDirectory.exists()) {
            lockerDirectory.mkdir();
        }
    }

    /** restore Chunks' information from disk */
    public static HashMap<String, Integer> extractChunksInfo() {
        String chunkInfoPath = chunksDir + "/" + chunksInfo;
        HashMap<String, Integer> result = null;

        if (!existsChunksInfo()) {
            return result;
        }

        try {
            FileInputStream fis = new FileInputStream(chunkInfoPath);
            ObjectInputStream in = new ObjectInputStream(fis);
            result = (HashMap<String, Integer>)in.readObject();
            in.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    /** save chunks' information to disk */
    public static void saveChunksInfo(HashMap<String, Integer> chunks) {
        String chunkInfoPath = chunksDir + "/" + chunksInfo;
        try {
            FileOutputStream fos = new FileOutputStream(chunkInfoPath);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(chunks);
            out.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, FileInfo> extractFilesInfo(String lockerName) {
        String fileInfoPath = lockersDir + "/" + lockerName;
        HashMap<String, FileInfo> result = null;

        if (!existsFilesInfo(lockerName)) {
            return result;
        }

        try {
            FileInputStream fis = new FileInputStream(fileInfoPath);
            ObjectInputStream in = new ObjectInputStream(fis);
            result = (HashMap<String, FileInfo>)in.readObject();
            in.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void saveFilesInfo(HashMap<String, FileInfo> files, String lockerName) {
        String fileInfoPath = lockersDir + "/" + lockerName;
        try {
            FileOutputStream fos = new FileOutputStream(fileInfoPath);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(files);
            out.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** save chunk's data to disk, each chunk is stored in a file named by its hash */
    public static long saveChunk(Chunk chunk) {
        String chunkPath = chunksDir + "/" + chunk.chunkHash;
        try {
            FileOutputStream fos = new FileOutputStream(chunkPath);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(chunk);
            out.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File chunkFile = new File(chunkPath);

        return chunkFile.length();
    }

    /** get the chunk's data */
    public static Chunk getChunk(String hash) {
        String chunkPath = chunksDir + "/" + hash;
        Chunk result = null;

        if (!new File(chunkPath).exists()) {
            return result;
        }

        try {
            FileInputStream fis = new FileInputStream(chunkPath);
            ObjectInputStream in = new ObjectInputStream(fis);
            result = (Chunk)in.readObject();
            in.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    /** delete the chunk file */
    public static void deleteChunk(String hash) {
        String chunkPath = chunksDir + "/" + hash;
        File file = new File(chunkPath);
        if (file.exists()) {
            file.delete();
        }
    }
}
