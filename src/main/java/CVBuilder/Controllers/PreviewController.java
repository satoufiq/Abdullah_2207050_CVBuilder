package CVBuilder.Controllers;

import CVBuilder.db.CVDao;
import CVBuilder.models.CV;

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
    private final CVDao dao = new CVDao();

    /**
     * Sets CV from Form or SavedCVs list
     * Always fetch fresh from DB to avoid stale data.
     */
    public void setCV(CV cv) {
        try {
            if (cv.getId() != 0)
                this.cv = dao.findById(cv.getId());
            else
                this.cv = cv;  // Should never happen unless misused

        } catch (Exception e) {
            e.printStackTrace();
            this.cv = cv;
        }

        load();
    }

    /** Load values into preview UI */
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
            Label label = new Label("• " + s);
            label.setPadding(new Insets(3));
            label.setWrapText(true);
            box.getChildren().add(label);
        }
    }

    /** Open CVForm with loaded CV */
    @FXML
    private void editCV() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CVBuilder/CVForm.fxml"));
        Scene scene = new Scene(loader.load());

        CVFormController controller = loader.getController();
        controller.loadCV(cv);

        Stage stage = (Stage) nameLabel.getScene().getWindow();
        stage.setScene(scene);
    }

    /** Go to Home screen */
    @FXML
    private void goHome() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CVBuilder/Home.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) nameLabel.getScene().getWindow();
        stage.setScene(scene);
    }

    /** Go to Saved CVs screen */
    @FXML
    private void goSavedCVs() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CVBuilder/SavedCVs.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) nameLabel.getScene().getWindow();
        stage.setScene(scene);
    }
}
