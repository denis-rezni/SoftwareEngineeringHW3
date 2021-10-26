package refactoring.servlet.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author akirakozov
 */
public class UrlReader {

    public String readAsText(URL url) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
            StringBuilder buffer = new StringBuilder();
            String inputLine;

            while((inputLine=in.readLine()) != null) {
                buffer.append(inputLine);
                buffer.append("\n");
            }
            return buffer.toString();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}