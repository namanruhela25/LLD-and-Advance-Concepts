package patterns.p9_compositePattern.composite;

import java.util.ArrayList;
import java.util.List;

import patterns.p9_compositePattern.interfaces.FileSystemItem;

public class Folder implements FileSystemItem {
    private String name;
    private List<FileSystemItem> items;

    public Folder(String name) {
        this.name = name;
        this.items = new ArrayList<>();
    }

    public void addItem(FileSystemItem item) {
        items.add(item);
    }

    @Override
    public void ls(int indent) {
        System.out.println(" ".repeat(indent) + name + "/");
        for (FileSystemItem item : items) {
            item.ls(indent + 2);
        }
    }

    @Override
    public void openAll(int indent) {
        System.out.println(" ".repeat(indent) + "Opening folder: " + name);
        for (FileSystemItem item : items) {
            item.openAll(indent + 2);
        }
    }

    @Override
    public int getSize() {
        int totalSize = 0;
        for (FileSystemItem item : items) {
            totalSize += item.getSize();
        }
        return totalSize;
    }

    @Override
    public FileSystemItem cd(String name) {
        for (FileSystemItem item : items) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        return null; // Not found
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isFolder() {
        return true; // This is a folder
    }
    
}
