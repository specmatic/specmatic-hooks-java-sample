import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class MainTest {
    private static final PrintStream originalOut = System.out;
    private static ByteArrayOutputStream byteArrayOutputStream;

    @BeforeAll
    public static void setup() {
        System.setProperty("CONTRACT_FILE", "src/test/resources/spec.yaml");
        byteArrayOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(byteArrayOutputStream));
    }

    @AfterAll
    public static void tearDown() {
        System.clearProperty("CONTRACT_FILE");
        System.setOut(originalOut);
    }

    @Test
    public void shouldAddRateLimitHeaders() {
        Main.main(new String[]{});

        String output = byteArrayOutputStream.toString();
        assert output.contains("X-RateLimit-Limit");
        assert output.contains("X-RateLimit-Remaining");
    }
}
