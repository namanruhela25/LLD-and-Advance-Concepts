package designs.d1_GoogleDocsDocumentEditor.design.badDesign;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

class DocumentEditor {
    private List<String> documentElements;
    private String renderedDocument;

    public DocumentEditor() {
        this.documentElements = new ArrayList<>();
        renderedDocument = "";
    }

    public void addText(String text) { 
        documentElements.add(text);
    }

    public void addImage(String path) {
        documentElements.add(path);
    }

    public String renderDocument() {
        if (renderedDocument.isEmpty()) {
            StringBuilder result = new StringBuilder();
            for (String element : documentElements) {
                if (element.length() > 4 && 
                   (element.endsWith(".jpg") || element.endsWith(".png"))) {
                    result.append("[Image: ").append(element).append("]\n");
                } else {
                    result.append(element).append("\n");
                }
            }
            renderedDocument = result.toString();
        }
        return renderedDocument;
    }

    public void saveToFile() {
        try {
            FileWriter file = new FileWriter("document.txt");
            file.write(renderDocument());
            file.close();
        } catch (Exception e) {
            System.out.println("Exception " + e.getMessage());
        }

    }
}

public class DocumentEditorClient {
    public static void main(String[] args) {

        DocumentEditor editor = new DocumentEditor();

        editor.addText("Naman Ruhela");
        editor.addImage("photo.jpg");
        editor.addText("This is document editor");


        System.out.println(editor.renderDocument());

        editor.saveToFile();
    }
}
