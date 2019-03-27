package edu.bu.ec504.group9;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        /** have to  initialize FileIO first, this will create lockers and chunks directory */
	    FileIO.initialize();

	    /** get locker, each locker is a independent storage system */
	    Locker myLocker = LockerFactory.getLocker("myLocker", LockerFactory.CHUNKING.FIXEDSIZE);

	    for (int i = 0; i < 10; i++) {
            myLocker.addFile("TestFile0"+i+".txt");
        }
        for (int i = 10; i < 50; i++) {
            myLocker.addFile("TestFile"+i+".txt");
        }
        for (int i = 0; i < 10; i++) {
            myLocker.retrieveFile("TestFile0"+i+".txt","/Users/wang/Desktop/testfile");
        }
        for (int i = 10; i < 50; i++) {
            myLocker.retrieveFile("TestFile"+i+".txt", "/Users/wang/Desktop/testfile");
        }

    }
}
