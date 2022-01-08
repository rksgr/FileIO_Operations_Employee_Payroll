import java.io.File;

public class FileUtils {
    public static boolean deleteFiles(File filesToDelete){
        File[] allFiles = filesToDelete.listFiles();
        System.out.println("before if block");
        if (allFiles != null){
            for (File file: allFiles){
                System.out.println("Inside if block before delete, file names: "+file.getName());
                deleteFiles(file);
                //System.out.println("Inside if block after delete, file names: "+file.getName());
            }
        }
        System.out.println("after if block");
        return filesToDelete.delete();
    }
}
