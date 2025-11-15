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

        addLiveValidation(fullNameField);
        addLiveValidation(emailField);
        addLiveValidation(phoneField);
        addLiveValidation(addressArea);
    }


    private HBox entry(String prompt, boolean large) {
        TextInputControl input = large ? new TextArea() : new TextField();
        input.setPromptText(prompt);
        input.setPrefWidth(420);

        if (large) ((TextArea) input).setPrefRowCount(3);

        addLiveValidation(input);

        Button remove = new Button("X");
        remove.setOnAction(e ->
                ((VBox) ((HBox) remove.getParent()).getParent())
                        .getChildren()
                        .remove(remove.getParent())
        );

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
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File file = chooser.showOpenDialog(uploadImageBtn.getScene().getWindow());

        if (file != null) {
            profileImageURI = file.toURI().toString();
            profileImageView.setImage(new Image(profileImageURI));
        }
    }



    private boolean validateForm() {
        StringBuilder errors = new StringBuilder();
        Node firstInvalidNode = null;


        firstInvalidNode = checkField(fullNameField, "Full Name", errors, firstInvalidNode);
        firstInvalidNode = checkField(emailField, "Email", errors, firstInvalidNode);
        firstInvalidNode = checkField(phoneField, "Phone", errors, firstInvalidNode);
        firstInvalidNode = checkField(addressArea, "Address", errors, firstInvalidNode);


        if (profileImageURI == null) {
            errors.append("• Profile Photo is required.\n");
            if (firstInvalidNode == null) firstInvalidNode = profileImageView;
        }


        firstInvalidNode = validateContainer(educationContainer, "Education", errors, firstInvalidNode);
        firstInvalidNode = validateContainer(skillsContainer, "Skills", errors, firstInvalidNode);
        firstInvalidNode = validateContainer(experienceContainer, "Experience", errors, firstInvalidNode);
        firstInvalidNode = validateContainer(projectsContainer, "Projects", errors, firstInvalidNode);


        if (!errors.toString().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Form");
            alert.setHeaderText("Please fix the following issues:");
            alert.setContentText(errors.toString());
            alert.show();

            if (firstInvalidNode != null)
                scrollTo(firstInvalidNode);

            return false;
        }

        return true;
    }

    private Node checkField(TextInputControl field, String name, StringBuilder errors, Node firstInvalidNode) {
        if (field.getText().isBlank()) {
            errors.append("• ").append(name).append(" is required.\n");
            markInvalid(field);
            return (firstInvalidNode == null ? field : firstInvalidNode);
        }
        markValid(field);
        return firstInvalidNode;
    }

    private Node validateContainer(VBox box, String name, StringBuilder errors, Node firstInvalidNode) {
        if (box.getChildren().isEmpty()) {
            errors.append("• At least one ").append(name).append(" entry is required.\n");
            return (firstInvalidNode == null ? box : firstInvalidNode);
        }
        return firstInvalidNode;
    }



    private void addLiveValidation(TextInputControl input) {
        input.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.isBlank()) markInvalid(input);
            else markValid(input);
        });
    }

    private void markInvalid(Node node) {
        node.setStyle("-fx-border-color: #ff4d4d; -fx-border-width: 2;");
    }

    private void markValid(Node node) {
        node.setStyle("");
    }

    private void scrollTo(Node node) {
        scrollPane.layout();
        double y = node.getBoundsInParent().getMinY();
        scrollPane.setVvalue(y / scrollPane.getContent().getBoundsInLocal().getHeight());
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
            scene.getStylesheets().add(getClass().getResource("/CVBuilder/style.css").toExternalForm());

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
        scene.getStylesheets().add(getClass().getResource("/CVBuilder/style.css").toExternalForm());

        Stage stage = (Stage) fullNameField.getScene().getWindow();
        stage.setScene(scene);
    }
}
