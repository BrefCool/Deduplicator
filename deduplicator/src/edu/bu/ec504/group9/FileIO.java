package edu.bu.ec504.group9;

import java.nio.file.*;

import java.io.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
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

    /** define where the export locker's data are stored */
    private static String exportsDir = "exports";

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
        String exports = new StringJoiner(File.separator).add(storageDir).add(exportsDir).toString();
        File storesDirectory = new File(storageDir);
        File chunksDirectory = new File(chunks);
        File lockerDirectory = new File(lockers);
        File exportDirectory = new File(exports);

        if (!storesDirectory.exists()) {
            storesDirectory.mkdir();
        }

        if (!chunksDirectory.exists()) {
            chunksDirectory.mkdir();
        }

        if (!lockerDirectory.exists()) {
            lockerDirectory.mkdir();
        }

        if (!exportDirectory.exists()) {
            exportDirectory.mkdir();
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

    public static LockerMeta extractFilesInfo(String lockerName) {
        StringJoiner joiner = new StringJoiner(File.separator);
        joiner.add(storageDir).add(lockersDir).add(lockerName);
        String fileInfoPath = joiner.toString();
        LockerMeta result = null;

        if (!existsFilesInfo(lockerName)) {
            return result;
        }

        try {
            FileInputStream fis = new FileInputStream(fileInfoPath);
            ObjectInputStream in = new ObjectInputStream(fis);
            result = (LockerMeta) in.readObject();
            in.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void saveFilesInfo(LockerMeta files, String lockerName) {
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

    public static void saveFilesInfoTo(LockerMeta files, String lockerName, String toDir) {
        String fileInfoPath = new StringJoiner(File.separator).add(toDir).add(lockerName).toString();
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

    public static void saveChunkTo(Chunk chunk, String toDir) {
        String chunkPath = new StringJoiner(File.separator).add(toDir).add(chunk.chunkHash).toString();
        try {
            FileOutputStream fos = new FileOutputStream(chunkPath);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(chunk);
            out.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public static Chunk getChunkFrom(String hash, String path) {
        StringJoiner joiner = new StringJoiner(File.separator);
        joiner.add(path).add(hash);
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

    private static void delDirRecursive(String dirName) {
        Path targetDir = Paths.get(dirName);
        try {
            Files.walkFileTree(targetDir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportLocker(String lockerName, String outputPath) {
        /** output loker's metadata and chunks' data into /exports directory */
        //String exports = new StringJoiner(File.separator).add(storageDir).add(exportsDir).toString();
        String lockerExport = new StringJoiner(File.separator).add(outputPath).add(lockerName).toString();
        String chunks = new StringJoiner(File.separator).add(lockerExport).add("chunks").toString();
        File lockerExpDir = new File(lockerExport);
        File chunkExpDir = new File(chunks);
        if (lockerExpDir.exists()) {
            delDirRecursive(lockerExport);
        } else {
            lockerExpDir.mkdir();
        }
        chunkExpDir.mkdir();

        LockerMeta meta;
        if (FileIO.existsFilesInfo(lockerName)) {
            meta = FileIO.extractFilesInfo(lockerName);
        } else {
            meta = new LockerMeta();
        }

        for (Map.Entry<String, FileInfo> entry : meta.files.entrySet()) {
            FileInfo info = entry.getValue();
            for (String h : info.hashes) {
                Chunk chunk = getChunk(h);
                saveChunkTo(chunk, chunks);
            }
        }

        saveFilesInfoTo(meta, lockerName, lockerExport);
    }

    public static void importLocker(String lockerPath) {
        File lockerFile = new File(lockerPath);
        String lockerName = lockerFile.getName();
        String originalPath = new StringJoiner(File.separator).add(lockerPath).add(lockerName).toString();
        File originalFile = new File(originalPath);
        String lockersDirectory = new StringJoiner(File.separator).add(storageDir).add(lockersDir).toString();
        File lockers = new File(lockersDirectory);
        File[] fs = lockers.listFiles();

        if (!lockerFile.exists()) {
            System.out.println("locker's file doesn't exists");
            return;
        }

        if (fs != null) {
            for (File f : fs) {
                String fname = f.getName();
                if (fname.equals(lockerName)) {
                    System.out.println("lockers already exists");
                    return;
                }
            }
        }

        File copiedLocker = new File(new StringJoiner(File.separator).add(lockersDirectory).add(lockerName).toString());
        try (
                InputStream in = new BufferedInputStream(
                        new FileInputStream(originalFile));
                OutputStream out = new BufferedOutputStream(
                        new FileOutputStream(copiedLocker))) {

            byte[] buffer = new byte[1024];
            int lengthRead;
            while ((lengthRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, lengthRead);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Locker myLocker = LockerFactory.getLocker(lockerName, LockerFactory.CHUNKING.ROLLING);
        String importChunks = new StringJoiner(File.separator).add(lockerPath).add("chunks").toString();
        LockerMeta meta = myLocker.getMetaData();
        ChunkDB db = ChunkDB.getInstance();
        for (String key : meta.files.keySet()) {
            FileInfo fileinfo = meta.files.get(key);
            for (String hash : fileinfo.hashes) {
                Chunk chunk = getChunkFrom(hash, importChunks);
                db.addChunk(chunk);
            }
        }

        db.updateChunkInfo();
    }

//    public static void importLocker()
}
