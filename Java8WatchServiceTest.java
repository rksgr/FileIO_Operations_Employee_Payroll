import java.nio.file.WatchEvent;
import java.nio.file.WatchService;
import java.nio.file.WatchKey;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.SimpleFileVisitor;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import java.nio.file.attribute.BasicFileAttributes;



public class Java8WatchServiceTest {
    private final WatchService watcher;
    private final Map<WatchKey, Path> dirWatchers;

    /* Creates a WatchService and registers the given directory */
    Java8WatchServiceTest(Path dir) throws IOException{
        this.watcher = FileSystems.getDefault().newWatchService();
        this.dirWatchers = new HashMap<WatchKey, Path>();
        scanAndRegisterDirectories(dir);
    }
    /* Register the given directory with the WatchService */
    private void registerDirWatchers(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        dirWatchers.put(key,dir);
    }
    /* Register the given directory and all its sub-directories with the WatchService. */
    private void scanAndRegisterDirectories(final Path start) throws IOException{
        // Register directory and sub-directories
        Files.walkFileTree(start, new SimpleFileVisitor<Path>(){
            public FileVisitResult preVisitDirectory(Path dir,BasicFileAttributes basicattrs)
                throws IOException{
                registerDirWatchers(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
    void processEvents(){
        while(true){
            WatchKey key;
            try{
                key = watcher.take();
            }catch (InterruptedException e){return;}
            Path dir = dirWatchers.get(key);
            if (dir == null) continue;
            for (WatchEvent<?> event: key.pollEvents()){
                WatchEvent.Kind kind = event.kind();
                Path name = ((WatchEvent<Path>)event).context();
                Path child = dir.resolve(name);
                System.out.format("%s: %s\n",event.kind().name(), child);

                if (kind == ENTRY_CREATE){
                    try{
                        if (Files.isDirectory(child)) scanAndRegisterDirectories(child);
                    } catch (IOException x){}
                } else if (kind.equals(ENTRY_DELETE)){
                    if (Files.isDirectory(child)) dirWatchers.remove(key);
                }
            }
            boolean valid = key.reset();
            if (!valid){
                dirWatchers.remove(key);
                if (dirWatchers.isEmpty()) break;   // All directories are inaccessible
            }
        }
    }
}
