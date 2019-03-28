package edu.bu.ec504.group9;

import java.io.*;
import java.util.HashMap;
import java.util.StringJoiner;

public class FileIO {
    /** get home directory */
    private static String homeDir = System.getProperty("user.home");

    /** record the storage directory */
    private static String storageDir = new StringJoiner(File.separator).add(homeDir).add(".dedupStore").toString();

    /** record where all the chunks are saved */
    private static String chunksDir = "chunks";

    /** define where all the locker objects are stored */
    private static String lockersDir = "lockers";

    /** define where to save chunk info */
    private static String chunksInfo = "chunksInfo";

    public static boolean existsChunksInfo() {
        StringJoiner joiner = new StringJoiner(File.separator);
        joiner.add(storageDir).add(chunksDir).add(chunksInfo);
        String chunkInfoPath = joiner.toString();
        return new File(chunkInfoPath).exists();
    }

    public static boolean existsFilesInfo(String lockerName) {
        StringJoiner joiner = new StringJoiner(File.separator);
        joiner.add(storageDir).add(lockersDir).add(lockerName);
        String fileInfoPath = joiner.toString();
        return new File(fileInfoPath).exists();
    }

    /** create directory needed for storage */
    public static void initialize() {
        String chunks = new StringJoiner(File.separator).add(storageDir).add(chunksDir).toString();
        String lockers = new StringJoiner(File.separator).add(storageDir).add(lockersDir).toString();
        File storesDirectory = new File(storageDir);
        File chunksDirectory = new File(chunks);
        File lockerDirectory = new File(lockers);

        if (!storesDirectory.exists()) {
            storesDirectory.mkdir();
        }

        if (!chunksDirectory.exists()) {
            chunksDirectory.mkdir();
        }

        if (!lockerDirectory.exists()) {
            lockerDirectory.mkdir();
        }
    }

    /** restore Chunks' information from disk */
    public static HashMap<String, Integer> extractChunksInfo() {
        StringJoiner joiner = new StringJoiner(File.separator);
        joiner.add(storageDir).add(chunksDir).add(chunksInfo);
        String chunkInfoPath = joiner.toString();
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
        StringJoiner joiner = new StringJoiner(File.separator);
        joiner.add(storageDir).add(chunksDir).add(chunksInfo);
        String chunkInfoPath = joiner.toString();
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
        StringJoiner joiner = new StringJoiner(File.separator);
        joiner.add(storageDir).add(lockersDir).add(lockerName);
        String fileInfoPath = joiner.toString();
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
        StringJoiner joiner = new StringJoiner(File.separator);
        joiner.add(storageDir).add(lockersDir).add(lockerName);
        String fileInfoPath = joiner.toString();
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
        StringJoiner joiner = new StringJoiner(File.separator);
        joiner.add(storageDir).add(chunksDir).add(chunk.chunkHash);
        String chunkPath = joiner.toString();
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
        StringJoiner joiner = new StringJoiner(File.separator);
        joiner.add(storageDir).add(chunksDir).add(hash);
        String chunkPath = joiner.toString();
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
        StringJoiner joiner = new StringJoiner(File.separator);
        joiner.add(storageDir).add(chunksDir).add(hash);
        String chunkPath = joiner.toString();
        File file = new File(chunkPath);
        if (file.exists()) {
            file.delete();
        }
    }
}
