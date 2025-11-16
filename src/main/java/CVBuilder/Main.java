package CVBuilder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CVBuilder/Home.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass().getResource("/CVBuilder/style.css").toExternalForm());

        stage.setTitle("CV Builder");
        stage.setScene(scene);

        try {
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/CVBuilder/icon.png")));
        } catch (Exception ignored) {}
        stage.setWidth(900);
        stage.setHeight(650);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
