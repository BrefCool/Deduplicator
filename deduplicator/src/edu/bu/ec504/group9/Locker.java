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

    public  HashMap<String,ArrayList<String>> directorys;

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
        directorys = new HashMap<>();
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
    public void addFile(String filename, String path) {
        /** para to check the filename is file or directory */
        File checkFileORDir = new File(filename);

        // if is file
        if (checkFileORDir.isFile()) {

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
            try {
                chunking.getChunk(new File(filename), db, metadata);
            } catch (Exception e) {
                e.printStackTrace();
            }

            /** save metedata */
            //files.put(filename, metadata);
            files.put(path, metadata);

            /** update chunkDB to disk */
            db.updateChunkInfo();


            /** update metadata to disk */
            updateFileInfo();
        }
        // if is directory
        if (checkFileORDir.isDirectory()) {
            //System.out.println("it is directory");
            addDirectory(filename, path);
        }

    }

    /** retrieve file from locker */
    public void retrieveFile(String filename, String outputPath) {
        /**
         * if the filename is not a directory
         */

        if (!directorys.containsKey(filename)) {
            //String absFileName = "";
            //String[] list = filename.split("\\/");
//            for (String key : files.keySet()) {
//                String[] key_list = key.split("\\/");
//                if (key_list[key_list.length-1].equals(filename)) {
//                    absFileName = key;
//                }
//            }


            //FileInfo info = files.get(absFileName);
            FileInfo info = files.get(filename);
            if (info == null) {
                System.out.println("This file doesn't exist!");
                return;
            }
            try {
                String[] list = filename.split("\\/");
                File writename = new File(outputPath + "/" + list[list.length-1]);
                System.out.println(writename.toString());
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
        }else {
            /** if it's a directory */
            ArrayList<String> list = directorys.get(filename);
            String[] strs = filename.split("\\/");
            outputPath = outputPath +"/" + strs[strs.length-1];


            File dir = new File(outputPath);
            dir.mkdir();
            for (String str : list) {
                retrieveFile(str, outputPath);
            }

        }
    }

    /**
     * funtion to add files in the whole directory
     * @param dirPath
     */
    public void addDirectory(String dirPath, String relPath) {

        //String[] strs = dirPath.split("\\/");
        //String dir = strs[strs.length-1];
        //System.out.println(dir);
        if (directorys.containsKey(relPath)) {
            System.out.println("directory has existed, please change a name");
            return;
        }

        directorys.put(relPath,new ArrayList<>());
        File path = new File(dirPath);
        File[] files_list = path.listFiles();
        for (File file : files_list) {
            String absPath = file.getAbsolutePath();
            String [] s = absPath.split("\\/");
            directorys.get(relPath).add(relPath+"/"+s[s.length-1]);
            addFile(absPath,relPath+"/"+s[s.length-1]);
        }
    }

  public void deleteFile(String fileName){
        FileInfo info = files.get(fileName);
        if (info == null){
            System.out.println("This file doesn't exist!");
            return;
        }
        Queue<String>hashes = info.hashes;
        for (String hash : hashes) {
            deleteChunk(hash);}
        files.remove(fileName);
    }
}
