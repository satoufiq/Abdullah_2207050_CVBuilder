package CVBuilder.Controllers;

import CVBuilder.models.CV;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
//package CVBuilder.Controllers;

import CVBuilder.models.CV;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    @FXML private ScrollPane scrollPane;

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

    public void loadCV(CV cv) {

        fullNameField.setText(cv.getFullName());
        emailField.setText(cv.getEmail());
        phoneField.setText(cv.getPhone());
        addressArea.setText(cv.getAddress());

        profileImageURI = cv.getProfileImageURI();
        if (profileImageURI != null) {
            profileImageView.setImage(new Image(profileImageURI));
        }

        educationContainer.getChildren().clear();
        skillsContainer.getChildren().clear();
        experienceContainer.getChildren().clear();
        projectsContainer.getChildren().clear();

        for (String s : cv.getEducations()) educationContainer.getChildren().add(entry(s, false));
        for (String s : cv.getSkills()) skillsContainer.getChildren().add(entry(s, false));
        for (String s : cv.getExperiences()) experienceContainer.getChildren().add(entry(s, true));
        for (String s : cv.getProjects()) projectsContainer.getChildren().add(entry(s, true));

        scrollPane.setVvalue(0);
    }

    private HBox entry(String text, boolean large) {
        TextInputControl input = large ? new TextArea() : new TextField();
        input.setPrefWidth(420);

        input.setText(text);
        input.setPromptText(text);

        if (large) ((TextArea) input).setPrefRowCount(3);

        Button remove = new Button("X");
        remove.setOnAction(e -> ((VBox) ((HBox) remove.getParent()).getParent())
                .getChildren()
                .remove(remove.getParent()));

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
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        File file = chooser.showOpenDialog(uploadImageBtn.getScene().getWindow());
        if (file != null) {
            profileImageURI = file.toURI().toString();
            profileImageView.setImage(new Image(profileImageURI));
        }
    }

    private boolean validateForm() {
        if (fullNameField.getText().isBlank()
                || emailField.getText().isBlank()
                || phoneField.getText().isBlank()
                || addressArea.getText().isBlank()
                || profileImageURI == null
        ) {
            new Alert(Alert.AlertType.ERROR, "Please fill all required fields.").show();
            return false;
        }
        return true;
    }

    @FXML
    private void generateCV() {
        try {
            if (!validateForm()) return;

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

            PreviewController controller = loader.getController();
            controller.setCV(cv);

            Stage stage = (Stage) fullNameField.getScene().getWindow();
            stage.setScene(scene);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void collect(VBox box, java.util.List<String> list) {
        list.clear();
        for (Node node : box.getChildren()) {
            if (node instanceof HBox row) {
                for (Node c : row.getChildren()) {
                    if (c instanceof TextField tf && !tf.getText().isBlank())
                        list.add(tf.getText());
                    else if (c instanceof TextArea ta && !ta.getText().isBlank())
                        list.add(ta.getText());
                }
            }
        }
    }

    @FXML
    private void goBack() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CVBuilder/Home.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) fullNameField.getScene().getWindow();
        stage.setScene(scene);
    }
}
