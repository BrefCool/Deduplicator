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
	    Locker myLocker = LockerFactory.getLocker("myLocker", LockerFactory.CHUNKING.ROLLING);

//	    for (int i = 0; i <=99 ; i++)
//	    	myLocker.addFile(i + ".png");
//
//		for (int i = 0; i <= 99; i++)
//	    	myLocker.retrieveFile(+ i + ".png", "/Users/wang/Desktop/504project/output");
		myLocker.addFile("/Users/wang/Desktop/504project/testfile", "testfile");
		//myLocker.retrieveFile("TestFile00.txt", "/Users/wang/Desktop/504project/output");
		//myLocker.retrieveFile("TestFile01.txt", "/Users/wang/Desktop/504project/output");
        myLocker.retrieveFile("testfile/newtest", "/Users/wang/Desktop/504project/output");

    }
}
