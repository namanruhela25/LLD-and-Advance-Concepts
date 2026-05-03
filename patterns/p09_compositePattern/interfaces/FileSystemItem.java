package patterns.p9_compositePattern.interfaces;

public interface FileSystemItem {
    void ls(int indent);            
    void openAll(int indent);      
    int getSize();                  
    FileSystemItem cd(String name); 
    String getName();
    boolean isFolder();
}