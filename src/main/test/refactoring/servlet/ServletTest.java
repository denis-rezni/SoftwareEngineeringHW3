package refactoring.servlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import refactoring.servlet.utils.UrlReader;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;
import ru.akirakozov.sd.refactoring.servlet.QueryServlet;
import ru.akirakozov.sd.refactoring.url.Config;
import ru.akirakozov.sd.refactoring.url.Parameter;
import ru.akirakozov.sd.refactoring.url.UrlBuilder;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ServletTest {

    private UrlReader reader = new UrlReader();
    private UrlBuilder builder = new UrlBuilder();
    private Server server;

    private final String HOST = "localhost";
    private final int PORT = 8081;

    @Before
    public void init() throws Exception {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " PRICE          INT     NOT NULL)";
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        }


        server = new Server(PORT);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        context.addServlet(new ServletHolder(new AddProductServlet()), "/add-product");
        context.addServlet(new ServletHolder(new GetProductsServlet()),"/get-products");
        context.addServlet(new ServletHolder(new QueryServlet()),"/query");

        server.start();
    }

    @After
    public void close() throws SQLException {

        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "DROP TABLE PRODUCT";
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        }

        try {
            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void simpleAddAndGet() {
        addProduct("cat", 10);

        String getResponse = reader.readAsText(getGetUrl()).trim();
        String expected =
        """
        <html><body>
        cat\t10</br>
        </body></html>""";
        Assert.assertEquals(expected, getResponse);
    }

    @Test
    public void addWorksAtomically() {
        addProduct("cat", 10);

        String getResponse = reader.readAsText(getGetUrl()).trim();
        String expected =
                """
                <html><body>
                cat\t10</br>
                </body></html>""";
        Assert.assertEquals(expected, getResponse);

        addProduct("dog", 20);

        getResponse = reader.readAsText(getGetUrl()).trim();
        expected =
                """
                <html><body>
                cat\t10</br>
                dog\t20</br>
                </body></html>""";
        Assert.assertEquals(expected, getResponse);
    }

    @Test
    public void sameNames() {
        addProduct("cat", 10);
        addProduct("dog", 20);
        addProduct("cat", 30);
        addProduct("dog", 40);

        String getResponse = reader.readAsText(getGetUrl()).trim();
        String expected =
                """
                <html><body>
                cat\t10</br>
                dog\t20</br>
                cat\t30</br>
                dog\t40</br>
                </body></html>""";
        Assert.assertEquals(expected, getResponse);
    }

    @Test
    public void simpleMaxTest() {
        addProduct("dog", 20);
        addProduct("cat", 10);
        addProduct("frog", 40);
        addProduct("fish", 30);

        String getResponse = reader.readAsText(getQueryUrl("max")).trim();
        String expected =
                """
                        <html><body>
                        <h1>Product with max price: </h1>
                        frog\t40</br>
                        </body></html>""";
        Assert.assertEquals(expected, getResponse);
    }

    @Test
    public void maxChangesAfterAdd() {
        addProduct("dog", 20);
        addProduct("cat", 10);

        String getResponse = reader.readAsText(getQueryUrl("max")).trim();
        String expected =
                """
                        <html><body>
                        <h1>Product with max price: </h1>
                        dog\t20</br>
                        </body></html>""";
        Assert.assertEquals(expected, getResponse);

        addProduct("fish", 30);
        addProduct("frog", 40);

        getResponse = reader.readAsText(getQueryUrl("max")).trim();
        expected =
                """
                        <html><body>
                        <h1>Product with max price: </h1>
                        frog\t40</br>
                        </body></html>""";
        Assert.assertEquals(expected, getResponse);
    }


    @Test
    public void simpleMinTest() {
        addProduct("frog", 40);
        addProduct("cat", 10);
        addProduct("fish", 30);
        addProduct("dog", 20);

        String getResponse = reader.readAsText(getQueryUrl("min")).trim();
        String expected =
                """
                        <html><body>
                        <h1>Product with min price: </h1>
                        cat\t10</br>
                        </body></html>""";
        Assert.assertEquals(expected, getResponse);
    }

    @Test
    public void minChangesAfterAdd() {
        addProduct("dog", 20);
        addProduct("fish", 30);

        String getResponse = reader.readAsText(getQueryUrl("min")).trim();
        String expected =
                """
                        <html><body>
                        <h1>Product with min price: </h1>
                        dog\t20</br>
                        </body></html>""";
        Assert.assertEquals(expected, getResponse);

        addProduct("frog", 40);
        addProduct("cat", 10);

        getResponse = reader.readAsText(getQueryUrl("min")).trim();
        expected =
                """
                        <html><body>
                        <h1>Product with min price: </h1>
                        cat\t10</br>
                        </body></html>""";
        Assert.assertEquals(expected, getResponse);
    }

    @Test
    public void simpleSumTest() {
        addProduct("dog", 20);
        addProduct("cat", 10);
        addProduct("frog", 40);
        addProduct("fish", 30);

        String getResponse = reader.readAsText(getQueryUrl("sum")).trim();
        String expected =
                """
                        <html><body>
                        Summary price:\040
                        100
                        </body></html>""";
        Assert.assertEquals(expected, getResponse);
    }

    @Test
    public void simpleCountTest() {
        addProduct("dog", 20);
        addProduct("cat", 10);
        addProduct("frog", 40);
        addProduct("fish", 30);

        String getResponse = reader.readAsText(getQueryUrl("count")).trim();
        String expected =
                """
                        <html><body>
                        Number of products:\040
                        4
                        </body></html>""";
        Assert.assertEquals(expected, getResponse);
    }

    // todo add test checking that queries don't change anything


    private void addProduct(String name, int price) {
        URL url = getAddUrl(name, String.valueOf(price));
        String addResponse = reader.readAsText(url).trim();
        Assert.assertEquals("OK", addResponse);
    }

    private URL getGetUrl() {
        Config config = new Config(HOST, PORT, "get-products");
        return builder.buildUrl(config);
    }

    private URL getAddUrl(String name, String price) {
        Config config = new Config(HOST, PORT, "add-product");
        return builder.buildUrl(config, List.of(new Parameter("name", name), new Parameter("price", price)));
    }

    private URL getQueryUrl(String command) {
        Config config = new Config(HOST, PORT, "query");
        return builder.buildUrl(config, List.of(new Parameter("command", command)));
    }
}
