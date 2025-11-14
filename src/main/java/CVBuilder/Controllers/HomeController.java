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
    private void onCreateNewCV(ActionEvent event) {
        System.out.println(">>> onCreateNewCV invoked");

        // 1) check resource URL
        URL url = getClass().getResource("/CVBuilder/CVForm.fxml");
        System.out.println(">>> resource URL = " + url);

        if (url == null) {
            System.err.println("ERROR: /CVBuilder/CVForm.fxml not found on classpath!");
            System.err.println("Check src/main/resources/CVBuilder/CVForm.fxml (capitalization matters).");
            return;
        }

        // 2) attempt to load and catch explicit exceptions
        try {
            FXMLLoader loader = new FXMLLoader(url);
            Scene scene = new Scene(loader.load());
            System.out.println(">>> CVForm.fxml loaded successfully");

            // 3) get Stage from event source and set scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Create CV");
            System.out.println(">>> Scene switched to CVForm.fxml");

        } catch (Exception ex) {
            System.err.println("ERROR: Exception while loading CVForm.fxml");
            ex.printStackTrace();
        }
    }
}
