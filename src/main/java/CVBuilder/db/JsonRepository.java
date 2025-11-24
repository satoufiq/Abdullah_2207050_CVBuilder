package CVBuilder.db;

import CVBuilder.models.CV;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JsonRepository {

    private static final Path JSON_PATH =
            Path.of("cvs.json");

    private static final ObjectMapper mapper = new ObjectMapper();

    private static void ensureFileExists() throws Exception {
        if (!Files.exists(JSON_PATH)) {
            Files.createFile(JSON_PATH);
            mapper.writeValue(JSON_PATH.toFile(), new ArrayList<CV>());
        }
    }

    public static List<CV> loadAll() {
        try {
            ensureFileExists();
            return mapper.readValue(JSON_PATH.toFile(), new TypeReference<List<CV>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void saveAll(List<CV> list) {
        try {
            ensureFileExists();
            mapper.writerWithDefaultPrettyPrinter().writeValue(JSON_PATH.toFile(), list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
