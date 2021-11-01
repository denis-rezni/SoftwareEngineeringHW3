package ru.akirakozov.sd.refactoring.servlet.query;

import ru.akirakozov.sd.refactoring.db.Database;
import ru.akirakozov.sd.refactoring.utils.Item;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MinCommand implements Command {

    private final HttpServletResponse response;

    public MinCommand(HttpServletResponse response) {
        this.response = response;
    }

    @Override
    public void run() throws IOException {
        Database db = new Database();
        Item minItem = db.getMinItem();

        response.getWriter().println("<html><body>");
        response.getWriter().println("<h1>Product with min price: </h1>");
        response.getWriter().println(minItem.getName() + "\t" + minItem.getPrice() + "</br>");
        response.getWriter().println("</body></html>");
    }
}
