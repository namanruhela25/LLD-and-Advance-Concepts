package designs.d1_GoogleDocsDocumentEditor.design.goodDesign.elements;

import designs.d1_GoogleDocsDocumentEditor.design.goodDesign.interfaces.DocumentElement;

public class NewLineElement implements DocumentElement {
    @Override
    public String render() {
        return "\n";
    }
}
