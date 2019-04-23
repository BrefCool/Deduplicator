package edu.bu.ec504.group9;
import java.util.Scanner;
import java.io.File;
public class CmdLineArgs {
    public static void main(String[] args) {

        /** have to  initialize FileIO first, this will create lockers and chunks directory */
        FileIO.initialize();

        /** obtain numbers of arguments in command line */
        int count = args.length;
        int x = 0;
        int y = 0;
        int z = 0;
        int w = 0;

        if (count <= 0) {
            System.out.println("no arguments exists");
            return;
        }

        /** search for key words "locker name,addfile name, retrieve file name and output path */
        for (int i = 0; i < count; i++) {
            if (args[i].equals("-locker")) {
                x = i + 1;
                if (x >= count) {
                    System.out.println("lockername is needed after '-locker'");
                    return;
                }
            } else if (args[i].equals("-addFile")) {
                y = i + 1;
                if (y >= count) {
                    System.out.println("filename is needed after '-addFile'");
                    return;
                }
            } else if (args[i].equals("-retrieveFile")) {
                z = i + 1;
                if (z >= count) {
                    System.out.println("filename is needed after '-retrieveFile'");
                    return;
                }
            } else if (args[i].equals("-output")) {
                w = i + 1;
                if (w >= count) {
                    System.out.println("output path is needed after '-output'");
                    return;
                }
            } else if (args[i].equals("-gui")) {
                GUI gui = new GUI();
                return;
            }
        }

        String lockerName = args[x];
        String addFileName = args[y];
        String retrieveFileName = args[z];
        String filepath = args[w];

            /** instantiate locker */
            Locker myLocker = LockerFactory.getLocker(lockerName, LockerFactory.CHUNKING.ROLLING);

            /** do operations */
            if (y != 0) {
                myLocker.addFile(addFileName, "");
            }
            if (z != 0) {
                if (w != 0) {
                    myLocker.retrieveFile(retrieveFileName, filepath);
               } else {

                    /** create empty folder to retrieve when output path is null */
                    File dir = new File("retrieveFile");
                    dir.mkdir();
                    myLocker.retrieveFile(retrieveFileName, "./retrieveFile");
                }
            }

    }
}



