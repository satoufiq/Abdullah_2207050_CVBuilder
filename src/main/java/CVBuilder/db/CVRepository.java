package CVBuilder.db;

import CVBuilder.models.CV;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class CVRepository {


    private static final ObservableList<CV> list = FXCollections.observableArrayList();

    public static ObservableList<CV> getList() {
        return list;
    }


    public static void add(CV cv) {
        list.add(cv);
    }


    public static void remove(int id) {
        list.removeIf(c -> c.getId() == id);
    }

    public static void update(CV cv) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == cv.getId()) {
                list.set(i, cv);
                return;
            }
        }
    }

    public static void loadAll(java.util.List<CV> cvs) {
        list.setAll(cvs);
    }
}
