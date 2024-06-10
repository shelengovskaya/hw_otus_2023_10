package ru.otus.crm.service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.crm.model.Client;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> clientDataTemplate;
    private final TransactionManager transactionManager;
    private final HwCache<Long, Client> cache = new MyCache<>();;


    public DbServiceClientImpl(TransactionManager transactionManager, DataTemplate<Client> clientDataTemplate) {
        this.transactionManager = transactionManager;
        this.clientDataTemplate = clientDataTemplate;
    }

    @Override
    public Client saveClient(Client client) {
        Client newClient = transactionManager.doInTransaction(session -> {
            var clientCloned = client.clone();
            if (client.getId() == null) {
                var savedClient = clientDataTemplate.insert(session, clientCloned);
                log.info("created client: {}", clientCloned);
                return savedClient;
            }
            var savedClient = clientDataTemplate.update(session, clientCloned);
            log.info("updated client: {}", savedClient);
            return savedClient;
        });

        cache.put(client.getId(), newClient);
        log.info("put client to cache: {}", client);

        return newClient;
    }

    @Override
    public Optional<Client> getClient(long id) {
        Client client = cache.get(id);
        if (client != null) {
            log.info("get client from cache: {}", client);
            return Optional.of(client);
        }

        Optional<Client> newClient = transactionManager.doInReadOnlyTransaction(session -> {
            var clientOptional = clientDataTemplate.findById(session, id);
            log.info("client: {}", clientOptional);
            return clientOptional;
        });

        if (newClient.isPresent()) {
            cache.put(newClient.get().getId(), newClient.get());
            log.info("put client to cache: {}", newClient.get());
        }
        return newClient;
    }

    @Override
    public List<Client> findAll() {
        List<Client> clients = transactionManager.doInReadOnlyTransaction(session -> {
            var clientList = clientDataTemplate.findAll(session);
            log.info("clientList:{}", clientList);
            return clientList;
        });

        clients.forEach(client -> {
            cache.put(client.getId(), client);
            log.info("put client to cache: {}", client);
        });

        return clients;
    }
}
