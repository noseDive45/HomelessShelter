package com.example.jackson.homelessshelter.Model;

/**
 * This interface is implemented by all fragments shown in the activity with the drawer
 */
public interface DrawerLocker {

    /**
     * This method decides whether or not the drawer should be locked
     * @param shouldLock boolean stating whether or not the drawer should be locked
     */

    void unlocked(boolean shouldLock);
}
