package edu.bu.ec504.group9;

import edu.bu.ec504.group9.Chunking.FixedSizeChunking;
import edu.bu.ec504.group9.Chunking.RollingChunking;

public class LockerFactory {
    public static enum CHUNKING {
        ROLLING, FIXEDSIZE;
    }

    /** get default locker */
    public static Locker getLocker() {
        return new Locker();
    }

    /** get locker */
    public static Locker getLocker(String lockerName, CHUNKING chunking) {
        switch(chunking) {
            case ROLLING: return new Locker(lockerName, new RollingChunking());
            case FIXEDSIZE: return new Locker(lockerName, new FixedSizeChunking());
            default:
                System.out.println("invalid chunking");
                return null;
        }
    }
}
