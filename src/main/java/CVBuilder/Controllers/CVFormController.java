package CVBuilder.Controllers;

import CVBuilder.db.CVDao;
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
    private CV currentCV = null;

    private final CVDao cvDao = new CVDao();

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

    // ------------------- LOAD EXISTING CV ---------------------

    public void loadCV(CV cv) {
        this.currentCV = cv;

        fullNameField.setText(cv.getFullName());
        emailField.setText(cv.getEmail());
        phoneField.setText(cv.getPhone());
        addressArea.setText(cv.getAddress());
        profileImageURI = cv.getProfileImageURI();

        if (profileImageURI != null)
            profileImageView.setImage(new Image(profileImageURI));

        educationContainer.getChildren().clear();
        skillsContainer.getChildren().clear();
        experienceContainer.getChildren().clear();
        projectsContainer.getChildren().clear();

        for (String s : cv.getEducations())
            educationContainer.getChildren().add(entry(s, false, "Degree - Institute - Year"));

        for (String s : cv.getSkills())
            skillsContainer.getChildren().add(entry(s, false, "Skill (e.g., Java)"));

        for (String s : cv.getExperiences())
            experienceContainer.getChildren().add(entry(s, true, "Job Title - Company - Duration"));

        for (String s : cv.getProjects())
            projectsContainer.getChildren().add(entry(s, true, "Project - Description"));

        scrollPane.setVvalue(0);
    }

    // ------------------- ENTRY COMPONENT ----------------------

    private HBox entry(String text, boolean large, String prompt) {

        TextInputControl input = large ? new TextArea() : new TextField();
        input.setPrefWidth(420);

        if (text == null || text.isBlank())
            input.setPromptText(prompt);
        else
            input.setText(text);

        if (large)
            ((TextArea) input).setPrefRowCount(3);

        Button remove = new Button("X");
        remove.setOnAction(e ->
                ((VBox)((HBox) remove.getParent()).getParent())
                        .getChildren().remove(remove.getParent())
        );

        HBox box = new HBox(8, input, remove);
        box.setPadding(new Insets(4));
        return box;
    }

    // ---------------------- ADD ROWS --------------------------

    @FXML
    private void addEducation(javafx.event.ActionEvent e) {
        educationContainer.getChildren().add(entry("", false, "Degree - Institute - Year"));
    }

    @FXML
    private void addSkill(javafx.event.ActionEvent e) {
        skillsContainer.getChildren().add(entry("", false, "Skill (e.g., Java)"));
    }

    @FXML
    private void addExperience(javafx.event.ActionEvent e) {
        experienceContainer.getChildren().add(entry("", true, "Job Title - Company - Duration"));
    }

    @FXML
    private void addProject(javafx.event.ActionEvent e) {
        projectsContainer.getChildren().add(entry("", true, "Project - Description"));
    }

    // --------------------- IMAGE UPLOAD ------------------------

    @FXML
    private void uploadImage() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        File file = chooser.showOpenDialog(uploadImageBtn.getScene().getWindow());
        if (file != null) {
            profileImageURI = file.toURI().toString();
            profileImageView.setImage(new Image(profileImageURI));
        }
    }

    // --------------------- VALIDATION --------------------------

    private boolean validateForm() {
        boolean valid = true;

        valid &= validateName(fullNameField);
        valid &= validateEmail(emailField);
        valid &= validatePhone(phoneField);
        valid &= validateField(addressArea);

        valid &= validateList(educationContainer);
        valid &= validateList(skillsContainer);
        valid &= validateList(experienceContainer);
        valid &= validateList(projectsContainer);

        if (!valid)
            new Alert(Alert.AlertType.ERROR, "Please correct highlighted fields.").show();

        return valid;
    }

    private boolean validateName(TextField field) {
        boolean ok = field.getText().matches("[a-zA-Z ]+");
        field.setStyle(ok ? "" : "-fx-border-color:red; -fx-border-width:2;");
        return ok;
    }

    private boolean validateEmail(TextField field) {
        boolean ok = field.getText().contains("@") && field.getText().contains(".");
        field.setStyle(ok ? "" : "-fx-border-color:red; -fx-border-width:2;");
        return ok;
    }

    private boolean validatePhone(TextField field) {
        boolean ok = field.getText().matches("[0-9]+");
        field.setStyle(ok ? "" : "-fx-border-color:red; -fx-border-width:2;");
        return ok;
    }

    private boolean validateField(TextInputControl field) {
        boolean ok = !field.getText().isBlank();
        field.setStyle(ok ? "" : "-fx-border-color:red; -fx-border-width:2;");
        return ok;
    }

    private boolean validateList(VBox box) {
        boolean allOk = true;

        for (Node n : box.getChildren()) {
            if (n instanceof HBox hbox) {
                TextInputControl input = (TextInputControl) hbox.getChildren().get(0);
                boolean ok = !input.getText().isBlank();
                input.setStyle(ok ? "" : "-fx-border-color:red; -fx-border-width:2;");
                allOk &= ok;
            }
        }
        return allOk;
    }

    // --------------------- SAVE + PREVIEW ----------------------

    @FXML
    private void generateCV() {
        try {
            if (!validateForm()) return;

            CV cv = (currentCV == null) ? new CV() : currentCV;

            cv.setFullName(fullNameField.getText());
            cv.setEmail(emailField.getText());
            cv.setPhone(phoneField.getText());
            cv.setAddress(addressArea.getText());
            cv.setProfileImageURI(profileImageURI);

            collect(educationContainer, cv.getEducations());
            collect(skillsContainer, cv.getSkills());
            collect(experienceContainer, cv.getExperiences());
            collect(projectsContainer, cv.getProjects());

            if (cv.getId() == 0)
                cvDao.insert(cv);
            else
                cvDao.update(cv);

            new Alert(Alert.AlertType.INFORMATION, "CV saved successfully!").show();

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
        for (Node n : box.getChildren()) {
            if (n instanceof HBox row) {
                Node input = row.getChildren().get(0);
                if (input instanceof TextField tf && !tf.getText().isBlank()) list.add(tf.getText());
                if (input instanceof TextArea ta && !ta.getText().isBlank()) list.add(ta.getText());
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
