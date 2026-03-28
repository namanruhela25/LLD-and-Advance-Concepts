package designs.GoogleDocsDocumentEditor.design.goodDesign.document;

import designs.GoogleDocsDocumentEditor.design.goodDesign.interfaces.DocumentElement;
import java.util.ArrayList;
import java.util.List;

public class Document {
    private List<DocumentElement> elements = new ArrayList<>();

    public void addElement(DocumentElement element) {
        elements.add(element);
    }

    public List<DocumentElement> getElements() {
        return elements;
    }
}
