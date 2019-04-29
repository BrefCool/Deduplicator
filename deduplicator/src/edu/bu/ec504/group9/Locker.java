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

    /** Locker's metadata */
    private LockerMeta metaData;

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
        metaData = retrieveFileInfo();
    }

    /** construct the Locker instance with locker's name */
    Locker(String name, Chunking chunkingModule) {
        lockerName = name;
        db = ChunkDB.getInstance();
        chunking = chunkingModule;
        metaData = retrieveFileInfo();
    }

    public void printFiles() {
        for (String key : metaData.files.keySet()) {
            System.out.println(key);
        }
    }

    public void printDirs() {
        for (String key : metaData.directorys.keySet()) {
            System.out.println(key + ":");
            for (String ele : metaData.directorys.get(key)) {
                System.out.println(ele);
            }
        }
    }

    public LockerMeta getMetaData() {
        return metaData;
    }

    /** restore all the files' information of this locker from disk *
     *  each locker has its own files info stored in /lockers directory
     *  locker's name is unique
     */
    public LockerMeta retrieveFileInfo() {
        if (FileIO.existsFilesInfo(lockerName)) {
            return FileIO.extractFilesInfo(lockerName);
        } else {
            LockerMeta data = new LockerMeta();
            FileIO.saveFilesInfo(data, lockerName);
            return data;
        }
    }

    /** update all files information, save them to disk*/
    public void updateFileInfo() {
        FileIO.saveFilesInfo(metaData, lockerName);
    }

    /** check the file info */
    public boolean containsFile(String filename) {
        return metaData.files.containsKey(filename);
    }

    /** add new file to this locker */
    public String addFile(String filePath, String rootPath) {
        /** para to check the filename is file or directory */
        File checkFileORDir = new File(filePath);

        /** check file exists */
        if (!checkFileORDir.exists()) {
            System.out.println("file doesn't exists");
            return "file doesn't exists";
        }

        /** extract filename */
        String filename = checkFileORDir.getName();
        filename = new StringJoiner(File.separator).add(rootPath).add(filename).toString();
        /** record first level file or dir */
        if (rootPath.equals("")) {
            metaData.directorys.get("/").add(filename);
        }

        // if is file
        if (checkFileORDir.isFile()) {

            /** set file metadata */
            if (containsFile(filename)) {
                System.out.println("filename already exists");
                return "filename already exists";
            }

            /** initialize the file metadata */
            FileInfo metadata = new FileInfo();

            /** get every Chunk
             *  each Chunk is decided by Chunking Module
             *  (Chunking Module should decide chunk's hash, data and size)
             *  store each chunk to chunkDB
             *  add metadata information*/
            try {
                chunking.getChunk(new File(filePath), db, metadata);
            } catch (Exception e) {
                e.printStackTrace();
            }

            /** save metedata */
            metaData.files.put(filename, metadata);

        }
        else if (checkFileORDir.isDirectory()) {
            String re =new String("");
            re = addDirectory(filePath, filename);
            return re;
        }

        /** update chunkDB to disk */
        db.updateChunkInfo();

        /** update metadata to disk */
        updateFileInfo();
        return "Add file successfully";

    }

    /** retrieve file from locker */
    public void retrieveFile(String filename, String outputPath) {
        if (filename.length() != 0 && filename.charAt(0) != '/') {
            filename = '/' + filename;
        }
        /**
         * if the filename is not a directory
         */

        if (!metaData.directorys.containsKey(filename)) {

            FileInfo info = metaData.files.get(filename);
            if (info == null) {
                System.out.println("This file doesn't exist!");
                return;
            }
            try {
                String[] list = filename.split("\\/");
                File writename = new File(outputPath + "/" + list[list.length-1]);
                //System.out.println(writename.toString());
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
            ArrayList<String> list = metaData.directorys.get(filename);
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
    public String addDirectory(String dirPath, String relPath) {

        if (metaData.directorys.containsKey(relPath)) {
            System.out.println("directory has existed, please change a name");
            return "directory has existed, please change a name";
        }

        metaData.directorys.put(relPath,new ArrayList<>());
        File path = new File(dirPath);
        File[] files_list = path.listFiles();
        for (File file : files_list) {
            String absPath = file.getAbsolutePath();
            String [] s = absPath.split("\\/");
            metaData.directorys.get(relPath).add(relPath+"/"+s[s.length-1]);
            addFile(absPath,relPath);
        }
        return "Add file successfully";
    }

  public void deleteFile(String filename){
      if (filename.length() != 0 && filename.charAt(0) != '/') {
          filename = '/' + filename;
      }

      final String target = filename;

      /**
       * if the filename is not a directory
       */
      if (!metaData.directorys.containsKey(filename)) {

          /** remove target file */
          FileInfo info = metaData.files.get(filename);
          if (info == null) {
              System.out.println("file doesn't exists");
              return;
          }

          for(String hash : info.hashes) {
              db.deleteChunk(hash);
          }
          metaData.files.remove(filename);

      } else {
          /** if it's a directory */
          ArrayList<String> list = metaData.directorys.get(filename);
          ArrayList<String> listCopy = new ArrayList<>(list);

          for (String subFile : listCopy) {
              deleteFile(subFile);
          }

          metaData.directorys.remove(filename);
      }

      /** if this file belongs to one directory, remove this file from the directory */
      String rootPath = filename.substring(0, filename.lastIndexOf('/'));
      if (rootPath.equals("")) {
          rootPath = "/";
      }
      if (metaData.directorys.containsKey(rootPath)) {
          ArrayList<String> list = metaData.directorys.get(rootPath);
          list.removeIf(s -> (s.equals(target)));
      }

      /** update chunkDB to disk */
      db.updateChunkInfo();

      /** update metadata to disk */
      updateFileInfo();

    }
    //SubString Search function
  public List<String> SSS(String txt2search){
        List<String> filelist = new ArrayList<>();
//        String FileName;
        String chunktxt; // text file
        KMP kmp = new KMP();  //KMP function
        for (String key : metaData.files.keySet()){
            StringBuilder sb = new StringBuilder();
            FileInfo info = metaData.files.get(key);
            if (info == null)
                continue;
            Queue<String> hashes = info.hashes;
            for (String hash : hashes) {
                Chunk chunk = getChunk(hash); // get chunk
                chunktxt = new String(chunk.getData());  //chunk -> string
                sb.append(chunktxt);  // full file
            }

//            System.out.println(key + ":");
//            System.out.println(filetxt);

            if (kmp.kmp(sb.toString(),txt2search))
                filelist.add(key);
        }

        return filelist;
  }
}

