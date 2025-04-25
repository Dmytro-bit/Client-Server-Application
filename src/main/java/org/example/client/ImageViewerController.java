package org.example.client;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ImageViewerController {

    @FXML
    private HBox imageContainer;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void addImage(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(200);
        imageView.setPreserveRatio(true);
        imageContainer.getChildren().add(imageView);
    }

    @FXML
    public void handleClose() {
        if (stage != null) {
            stage.close();
        }
    }
}