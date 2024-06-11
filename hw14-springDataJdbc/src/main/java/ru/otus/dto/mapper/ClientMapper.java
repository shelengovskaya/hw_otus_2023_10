package ru.otus.dto.mapper;

import org.springframework.stereotype.Component;
import ru.otus.dto.ClientDTO;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;

import java.util.Arrays;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

@Component
public class ClientMapper {
    public Client toClient(ClientDTO clientDTO) {
        return new Client(
                clientDTO.id(),
                clientDTO.name(),
                new Address(clientDTO.address()),
                Arrays.stream(clientDTO.phones().split(", "))
                        .map(Phone::new)
                        .collect(toSet())
        );
    }

    public ClientDTO toClientDTO(Client client) {
        return new ClientDTO(
                client.getId(),
                client.getName(),
                client.getAddress().getStreet(),
                client.getPhones().stream()
                        .map(Phone::getNumber)
                        .collect(Collectors.joining(", "))
        );
    }
}
