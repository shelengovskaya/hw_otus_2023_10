package ru.otus;

import ru.otus.crm.service.DBServiceClient;
import ru.otus.helpers.WebServerHelper;
import ru.otus.server.UsersWebServer;

/*
    Полезные для демо ссылки

    // Стартовая страница
    http://localhost:8080

    // Страница пользователей
    http://localhost:8080/users

*/
public class WebServer {
    public static void main(String[] args) throws Exception {
        DBServiceClient dbServiceClient = WebServerHelper.createDbServiceClient();
        UsersWebServer usersWebServer = WebServerHelper.createUsersWebServer(dbServiceClient);

        usersWebServer.start();
        usersWebServer.join();
    }
}
