package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.servlet.query.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.function.Function;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {

    private static final Map<String, Function<HttpServletResponse, Command>> commandToConstructor = Map.of(
            "max",
            MaxCommand::new,
            "min",
            MinCommand::new,
            "count",
            CountCommand::new,
            "sum",
            SumCommand::new
    );

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String commandString = request.getParameter("command");
        var constructor = commandToConstructor.get(commandString);

        if (constructor == null) {
            response.getWriter().println("Unknown command: " + commandString);
            approveResponse(response);
            return;
        }

        Command command = constructor.apply(response);
        command.run();

        approveResponse(response);
    }

    private void approveResponse(HttpServletResponse response) {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
