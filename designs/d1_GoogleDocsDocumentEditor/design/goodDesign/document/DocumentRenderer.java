package designs.d1_GoogleDocsDocumentEditor.design.goodDesign.document;

import java.util.List;

import designs.d1_GoogleDocsDocumentEditor.design.goodDesign.interfaces.DocumentElement;

public class DocumentRenderer {
    private Document document;

    public DocumentRenderer(Document document) {
        this.document = document;
    }

    public String render() {
        List<DocumentElement> elements = document.getElements();
        StringBuilder sb = new StringBuilder();
        for (DocumentElement element : elements) {
            sb.append(element.render());
        }
        return sb.toString();
    }
}
