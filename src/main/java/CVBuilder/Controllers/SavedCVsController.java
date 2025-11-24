package CVBuilder.Controllers;

import CVBuilder.db.CVDao;
import CVBuilder.models.CV;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.List;

public class SavedCVsController {

    @FXML private TableView<CV> table;
    @FXML private TableColumn<CV, Integer> idCol;
    @FXML private TableColumn<CV, String> nameCol;
    @FXML private TableColumn<CV, String> emailCol;
    @FXML private TableColumn<CV, String> phoneCol;
    @FXML private TableColumn<CV, Void> actionsCol;

    private final CVDao dao = new CVDao();
    private final ObservableList<CV> items = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));

        // ACTION BUTTONS STYLING
        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button viewBtn = new Button("View");
            private final Button editBtn = new Button("Edit");
            private final Button delBtn = new Button("Delete");
            private final HBox box = new HBox(8, viewBtn, editBtn, delBtn);

            {
                // Button widths
                viewBtn.setPrefWidth(55);
                editBtn.setPrefWidth(55);
                delBtn.setPrefWidth(60);

                // Styling
                viewBtn.setStyle("-fx-background-color:#3b7bff; -fx-text-fill:white; -fx-background-radius:6;");
                editBtn.setStyle("-fx-background-color:#28a745; -fx-text-fill:white; -fx-background-radius:6;");
                delBtn.setStyle("-fx-background-color:#dc3545; -fx-text-fill:white; -fx-background-radius:6;");

                // Hover effects
                viewBtn.setOnMouseEntered(e -> viewBtn.setStyle("-fx-background-color:#1b63e0; -fx-text-fill:white; -fx-background-radius:6;"));
                viewBtn.setOnMouseExited(e -> viewBtn.setStyle("-fx-background-color:#3b7bff; -fx-text-fill:white; -fx-background-radius:6;"));

                editBtn.setOnMouseEntered(e -> editBtn.setStyle("-fx-background-color:#1f8a39; -fx-text-fill:white; -fx-background-radius:6;"));
                editBtn.setOnMouseExited(e -> editBtn.setStyle("-fx-background-color:#28a745; -fx-text-fill:white; -fx-background-radius:6;"));

                delBtn.setOnMouseEntered(e -> delBtn.setStyle("-fx-background-color:#b92c36; -fx-text-fill:white; -fx-background-radius:6;"));
                delBtn.setOnMouseExited(e -> delBtn.setStyle("-fx-background-color:#dc3545; -fx-text-fill:white; -fx-background-radius:6;"));

                // Actions
                viewBtn.setOnAction(e -> viewCV(getCurrent()));
                editBtn.setOnAction(e -> editCV(getCurrent()));
                delBtn.setOnAction(e -> deleteCV(getCurrent()));
            }

            private CV getCurrent() {
                return getTableView().getItems().get(getIndex());
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });

        table.setItems(items);
        refresh();
    }

    private void refresh() {
        try {
            List<CV> list = dao.findAll();
            items.setAll(list);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load saved CVs: " + e.getMessage()).show();
        }
    }

    @FXML
    private void openNewForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CVBuilder/CVForm.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) table.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void viewCV(CV cv) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CVBuilder/Preview.fxml"));
            Scene scene = new Scene(loader.load());
            PreviewController ctrl = loader.getController();
            ctrl.setCV(cv);
            Stage stage = (Stage) table.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void editCV(CV cv) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CVBuilder/CVForm.fxml"));
            Scene scene = new Scene(loader.load());
            CVFormController ctrl = loader.getController();
            ctrl.loadCV(cv);
            Stage stage = (Stage) table.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void deleteCV(CV cv) {
        if (cv == null) return;

        Alert a = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete CV for " + cv.getFullName() + "?",
                ButtonType.YES, ButtonType.NO);

        a.setTitle("Confirm Delete");

        a.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                try {
                    dao.delete(cv.getId());
                    refresh();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
