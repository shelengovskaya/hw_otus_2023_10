package ru.otus.crm.model.mapper;

import jakarta.servlet.http.HttpServletRequest;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;

import java.util.Map;

import static java.util.Arrays.stream;

import static java.util.stream.Collectors.joining;

public class ClientMapper {
    public Client toClient(HttpServletRequest request) {
        Client client = new Client();
        client.setName(request.getParameter("name"));
        client.setAddress(new Address(request.getParameter("address")));
        client.setPhones(
                stream(request.getParameter("phone").split(","))
                        .map(phone -> new Phone(phone, client))
                        .toList()
        );
        return client;
    }

    public Map<String, Object> toMap(Client client) {
        return Map.of("id", client.getId(),
                "name", client.getName(),
                "address", client.getAddress().getStreet(),
                "phones", client.getPhones()
                        .stream()
                        .map(Phone::getNumber)
                        .collect(joining(","))
        );
    }
}
