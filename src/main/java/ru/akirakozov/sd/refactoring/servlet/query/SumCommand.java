package ru.akirakozov.sd.refactoring.servlet.query;

import ru.akirakozov.sd.refactoring.db.Database;
import ru.akirakozov.sd.refactoring.html.HtmlWriter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SumCommand implements Command {

    private final HttpServletResponse response;

    public SumCommand(HttpServletResponse response) {
        this.response = response;
    }

    @Override
    public void run() throws IOException {
        Database db = new Database();
        long sum = db.getSum();

        HtmlWriter writer = new HtmlWriter(response);
        writer.writeSum(sum);
    }
}
