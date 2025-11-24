package CVBuilder.db;

import CVBuilder.models.CV;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CVRepository {

    private static final ObservableList<CV> list = FXCollections.observableArrayList();
    private static final Lock LOCK = new ReentrantLock();

    public static ObservableList<CV> getList() {
        return list;
    }

    public static void add(CV cv) {
        LOCK.lock();
        try {
            list.add(cv);
        } finally {
            LOCK.unlock();
        }
    }

    public static void remove(int id) {
        LOCK.lock();
        try {
            list.removeIf(c -> c.getId() == id);
        } finally {
            LOCK.unlock();
        }
    }

    public static void update(CV cv) {
        LOCK.lock();
        try {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getId() == cv.getId()) {
                    list.set(i, cv);
                    return;
                }
            }
            // if not found, add
            list.add(cv);
        } finally {
            LOCK.unlock();
        }
    }

    public static void loadAll(java.util.List<CV> cvs) {
        LOCK.lock();
        try {
            list.setAll(cvs);
        } finally {
            LOCK.unlock();
        }
    }
}
