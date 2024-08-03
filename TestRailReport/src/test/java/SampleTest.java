
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SampleTest {

    @Test
    public void testDemo(){
        try (PrintWriter writer = new PrintWriter(new FileWriter("test-report.html"))) {
            writer.println("<html><body><h1>Test Cases Report</h1><ul>");
                writer.printf("<li>Case ID: %d, Title: %s</li>%n", 12345, "login test");
            writer.println("</ul></body></html>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
