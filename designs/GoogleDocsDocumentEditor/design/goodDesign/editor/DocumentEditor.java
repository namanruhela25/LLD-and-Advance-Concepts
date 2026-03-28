package designs.GoogleDocsDocumentEditor.design.goodDesign.editor;

import designs.GoogleDocsDocumentEditor.design.goodDesign.document.Document;
import designs.GoogleDocsDocumentEditor.design.goodDesign.elements.TextElement;
import designs.GoogleDocsDocumentEditor.design.goodDesign.elements.ImageElement;
import designs.GoogleDocsDocumentEditor.design.goodDesign.elements.NewLineElement;
import designs.GoogleDocsDocumentEditor.design.goodDesign.elements.TabSpaceElement;
import designs.GoogleDocsDocumentEditor.design.goodDesign.interfaces.DocumentElement;
import designs.GoogleDocsDocumentEditor.design.goodDesign.interfaces.Persistence;

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
