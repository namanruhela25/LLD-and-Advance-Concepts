package patterns.p9_compositePattern.leaf;
import patterns.p9_compositePattern.interfaces.FileSystemItem;

public class File implements FileSystemItem {
    private String name;
    private int size;

    public File(String name, int size) {
        this.name = name;
        this.size = size;
    }

    @Override
    public void ls(int indent) {
        System.out.println(" ".repeat(indent) + name);
    }

    @Override
    public void openAll(int indent) {
        System.out.println(" ".repeat(indent) + "Opening file: " + name);
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public FileSystemItem cd(String name) {
        return null; // Files cannot contain other items
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isFolder() {
        return false; // This is a file, not a folder
    }
    
}
