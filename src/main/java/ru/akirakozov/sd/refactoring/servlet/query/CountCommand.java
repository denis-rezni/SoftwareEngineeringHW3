package ru.akirakozov.sd.refactoring.servlet.query;

import ru.akirakozov.sd.refactoring.db.Database;
import ru.akirakozov.sd.refactoring.utils.Item;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CountCommand implements Command {

    private HttpServletResponse response;

    public CountCommand(HttpServletResponse response) {
        this.response = response;
    }

    @Override
    public void run() throws IOException {
        Database db = new Database();
        int count = db.getCount();

        response.getWriter().println("<html><body>");
        response.getWriter().println("Number of products: ");
        response.getWriter().println(count);
        response.getWriter().println("</body></html>");
    }
}
