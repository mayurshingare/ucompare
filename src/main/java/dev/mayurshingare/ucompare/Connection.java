package dev.mayurshingare.ucompare;

import org.apache.commons.vfs2.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public interface Connection {
    String getConnectionString();

    default FileSystemOptions getOptions(){
        FileSystemOptions options = new FileSystemOptions();
        return options;
    }

    default List<FileInfo> listFiles() throws FileSystemException {
        Logger LOGGER = LoggerFactory.getLogger(this.getClass());
        Queue<FileObject> queue=new ArrayDeque<>();
        ArrayList<FileInfo> files=new ArrayList<>();
        FileSystemManager manager = VFS.getManager();

        LOGGER.info("Resolving {}",getConnectionString());
        FileObject parentPath = manager.resolveFile(getConnectionString(),getOptions());

        queue.add(parentPath);
        while(!queue.isEmpty()){
            FileObject nextPath = queue.remove();
            if(nextPath!=parentPath){
                LOGGER.info("Parsing info for file - {}",nextPath);
                FileInfo fileInfo = new FileInfo(nextPath,parentPath);
                LOGGER.info("Parsed {} to {}",nextPath,fileInfo);
                files.add(fileInfo);
            }

            if(nextPath.isFolder() && nextPath.isExecutable() && nextPath.isReadable()){
                try{
                    FileObject[] children = nextPath.getChildren();
                    for(FileObject file:children){
                        queue.add(file);
                        LOGGER.info("Added to queue - {}",file);
                    }
                } catch(FileSystemException e){
                    LOGGER.error("Error listing files for {}",nextPath,e);
                }
            }
        }
        return files;
    }
}
