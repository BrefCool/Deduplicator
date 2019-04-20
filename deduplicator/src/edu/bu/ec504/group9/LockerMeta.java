package edu.bu.ec504.group9;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class LockerMeta implements Serializable {
    /** Store Locker's file info */
    public HashMap<String, FileInfo> files;

    /** Store locker's dir info */
    public  HashMap<String, ArrayList<String>> directorys;

    LockerMeta() {
        files = new HashMap<>();
        directorys = new HashMap<>();
        directorys.put("/", new ArrayList<>());
    }
}
