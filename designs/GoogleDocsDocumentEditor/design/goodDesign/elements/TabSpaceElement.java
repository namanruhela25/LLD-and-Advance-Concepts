package designs.GoogleDocsDocumentEditor.design.goodDesign.elements;

import designs.GoogleDocsDocumentEditor.design.goodDesign.interfaces.DocumentElement;

public class TabSpaceElement implements DocumentElement {
    @Override
    public String render() {
        return "\t";
    }
}
