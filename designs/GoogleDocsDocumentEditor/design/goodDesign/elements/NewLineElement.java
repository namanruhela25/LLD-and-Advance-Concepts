package designs.GoogleDocsDocumentEditor.design.goodDesign.elements;

import designs.GoogleDocsDocumentEditor.design.goodDesign.interfaces.DocumentElement;

public class NewLineElement implements DocumentElement {
    @Override
    public String render() {
        return "\n";
    }
}
