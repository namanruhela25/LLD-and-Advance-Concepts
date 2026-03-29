package designs.d1_GoogleDocsDocumentEditor.design.goodDesign;

import designs.d1_GoogleDocsDocumentEditor.design.goodDesign.document.Document;
import designs.d1_GoogleDocsDocumentEditor.design.goodDesign.document.DocumentRenderer;
import designs.d1_GoogleDocsDocumentEditor.design.goodDesign.editor.DocumentEditor;
import designs.d1_GoogleDocsDocumentEditor.design.goodDesign.elements.*;
import designs.d1_GoogleDocsDocumentEditor.design.goodDesign.interfaces.Persistence;
import designs.d1_GoogleDocsDocumentEditor.design.goodDesign.persistence.SaveToFile;

public class DocumentEditorClient {
    public static void main(String[] args) {

        Document document = new Document();

        Persistence savetoFile = new SaveToFile();

        DocumentRenderer renderer = new DocumentRenderer(document);

        DocumentEditor editor = new DocumentEditor(document);

        editor.addText("Naman Ruhela");
        editor.addNewLine();
        editor.addText("This is a document editor.");
        editor.addNewLine();
        editor.addTabSpace();
        editor.addText("Good design of document editor");
        editor.addNewLine();
        editor.addImage("photo.jpg");

        String renderedDocument = renderer.render();
        System.out.println(renderedDocument);

        savetoFile.save(renderedDocument);
    }
}