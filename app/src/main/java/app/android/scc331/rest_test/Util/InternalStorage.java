package app.android.scc331.rest_test.Util;

/**
 * Credit to + https://github.com/symbyte for code to help with data storage
 * Manages Data Storage on the device.
 */

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public final class InternalStorage{

    private InternalStorage() {}

    /**
     * Checks for a files existance on the device
     * @param context
     * @param fname File name
     * @return boolean depending on files existance
     */
    public static boolean fileExistance(Context context, String fname){
        File file = context.getFileStreamPath(fname);
        return file.exists();
    }

    /**
     * Writes a file to seconday storage
     * @param context
     * @param key Key to find file again
     * @param object object to store
     * @throws IOException
     */
    public static void writeObject(Context context, String key, Serializable object) throws IOException {
        FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(object);
        oos.close();
        fos.close();
    }

    /**
     * Read an object from secondary memory
     * @param context
     * @param key key to find file
     * @return the object in memory
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object readObject(Context context, String key) throws IOException, ClassNotFoundException {
        FileInputStream fis = context.openFileInput(key);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object object = ois.readObject();
        ois.close();
        fis.close();
        return object;
    }
}