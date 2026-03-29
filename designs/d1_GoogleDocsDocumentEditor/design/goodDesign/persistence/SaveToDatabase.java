package designs.d1_GoogleDocsDocumentEditor.design.goodDesign.persistence;

import designs.d1_GoogleDocsDocumentEditor.design.goodDesign.interfaces.Persistence;

public class SaveToDatabase implements Persistence {
    @Override
    public void save(String data) {
        // Database logic
        System.out.println("Saving to database");
    }
}
