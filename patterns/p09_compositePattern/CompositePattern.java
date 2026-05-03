package patterns.p9_compositePattern;

import patterns.p9_compositePattern.composite.Folder;
import patterns.p9_compositePattern.interfaces.FileSystemItem;
import patterns.p9_compositePattern.leaf.File;

public class CompositePattern {
    public static void main(String[] args) {
        // Create root folder
        Folder root = new Folder("root");

        // Create subfolders and files
        Folder documents = new Folder("documents");
        Folder photos = new Folder("photos");
        File file1 = new File("resume.pdf", 500);
        File file2 = new File("vacation.jpg", 1500);

        // Build the file system structure
        root.addItem(documents);
        root.addItem(photos);
        documents.addItem(file1);
        photos.addItem(file2);

        // Display the file system structure
        System.out.println("File System Structure:");
        root.ls(0);

        // Open all items in the file system
        System.out.println("\nOpening all items:");
        root.openAll(0);

        // Get total size of the file system
        System.out.println("\nTotal size: " + root.getSize() + " KB");

        // Navigate to a specific folder
        FileSystemItem found = root.cd("documents");
        if (found != null) {
            System.out.println("\nFound folder: " + found.getName());
            found.ls(0);
        } else {
            System.out.println("\nFolder not found.");
        }
    }
}
