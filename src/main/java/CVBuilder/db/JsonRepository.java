package CVBuilder.db;

import CVBuilder.models.CV;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import CVBuilder.concurrent.AppExecutors;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JsonRepository {

    private static final String RESOURCE_PATH = "/CVBuilder/cvs.json";
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        // schedule periodic autosave every 30 seconds (adjust as needed)
        AppExecutors.SCHEDULER.scheduleAtFixedRate(() -> {
            try {
                // use repository list snapshot
                List<CV> snapshot = new ArrayList<>(CVRepository.getList());
                saveAll(snapshot);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 30, 30, java.util.concurrent.TimeUnit.SECONDS);
    }

    private static Path resolveJsonPath() throws Exception {

        URL url = JsonRepository.class.getResource(RESOURCE_PATH);

        if (url == null) {

            Path dev = Path.of("src/main/resources/CVBuilder/cvs.json");
            if (!Files.exists(dev)) {

                Files.createDirectories(dev.getParent());
                Files.createFile(dev);
                Files.writeString(dev, "[]");
            }
            return dev;
        }

        if ("file".equals(url.getProtocol())) {
            return Path.of(url.toURI());
        } else {

            return Path.of("cvs.json");
        }
    }

    public static List<CV> loadAll() {
        try {
            Path path = resolveJsonPath();
            if (!Files.exists(path)) return new ArrayList<>();
            return mapper.readValue(path.toFile(), new TypeReference<List<CV>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void saveAll(List<CV> list) {
        try {
            Path path = resolveJsonPath();
            mapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
