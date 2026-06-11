package ExpenseTracker;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DBConfig {
    private static final String CONFIG_LOCATION = "/ExpenseTracker/appsettings.json";
    private static final String FILE_FALLBACK = "src/ExpenseTracker/appsettings.json";

    private final String url;
    private final String user;
    private final String password;

    private DBConfig(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public static DBConfig load() {
        String json = loadJsonConfig();

        String url = parseJsonValue(json, "url");
        String user = parseJsonValue(json, "user");
        String password = parseJsonValue(json, "password");

        if (url == null || url.isBlank()) {
            throw new IllegalStateException("Property db.url is required in " + CONFIG_LOCATION);
        }

        return new DBConfig(url.trim(), user == null ? "" : user.trim(), password == null ? "" : password.trim());
    }

    private static String loadJsonConfig() {
        try (InputStream input = DBConfig.class.getResourceAsStream(CONFIG_LOCATION)) {
            if (input != null) {
                return new String(input.readAllBytes(), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load database configuration from classpath", e);
        }

        try {
            Path fallback = Path.of(System.getProperty("user.dir")).resolve(FILE_FALLBACK);
            if (Files.exists(fallback)) {
                return Files.readString(fallback, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load database configuration from fallback path", e);
        }

        throw new IllegalStateException("Database configuration file not found: " + CONFIG_LOCATION + " or " + FILE_FALLBACK);
    }

    private static String parseJsonValue(String json, String fieldName) {
        Pattern pattern = Pattern.compile("\"" + Pattern.quote(fieldName) + "\"\\s*:\\s*\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
