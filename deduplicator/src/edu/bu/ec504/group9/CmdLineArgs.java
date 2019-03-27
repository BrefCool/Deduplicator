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

        /** search for key words "locker name,addfile name, retrieve file name and output path */
        for (int i = 0; i < count; i++) {
            if (args[i].contains("-locker")) {
                x = i + 1;
            } else if (args[i].contains("-addFile")) {
                y = i + 1;
            } else if (args[i].contains("-retrieveFile")) {
                z = i + 1;
            } else if (args[i].contains("-output")) {
                w = i + 1;
            }
        }
            String lockerName = args[x];
            String addFileName = args[y];
            String retrieveFileName = args[z];
            String filepath = args[w];

            /** instantiate locker */
            Locker myLocker = LockerFactory.getLocker(lockerName, LockerFactory.CHUNKING.FIXEDSIZE);

            /** do operations */
            if (y != 0) {
                myLocker.addFile(addFileName);
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



