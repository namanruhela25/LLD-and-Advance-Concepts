package designs.d1_GoogleDocsDocumentEditor.design.goodDesign.editor;

import designs.d1_GoogleDocsDocumentEditor.design.goodDesign.document.Document;
import designs.d1_GoogleDocsDocumentEditor.design.goodDesign.elements.ImageElement;
import designs.d1_GoogleDocsDocumentEditor.design.goodDesign.elements.NewLineElement;
import designs.d1_GoogleDocsDocumentEditor.design.goodDesign.elements.TabSpaceElement;
import designs.d1_GoogleDocsDocumentEditor.design.goodDesign.elements.TextElement;
import designs.d1_GoogleDocsDocumentEditor.design.goodDesign.interfaces.DocumentElement;
import designs.d1_GoogleDocsDocumentEditor.design.goodDesign.interfaces.Persistence;

public class DocumentEditor {
    private Document document;
    private Persistence persistence;

    public DocumentEditor(Document document) {
        this.document = document;
    }

    public DocumentEditor(Document document, Persistence persistence) {
        this.document = document;
        this.persistence = persistence;
    }

    public void addText(String text) {
        document.addElement(new TextElement(text));
    }

    public void addImage(String img) {
        document.addElement(new ImageElement(img));
    }

    public void addNewLine() {
        document.addElement(new NewLineElement());
    }

    public void addTabSpace() {
        document.addElement(new TabSpaceElement());
    }

    public void insertElement(DocumentElement element) {
        document.addElement(element);
    }

    public void save(String data) {
        if (persistence != null) {
            persistence.save(data);
        }
    }
}
