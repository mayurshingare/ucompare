package dev.mayurshingare.ucompare;

import org.apache.commons.vfs2.FileContent;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

public class FileInfo {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileInfo.class);
    String filePath;
    boolean isDirectory;
    long size;
    LocalDateTime lastModifiedTime;

    public String getFilePath() {
        return filePath;
    }

    protected FileInfo(FileObject fileObject) throws FileSystemException {
        filePath=fileObject.getName().getPathDecoded();
        isDirectory=fileObject.isFolder();
        FileContent fileContent=fileObject.getContent();
        if(!isDirectory){
            size=fileContent.getSize();
        }
        lastModifiedTime=LocalDateTime.ofInstant(Instant.ofEpochMilli(fileContent.getLastModifiedTime()), ZoneId.systemDefault());
    }

    protected FileInfo(FileObject fileObject, FileObject relativeToParent) throws FileSystemException {
        filePath=fileObject.getName().getPathDecoded();
        String parentPath= relativeToParent.getName().getPathDecoded();
        filePath = filePath.replaceFirst(parentPath,"");
        isDirectory=fileObject.isFolder();
        FileContent fileContent=fileObject.getContent();
        if(!isDirectory){
            size=fileContent.getSize();
        }
        lastModifiedTime=LocalDateTime.ofInstant(Instant.ofEpochMilli(fileContent.getLastModifiedTime()), ZoneId.systemDefault());
    }

    public String toString(){
        return String.format("%s,%b,%d",filePath,isDirectory,size);//,lastModifiedTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileInfo fileInfo = (FileInfo) o;
        return isDirectory == fileInfo.isDirectory && size == fileInfo.size && filePath.equals(fileInfo.filePath); //&& lastModifiedTime.equals(fileInfo.lastModifiedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filePath, isDirectory, size, lastModifiedTime);
    }
}
