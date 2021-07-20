package ui.components;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class FolderView{

    public static final ImageView IMAGE_VIEW = new ImageView(new Image("folder.jpg"));
    private VBox root = new VBox();

    public FolderView(){
        IMAGE_VIEW.setMouseTransparent(true);
//        Border border = new Border(new BorderStroke(Paint.valueOf("red"), BorderStrokeStyle.SOLID,new CornerRadii(2),new BorderWidths(2)));
//        root.setBorder(border);
    }

    public Node getFolderView(){
        return this.root;
    }
}
