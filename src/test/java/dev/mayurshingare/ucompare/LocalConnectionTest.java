package dev.mayurshingare.ucompare;

import org.apache.commons.vfs2.FileSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;

public class LocalConnectionTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalConnectionTest.class);

    @Test
    public void testListFilesLocalConnection() {
        LocalConnection connection=new LocalConnection(new File("C:\\Users\\omkar"));
        try{
            List<FileInfo> files = connection.listFiles();
            files.forEach(fileInfo -> LOGGER.info("{}",fileInfo));
            assertTrue(files.size()>0);
        }catch(FileSystemException e){
            LOGGER.error("Error while listing files - {} - {}",connection,e);
        }
    }
}