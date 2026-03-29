package designs.d1_GoogleDocsDocumentEditor.design.goodDesign.elements;

import designs.d1_GoogleDocsDocumentEditor.design.goodDesign.interfaces.DocumentElement;

public class ImageElement implements DocumentElement {
    public String path;

    public ImageElement(String path){
        this.path = path;
    }

    @Override
    public String render() {
        return "[Image] : '" + path + "'";
    }
}
