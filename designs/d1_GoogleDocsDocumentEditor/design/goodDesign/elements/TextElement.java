package designs.d1_GoogleDocsDocumentEditor.design.goodDesign.elements;

import designs.d1_GoogleDocsDocumentEditor.design.goodDesign.interfaces.DocumentElement;

public class TextElement implements DocumentElement {
    public String text;

    public TextElement(String text){
        this.text = text;
    }

    @Override
    public String render() {
        return text;
    }
}
