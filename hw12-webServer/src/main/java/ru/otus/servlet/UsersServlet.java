package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ru.otus.crm.service.DBServiceClient;
import ru.otus.crm.model.mapper.ClientMapper;
import ru.otus.services.TemplateProcessor;

@SuppressWarnings({"squid:S1948"})
public class UsersServlet extends HttpServlet {

    private static final String USERS_PAGE_TEMPLATE = "users.html";
    private static final String TEMPLATE_ATTR_USERS = "users";

    private final DBServiceClient dbServiceClient;
    private final TemplateProcessor templateProcessor;
    private final ClientMapper clientMapper;

    public UsersServlet(
            TemplateProcessor templateProcessor,
            DBServiceClient dbServiceClient,
            ClientMapper clientMapper
    ) {
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
        this.clientMapper = clientMapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();

        paramsMap.put(TEMPLATE_ATTR_USERS, dbServiceClient.findAll()
                .stream()
                .map(clientMapper::toMap)
                .toList());

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, paramsMap));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse response) throws IOException {
        dbServiceClient.saveClient(clientMapper.toClient(req));
        response.sendRedirect(req.getServletPath());
    }
}
