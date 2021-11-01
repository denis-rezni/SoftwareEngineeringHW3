package ru.akirakozov.sd.refactoring.servlet.query;

import ru.akirakozov.sd.refactoring.db.Database;
import ru.akirakozov.sd.refactoring.html.HtmlWriter;
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

        HtmlWriter writer = new HtmlWriter(response);
        writer.writeMax(maxItem);
    }
}
