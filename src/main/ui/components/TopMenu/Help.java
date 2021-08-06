package ui.components.TopMenu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.miscellaneous.Output;
import logic.startup.Launcher;
import logic.startup.Updater;
import ui.MainUI;

import java.awt.*;
import java.net.URI;

// TODO Sort Private/Public
public class Help {

    private final static Help HELP = new Help();
    private final Menu root = new Menu();
    private final Updater updater = Updater.getInstance();


    static {
        HELP.root.setText("Hilfe");
        HELP.setMenu();
    }

    private Help() {}

    private void setMenu() {

        MenuItem mI_version = getVersionMenuItemIfAvailable();
        MenuItem mI_uber = getUberMenuItem();
        if (mI_version != null) {
            root.getItems().addAll(mI_version,new SeparatorMenuItem());
        }
        root.getItems().add(mI_uber);
    }

    private MenuItem getVersionMenuItemIfAvailable() {
        if (updater.isAvailable()) {
            MenuItem updateItem = new MenuItem("Neue Version Verfügbar!");
            updateItem.setOnAction(event -> showUpdateMenu());
            return updateItem;
        } else {
            return null;
        }
    }

    private MenuItem getUberMenuItem() {
        MenuItem uberMenu = new MenuItem("Über");
        uberMenu.setOnAction(event -> showUberMenu());
        return uberMenu;
    }

    private void showUpdateMenu() {
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20));
        Scene scene = new Scene(vBox);
        MainUI.getInstance().setInitOwner(stage);
        stage.setScene(scene);
        stage.setTitle("Update verfügbar");


        Label updateLabel = new Label("Ein neues Update ist von\r\n" +
                "Version " + updater.getCurrentVersion() + " auf Version " + updater.getNewVersion() + " Verfügbar.");
        updateLabel.setAlignment(Pos.CENTER);
        updateLabel.setTextAlignment(TextAlignment.CENTER);
        updateLabel.setPadding(new Insets(10));

        HBox hBox = new HBox();
        hBox.setSpacing(7.5);
        hBox.setPadding(new Insets(0, 0, 10, 0));
        hBox.setAlignment(Pos.CENTER);
        Button downloadButton = new Button("Start Download");
        Button cancelButton = new Button("Abbrechen");
        hBox.getChildren().addAll(downloadButton, cancelButton);

        downloadButton.setOnAction(event -> {
            try {
                updater.loadUpdateAndRestartApplication(Launcher.class);
            } catch (Exception e) {
                Output.exceptionWrite(e);
            } finally {
                if (stage.isShowing()) {
                    stage.close();
                }
            }
        });
        cancelButton.setOnAction(event -> stage.close());

        VBox patchNotesBox = new VBox();
        Label label = new Label("Patchnotes:");
        Text text = new Text(updater.getNewPatchNotes());
        patchNotesBox.setStyle("-fx-background-color: LIGHTGRAY;");
        patchNotesBox.getChildren().addAll(label, text);

        Label downloadQuestionLabel = new Label("Downloaden?");
        downloadQuestionLabel.setPadding(new Insets(10, 0, 5, 0));

        vBox.getChildren().addAll(updateLabel, patchNotesBox, downloadQuestionLabel, hBox);
        stage.show();
    }

    private void showUberMenu() {
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPrefWidth(400);
        vBox.setPadding(new Insets(20));
        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.setTitle("Über LernSoftware");
        MainUI.getInstance().setInitOwner(stage);

        Label headerLabel = new Label("LernSoftware");
        headerLabel.setFont(Font.font(headerLabel.getFont().getSize()+2));
        headerLabel.setStyle("-fx-font-weight: bold;");

        HBox hBox = new HBox();
        Label label = new Label("Open Source Software - ");
        Hyperlink website = new Hyperlink("Website");
        website.setBorder(Border.EMPTY);
        website.setPadding(new Insets(4, 0, 4, 0));
        website.setOnAction(event -> openSite("http://Rouven-Ra-Ro.de/LernSoftware"));
        Hyperlink github = new Hyperlink("Github");
        github.setBorder(Border.EMPTY);
        github.setPadding(new Insets(4, 0, 4, 0));
        github.setOnAction(event -> openSite("https://github.com/KarasuRou/LernSoftware"));
        hBox.getChildren().addAll(label, website, new Label(" - "), github);

        Label label1 = new Label("Momentane Version: " + updater.getCurrentVersion());
        Label label2 = new Label("Momentane Patchnotes: \r\n" + updater.getCurrentPatchNotes());
        Label label3 = new Label("Copyright © 2021 Rouven Tjalf Rosploch ");


        vBox.getChildren().addAll(headerLabel, hBox, label1, label2, label3);
        stage.show();
    }

    private void openSite(String site) {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(site));
            }
        } catch (Exception e) {
            Output.exceptionWrite(e);
        }
    }


    /**
     * <p>This Method is the "Constructor" for the Help class.</p>
     * <p>This is the only way to access the Help.</p>
     * @return a {@link Help} instance
     */
    public static Help getInstance(){
        return HELP;
    }

    public Menu getHelp() {
        return root;
    }

    private Stage getPopUpStage(Parent root) { //TODO use in other PopUp's
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        MainUI.getInstance().setInitOwner(stage);
        return stage;
    }
}
