package ru.akirakozov.sd.refactoring.html;

import ru.akirakozov.sd.refactoring.utils.Item;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class HtmlWriter {

    private final HttpServletResponse response;

    private final String OPENING_TAGS = "<html><body>";
    private final String CLOSING_TAGS = "</body></html>";

    public HtmlWriter(HttpServletResponse response) {
        this.response = response;
    }

    public void writeGet(List<Item> items) throws IOException {
        response.getWriter().println(OPENING_TAGS);
        for (Item item : items) {
            response.getWriter().println(item.getName() + "\t" + item.getPrice() + "</br>");
        }
        response.getWriter().println(CLOSING_TAGS);
    }

    public void writeMin(Item minItem) throws IOException {
        response.getWriter().println(OPENING_TAGS);
        response.getWriter().println("<h1>Product with min price: </h1>");
        response.getWriter().println(minItem.getName() + "\t" + minItem.getPrice() + "</br>");
        response.getWriter().println(CLOSING_TAGS);
    }

    public void writeSum(long sum) throws IOException {
        response.getWriter().println(OPENING_TAGS);
        response.getWriter().println("Summary price: ");
        response.getWriter().println(sum);
        response.getWriter().println(CLOSING_TAGS);
    }

    public void writeMax(Item maxItem) throws IOException {
        response.getWriter().println(OPENING_TAGS);
        response.getWriter().println("<h1>Product with max price: </h1>");
        response.getWriter().println(maxItem.getName() + "\t" + maxItem.getPrice() + "</br>");
        response.getWriter().println(CLOSING_TAGS);
    }
}
