package dev.mayurshingare.ucompare;

import java.io.File;

public class LocalConnection implements Connection {
    File path;

    public LocalConnection(File path) {
        this.path = path;
    }

    public String getConnectionString(){
        return path.toURI().toString();
    }
}
