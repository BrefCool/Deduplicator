package edu.bu.ec504.group9;

import edu.bu.ec504.group9.Chunking.Chunking;
import edu.bu.ec504.group9.Chunking.FixedSizeChunking;


import java.io.*;
import java.util.*;

import static edu.bu.ec504.group9.FileIO.getChunk;


public class Locker {
    /** all the files' information stored in this locker
     * key: filename
     * value: FileInfo object*/
    private HashMap<String, FileInfo> files;

    /** the locker's name */
    private String lockerName;

    /** chunkDB instance */
    private ChunkDB db;

    /** chunking module */
    private Chunking chunking;

    /** default constructor */
    Locker() {
        lockerName = "default";
        db = ChunkDB.getInstance();
        /** use FixedSizeChunking as default */
        chunking = new FixedSizeChunking();
        /** restore all the files' information from disk */
        files = retrieveFileInfo();
    }

    /** construct the Locker instance with locker's name */
    Locker(String name, Chunking chunkingModule) {
        lockerName = name;
        db = ChunkDB.getInstance();
        chunking = chunkingModule;
        files = retrieveFileInfo();
    }

    /** restore all the files' information of this locker from disk *
     *  each locker has its own files info stored in /lockers directory
     *  locker's name is unique
     */
    public HashMap<String, FileInfo> retrieveFileInfo() {
        if (FileIO.existsFilesInfo(lockerName)) {
            return FileIO.extractFilesInfo(lockerName);
        } else {
            HashMap<String, FileInfo> map = new HashMap<>();
            FileIO.saveFilesInfo(map, lockerName);
            return map;
        }
    }

    /** update all files information, save them to disk*/
    public void updateFileInfo() {
        FileIO.saveFilesInfo(files, lockerName);
    }

    /** check the file info */
    public boolean containsFile(String filename) {
        return files.containsKey(filename);
    }

    /** add new file to this locker */
    public void addFile(String filename) {

        /** check file exists */
        if (!new File(filename).exists()) {
            System.out.println("file does not exists");
            return;
        }

        /** set file metadata */
        if (containsFile(filename)) {
            System.out.println("filename already exists");
            return;
        }

        /** initialize the file metadata */
        FileInfo metadata = new FileInfo();

        /** contruct FileInputStream */
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        /** get every Chunk
         *  each Chunk is decided by Chunking Module
         *  (Chunking Module should decide chunk's hash, data and size)
         *  store each chunk to chunkDB
         *  add metadata information*/
        Chunk chunk;
        try {
            while (fis.available() != 0) {
                chunk = chunking.getNextChunk(fis);
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

        /** save metedata */
        files.put(filename, metadata);

        /** update chunkDB to disk */
        db.updateChunkInfo();

        /** update metadata to disk */
        updateFileInfo();
    }

    /** retrieve file from locker */
    public void retrieveFile(String filename, String outputPath) {
        FileInfo info = files.get(filename);
        if (info == null){
          System.out.println("This file doesn't exist!");
          return;
        }
        try {
            File writename = new File(outputPath + "/" + filename);
            writename.createNewFile(); // Build a new file
            FileOutputStream stream = new FileOutputStream(writename, true);
            Queue<String> hashes = info.hashes;
            for (String hash : hashes) {
                Chunk chunk = getChunk(hash);
                stream.write(chunk.getData());
            }
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
