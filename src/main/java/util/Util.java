package util;

import java.io.File;

public class Util {
    //Src: https://stackoverflow.com/questions/412380/how-to-combine-paths-in-java
    public static String joinPaths(String path1, String path2) {
        File file1 = new File(path1);
        File file2 = new File(file1, path2);
        return file2.getPath();
    }
}
