import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.DumperOptions;

public class Main {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        String fileName = System.getenv("CONTRACT_FILE") != null ? System.getenv("CONTRACT_FILE") : System.getProperty("CONTRACT_FILE");

        if (fileName == null || fileName.isEmpty()) {
            System.out.println("CONTRACT_FILE environment variable not set.");
            System.exit(1);
        }

        Yaml yaml = new Yaml();

        try (FileInputStream inputStream = new FileInputStream(fileName)) {
            Map<String, Object> data = yaml.load(inputStream);

            Map<String, Object> paths = (Map<String, Object>) data.get("paths");
            Map<String, Object> productsPath = paths != null ? (Map<String, Object>) paths.get("/products") : null;
            Map<String, Object> getOperation = productsPath != null ? (Map<String, Object>) productsPath.get("get") : null;
            List<Map<String, Object>> parameters = getOperation != null ? (List<Map<String, Object>>) getOperation.get("parameters") : null;

            if (parameters != null) {
                for (Map<String, Object> param : parameters) {
                    if ("header".equals(param.get("in")) && "X-auth-token".equals(param.get("name"))) {
                        param.put("name", "X-internal-id");
                        break;
                    }
                }

                Map<String, Object> rateLimitLimit = Map.of(
                        "name", "X-RateLimit-Limit",
                        "in", "header",
                        "required", true,
                        "schema", Map.of("type", "integer", "example", 100),
                        "description", "Maximum number of requests allowed"
                );

                Map<String, Object> rateLimitRemaining = Map.of(
                        "name", "X-RateLimit-Remaining",
                        "in", "header",
                        "required", true,
                        "schema", Map.of("type", "integer", "example", 40),
                        "description", "Number of remaining requests allowed"
                );

                parameters.add(rateLimitLimit);
                parameters.add(rateLimitRemaining);
            }

            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml outputYaml = new Yaml(options);
            System.out.println(outputYaml.dump(data));

        } catch (FileNotFoundException e) {
            System.out.printf("File not found: %s%n", fileName);
            System.exit(2);
        } catch (IOException e) {
            System.out.printf("Error processing file: %s%n", e.getMessage());
            System.exit(3);
        }
    }
}
