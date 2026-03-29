package designs.d1_GoogleDocsDocumentEditor.design.goodDesign.document;

import java.util.ArrayList;
import java.util.List;

import designs.d1_GoogleDocsDocumentEditor.design.goodDesign.interfaces.DocumentElement;

public class Document {
    private List<DocumentElement> elements = new ArrayList<>();

    public void addElement(DocumentElement element) {
        elements.add(element);
    }

    public List<DocumentElement> getElements() {
        return elements;
    }
}
