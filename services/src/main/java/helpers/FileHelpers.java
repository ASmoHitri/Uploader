package helpers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class FileHelpers {
    private static String rootDir = "/files/";
    private static String currentDir = null;
    private static int currentDirFiles = 0;
    private static final int maxFilesPerDir = 20000;
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss-");

    public static String getCurrentDir() {
        if (currentDir == null || currentDirFiles >= maxFilesPerDir) {
            currentDir = dateFormat.format(new Date()) + UUID.randomUUID().toString() + "/";   // current datetime + randomUUID
            currentDirFiles = 0;
        }
        return rootDir + currentDir;
    }

    public static void increaseNumOfFiles() {
        currentDirFiles++;
    }

    public static String removeRootDirName(String path) {
        int rootLength = rootDir.length();
        return path.substring(rootLength-1);
    }
}
