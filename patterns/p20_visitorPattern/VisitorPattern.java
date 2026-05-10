package patterns.p20_visitorPattern;


interface FileSystemVisitor{
    void visit(TextFile t);
    void visit(VideoFile v);
    void visit(ImageFile i);
}

abstract class FileSystemItem {
    private String name;

    public FileSystemItem(String n) {
        this.name = n;
    }

    public String getName() {
        return name;
    }

    public abstract void accept(FileSystemVisitor v);

}

class TextFile extends FileSystemItem {
    private String content;
    
    public TextFile(String fileName, String fileContent) {
        super(fileName);
        this.content = fileContent;
    }
    
    public String getContent() {
        return content;
    }
    
    @Override
    public void accept(FileSystemVisitor visitor) {
        visitor.visit(this);
    }
}

class ImageFile extends FileSystemItem {
    
    public ImageFile(String fileName) {
        super(fileName);
    }
    
    @Override
    public void accept(FileSystemVisitor visitor) {
        visitor.visit(this);
    }
}

class VideoFile extends FileSystemItem {
    public VideoFile(String fileName) {
        super(fileName);
    }
    
    @Override
    public void accept(FileSystemVisitor visitor) {
        visitor.visit(this);
    }
}

// 1. Size calculation visitor
class SizeCalculationVisitor implements FileSystemVisitor {
    @Override
    public void visit(TextFile file) {
        System.out.println("Calculating size for TEXT file: " + file.getName());
    }
    
    @Override
    public void visit(ImageFile file) {
        System.out.println("Calculating size for IMAGE file: " + file.getName());
    }
    
    @Override
    public void visit(VideoFile file) {
        System.out.println("Calculating size for VIDEO file: " + file.getName());
    }
}

// 2. Compression Visitor
class CompressionVisitor implements FileSystemVisitor {
    @Override
    public void visit(TextFile file) {
        System.out.println("Compressing TEXT file: " + file.getName());
    }
    
    @Override
    public void visit(ImageFile file) {
        System.out.println("Compressing IMAGE file: " + file.getName());
    }
    
    @Override
    public void visit(VideoFile file) {
        System.out.println("Compressing VIDEO file: " + file.getName());
    }
}

// 3. Virus Scanning Visitor
class VirusScanningVisitor implements FileSystemVisitor {
    @Override
    public void visit(TextFile file) {
        System.out.println("Scanning TEXT file: " + file.getName());
    }
    
    @Override
    public void visit(ImageFile file) {
        System.out.println("Scanning IMAGE file: " + file.getName());
    }
    
    @Override
    public void visit(VideoFile file) {
        System.out.println("Scanning VIDEO file: " + file.getName());
    }
}


public class VisitorPattern {
    public static void main(String[] args) {
        FileSystemItem img = new ImageFile("image.jpg");
        img.accept(new VirusScanningVisitor());
        img.accept(new CompressionVisitor());
    }   
}
