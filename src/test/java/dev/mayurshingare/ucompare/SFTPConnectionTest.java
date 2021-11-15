package dev.mayurshingare.ucompare;

import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Reporter;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class SFTPConnectionTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(SFTPConnectionTest.class);
    @Test
    public void testGetConnectionString1() {
        SFTPConnection connection=new SFTPConnection("www.mysftpserver.com",22,"admin","admin","/test");
        String expected = "sftp://admin:admin@www.mysftpserver.com:22/test";
        LOGGER.info(connection.getConnectionString());
        assertEquals(connection.getConnectionString(),expected);
    }

    @Test
    public void testGetConnectionString2() {
        SFTPConnection connection=new SFTPConnection("somehost",22,"myusername","mypassword","/pub/downloads/");
        String expected = "sftp://myusername:mypassword@somehost:22/pub/downloads/";
        Reporter.log(connection.getConnectionString());
        LOGGER.info(connection.getConnectionString());
        assertEquals(connection.getConnectionString(),expected);
    }

    @Test
    public void testListFilesSSHConnection() {
        SFTPConnection connection = new SFTPConnection("20.86.144.211",22,"mayur","Magic@1234567","/root");

        try{
            List<FileInfo> files = connection.listFiles();
            files.forEach(fileInfo -> LOGGER.info("{}",fileInfo));
            assertTrue(files.size()>0);
        }catch(FileSystemException e){
            LOGGER.error("Error while listing files - {} - {}",connection,e);
        }
    }
}