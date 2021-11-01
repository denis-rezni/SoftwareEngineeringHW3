package ru.akirakozov.sd.refactoring.servlet.query;

import ru.akirakozov.sd.refactoring.db.Database;
import ru.akirakozov.sd.refactoring.utils.Item;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MaxCommand implements Command {

    private final HttpServletResponse response;

    public MaxCommand(HttpServletResponse response) {
        this.response = response;
    }

    @Override
    public void run() throws IOException {
        Database db = new Database();
        Item maxItem = db.getMaxItem();

        response.getWriter().println("<html><body>");
        response.getWriter().println("<h1>Product with max price: </h1>");
        response.getWriter().println(maxItem.getName() + "\t" + maxItem.getPrice() + "</br>");
        response.getWriter().println("</body></html>");
    }
}
