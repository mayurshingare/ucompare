package dev.mayurshingare.ucompare;


import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class SFTPConnection implements Connection{
    private static final Logger LOGGER = LoggerFactory.getLogger(SFTPConnection.class);
    String username, password, hostname, path;
    int port;

    public SFTPConnection(String hostname, int port, String username, String password,String path) {
        this.username = username;
        this.password = password;
        this.hostname = hostname;
        this.path = path;
        this.port = port;
    }

    @Override
    public FileSystemOptions getOptions(){
        FileSystemOptions options = Connection.super.getOptions();
        SftpFileSystemConfigBuilder builder = SftpFileSystemConfigBuilder.getInstance();
        builder.setUserDirIsRoot(options,false);
        return options;
    }

    public String getConnectionString(){
        String connectionString="";
        try{
            URI connectionURI = new URI("sftp",username+":"+password,hostname,port,path,null,null);
            connectionString=connectionURI.toString();
        }catch(URISyntaxException e){
            LOGGER.error("Error creating URL: {} - {}",this,e);
        }
        return connectionString;
    }

    public String toString(){
        return String.format("sftp://%s:%s@%s:%d%s",username,password,hostname,port,path);
    }
}
