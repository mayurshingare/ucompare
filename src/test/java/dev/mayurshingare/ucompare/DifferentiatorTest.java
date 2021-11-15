package dev.mayurshingare.ucompare;

import org.apache.commons.vfs2.FileSystemException;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;

import static org.testng.Assert.*;

public class DifferentiatorTest {

    @Test
    public void testDiffLocal(){
        LocalConnection conn1 = new LocalConnection(new File("D:\\Mayur"));
        LocalConnection conn2 = new LocalConnection(new File("D:\\test\\test3"));
        SFTPConnection conn3 = new SFTPConnection("20.86.144.211",22,"mayur","Magic@1234567","/var");
        try {
            List<FileInfo> left = conn1.listFiles();
            List<FileInfo> right = conn3.listFiles();
            List<Diff> diffs = Differentiator.diff(left,right,Object::equals);
            String diffStringHTML = Differentiator.diff2HTML(diffs);
            PrintWriter writer = new PrintWriter(new FileOutputStream("C:\\Users\\mayur\\Downloads\\diff.html"));
            writer.write(diffStringHTML);
            writer.close();
        } catch (FileSystemException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}