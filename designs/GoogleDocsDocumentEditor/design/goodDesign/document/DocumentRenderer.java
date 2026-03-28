package designs.GoogleDocsDocumentEditor.design.goodDesign.document;

import designs.GoogleDocsDocumentEditor.design.goodDesign.interfaces.DocumentElement;
import java.util.List;

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
