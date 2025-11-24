package CVBuilder.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import java.net.URL;

public class HomeController {

    @FXML
    private void onViewSavedCVs(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CVBuilder/SavedCVs.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Saved CVs");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void onCreateNewCV(ActionEvent event) {
        System.out.println(">>> onCreateNewCV invoked");

        URL url = getClass().getResource("/CVBuilder/CVForm.fxml");
        System.out.println(">>> resource URL = " + url);

        if (url == null) {
            System.err.println("ERROR: /CVBuilder/CVForm.fxml not found on classpath!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(url);
            Scene scene = new Scene(loader.load());


            scene.getStylesheets().add(
                    getClass().getResource("/CVBuilder/style.css").toExternalForm()
            );

            System.out.println(">>> CVForm.fxml loaded successfully");

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Create CV");// new scene title for cvform.fxml

            System.out.println(">>> Scene switched to CVForm.fxml");

        } catch (Exception ex) {
            System.err.println("ERROR: Exception while loading CVForm.fxml");
            ex.printStackTrace();
        }
    }
}
