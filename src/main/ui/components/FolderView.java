package ui.components;

import javafx.scene.Node;
import javafx.scene.layout.*;

public class FolderView{

//    public final static ImageView IMAGE_VIEW = new ImageView(new Image("folder.jpg"));
    private final static FolderView folderView = new FolderView();
    private final VBox root = new VBox();


//    static {
//        IMAGE_VIEW.setMouseTransparent(true);
//        Border border = new Border(new BorderStroke(Paint.valueOf("red"), BorderStrokeStyle.SOLID,new CornerRadii(2),new BorderWidths(2)));
//        root.setBorder(border);
//    }
    private FolderView(){}

    /**
     * <p>This Method is the "Constructor" for the FolderView class.</p>
     * <p>This is the only way to access the FolderView.</p>
     * @return a {@link FolderView} instance
     */
    public static FolderView getInstance() {
        return folderView;
    }

    /**
     * <p>This will return view of the FolderView class.</p>
     * @return the {@link FolderView} {@link Node}.
     */
    public Node getFolderView(){
        return this.root;
    }
}
