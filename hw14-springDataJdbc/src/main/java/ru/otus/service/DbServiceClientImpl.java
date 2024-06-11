package ru.otus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.model.Client;
import ru.otus.repository.ClientRepository;
import ru.otus.sessionmanager.TransactionManager;

import java.util.List;

@Service
public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final ClientRepository clientRepository;
    private final TransactionManager transactionManager;

    public DbServiceClientImpl(
            TransactionManager transactionManager,
            ClientRepository clientRepository
    ) {
        this.transactionManager = transactionManager;
        this.clientRepository = clientRepository;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(() -> {
            Client savedClient = clientRepository.save(client);
            log.info("created client: {}", savedClient);
            return savedClient;
        });
    }

    @Override
    public List<Client> findAll() {
        var clientList = clientRepository.findAll();
        log.info("clientList:{}", clientList);
        return clientList;
    }
}
