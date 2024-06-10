package ru.otus.helpers;

import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.util.resource.PathResourceFactory;
import org.eclipse.jetty.util.resource.Resource;
import org.hibernate.cfg.Configuration;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.crm.model.mapper.ClientMapper;
import ru.otus.server.UsersWebServer;
import ru.otus.server.UsersWebServerImpl;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.TemplateProcessorImpl;

import java.net.URI;

public class WebServerHelper {
    private static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static DBServiceClient createDbServiceClient() {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        ///
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        ///
        return new DbServiceClientImpl(transactionManager, clientTemplate);
    }

    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    private static final String REALM_NAME = "AnyRealm";
    private static final String HASH_LOGIN_SERVICE_CONFIG_NAME = "realm.properties";
    
    public static UsersWebServer createUsersWebServer(DBServiceClient dbServiceClient) {
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        String hashLoginServiceConfigPath =
                FileSystemHelper.localFileNameOrResourceNameToFullPath(HASH_LOGIN_SERVICE_CONFIG_NAME);
        PathResourceFactory pathResourceFactory = new PathResourceFactory();
        Resource configResource = pathResourceFactory.newResource(URI.create(hashLoginServiceConfigPath));
        LoginService loginService = new HashLoginService(REALM_NAME, configResource);

        return new UsersWebServerImpl(
                WEB_SERVER_PORT,
                loginService,
                dbServiceClient,
                new ClientMapper(),
                templateProcessor
        );
    }

}
