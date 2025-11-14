package CVBuilder.Controllers;

import CVBuilder.Models.CV;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class CVFormController {

    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextArea addressArea;

    @FXML private VBox educationContainer;
    @FXML private VBox skillsContainer;
    @FXML private VBox experienceContainer;
    @FXML private VBox projectsContainer;

    @FXML private ImageView profileImageView;
    @FXML private Button uploadImageBtn;

    private String profileImageURI;

    @FXML
    public void initialize() {
        addEducation(null);
        addSkill(null);
        addExperience(null);
        addProject(null);

        profileImageView.setFitWidth(120);
        profileImageView.setFitHeight(120);
        profileImageView.setPreserveRatio(true);
    }

    private HBox entry(String prompt, boolean large) {
        TextInputControl input = large ? new TextArea() : new TextField();
        input.setPromptText(prompt);
        input.setPrefWidth(420);

        if (large) ((TextArea) input).setPrefRowCount(3);

        Button remove = new Button("X");
        remove.setOnAction(e -> ((VBox) ((HBox) remove.getParent()).getParent())
                .getChildren().remove(remove.getParent()));

        HBox box = new HBox(8, input, remove);
        box.setPadding(new Insets(4));
        return box;
    }

    @FXML private void addEducation(javafx.event.ActionEvent e) {
        educationContainer.getChildren().add(entry("Degree - Institute - Year", false));
    }

    @FXML private void addSkill(javafx.event.ActionEvent e) {
        skillsContainer.getChildren().add(entry("Skill (e.g., Java)", false));
    }

    @FXML private void addExperience(javafx.event.ActionEvent e) {
        experienceContainer.getChildren().add(entry("Job Title - Company - Duration", true));
    }

    @FXML private void addProject(javafx.event.ActionEvent e) {
        projectsContainer.getChildren().add(entry("Project - Description", true));
    }

    @FXML
    private void uploadImage() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fc.showOpenDialog(uploadImageBtn.getScene().getWindow());

        if (file != null) {
            profileImageURI = file.toURI().toString();
            profileImageView.setImage(new Image(profileImageURI));
        }
    }

    @FXML
    private void generateCV() {
        try {
            CV cv = new CV();
            cv.setFullName(fullNameField.getText());
            cv.setEmail(emailField.getText());
            cv.setPhone(phoneField.getText());
            cv.setAddress(addressArea.getText());
            cv.setProfileImageURI(profileImageURI);

            collect(educationContainer, cv.getEducations());
            collect(skillsContainer, cv.getSkills());
            collect(experienceContainer, cv.getExperiences());
            collect(projectsContainer, cv.getProjects());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CVBuilder/Preview.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("/CVBuilder/style.css").toExternalForm());

            PreviewController controller = loader.getController();
            controller.setCV(cv);

            Stage stage = (Stage) fullNameField.getScene().getWindow();
            stage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void collect(VBox box, java.util.List<String> list) {
        list.clear();
        for (Node n : box.getChildren()) {
            if (n instanceof HBox row) {
                for (Node c : row.getChildren()) {
                    if (c instanceof TextField tf) {
                        if (!tf.getText().isBlank()) list.add(tf.getText());
                    } else if (c instanceof TextArea ta) {
                        if (!ta.getText().isBlank()) list.add(ta.getText());
                    }
                }
            }
        }
    }

    @FXML
    private void goBack() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CVBuilder/Home.fxml"));
        Scene sc = new Scene(loader.load());
        Stage stage = (Stage) fullNameField.getScene().getWindow();
        stage.setScene(sc);
    }
}