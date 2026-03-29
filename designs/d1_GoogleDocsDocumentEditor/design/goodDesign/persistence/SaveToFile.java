package designs.d1_GoogleDocsDocumentEditor.design.goodDesign.persistence;

import java.io.FileWriter;

import designs.d1_GoogleDocsDocumentEditor.design.goodDesign.interfaces.Persistence;

public class SaveToFile implements Persistence {
    @Override
    public void save(String data) {
        try {
            FileWriter writer = new FileWriter("document.txt");
            writer.write(data);
            writer.close();
            System.out.println("Saved to file");
        } catch (Exception e) {
            System.out.println("Exception : "+e);
        }
    }
}
