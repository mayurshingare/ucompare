package dev.mayurshingare.ucompare;

public class Diff {

    public Type getType() {
        return type;
    }

    public FileInfo getData() {
        return data;
    }

    Type type;
    FileInfo data;

    public Diff(Type type, FileInfo data) {
        this.type = type;
        this.data = data;
    }

    public enum Type {
        INSERT, CHANGE, DELETE, EQUAL
    }

    @Override
    public String toString(){
        return type+" - "+data;
    }
}
