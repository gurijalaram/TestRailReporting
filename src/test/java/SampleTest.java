
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SampleTest {

    @Test
    public void testDemo() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("test-report.html"))) {
            writer.println("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Pretty HTML Table</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            margin: 20px;\n" +
                "            background-color: #f4f4f4;\n" +
                "        }\n" +
                "\n" +
                "        table {\n" +
                "            width: 100%;\n" +
                "            border-collapse: collapse;\n" +
                "            margin: 20px 0;\n" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "\n" +
                "        th, td {\n" +
                "            padding: 12px;\n" +
                "            text-align: left;\n" +
                "            border-bottom: 1px solid #ddd;\n" +
                "        }\n" +
                "\n" +
                "        th {\n" +
                "            background-color: #4CAF50;\n" +
                "            color: white;\n" +
                "            font-weight: bold;\n" +
                "        }\n" +
                "\n" +
                "        tr:nth-child(even) {\n" +
                "            background-color: #f2f2f2;\n" +
                "        }\n" +
                "\n" +
                "        tr:hover {\n" +
                "            background-color: #ddd;\n" +
                "        }\n" +
                "\n" +
                "        caption {\n" +
                "            font-size: 1.5em;\n" +
                "            margin: 10px 0;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <table>\n" +
                "        <caption>Sample Table</caption>\n" +
                "        <thead>\n" +
                "            <tr>\n" +
                "                <th>Header 1</th>\n" +
                "                <th>Header 2</th>\n" +
                "                <th>Header 3</th>\n" +
                "                <th>Header 4</th>\n" +
                "            </tr>\n" +
                "        </thead>\n" +
                "        <tbody>\n" +
                "            <tr>\n" +
                "                <td>Row 1, Cell 1</td>\n" +
                "                <td>Row 1, Cell 2</td>\n" +
                "                <td>Row 1, Cell 3</td>\n" +
                "                <td>Row 1, Cell 4</td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "                <td>Row 2, Cell 1</td>\n" +
                "                <td>Row 2, Cell 2</td>\n" +
                "                <td>Row 2, Cell 3</td>\n" +
                "                <td>Row 2, Cell 4</td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "                <td>Row 3, Cell 1</td>\n" +
                "                <td>Row 3, Cell 2</td>\n" +
                "                <td>Row 3, Cell 3</td>\n" +
                "                <td>Row 3, Cell 4</td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "                <td>Row 4, Cell 1</td>\n" +
                "                <td>Row 4, Cell 2</td>\n" +
                "                <td>Row 4, Cell 3</td>\n" +
                "                <td>Row 4, Cell 4</td>\n" +
                "            </tr>\n" +
                "        </tbody>\n" +
                "    </table>\n" +
                "</body>\n" +
                "</html>\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
