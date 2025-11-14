package CVBuilder.Controllers;

import CVBuilder.Models.CV;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PreviewController {

    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label phoneLabel;
    @FXML private Label addressLabel;

    @FXML private ImageView profileImageView;

    @FXML private VBox educationBox;
    @FXML private VBox skillsBox;
    @FXML private VBox experienceBox;
    @FXML private VBox projectsBox;

    private CV cv;

    public void setCV(CV cv) {
        this.cv = cv;
        load();
    }

    private void load() {
        nameLabel.setText(cv.getFullName());
        emailLabel.setText(cv.getEmail());
        phoneLabel.setText(cv.getPhone());
        addressLabel.setText(cv.getAddress());

        if (cv.getProfileImageURI() != null)
            profileImageView.setImage(new Image(cv.getProfileImageURI()));

        fill(educationBox, cv.getEducations());
        fill(skillsBox, cv.getSkills());
        fill(experienceBox, cv.getExperiences());
        fill(projectsBox, cv.getProjects());
    }

    private void fill(VBox box, java.util.List<String> list) {
        box.getChildren().clear();
        for (String s : list) {
            Label l = new Label("• " + s);
            l.setPadding(new Insets(3));
            l.setWrapText(true);
            box.getChildren().add(l);
        }
    }

    @FXML
    private void goBack() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CVBuilder/CVForm.fxml"));
        Scene sc = new Scene(loader.load());
        Stage stage = (Stage) nameLabel.getScene().getWindow();
        stage.setScene(sc);
    }
}