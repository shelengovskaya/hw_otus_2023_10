package ru.otus.dto;

public record ClientDTO (
    Long id,
    String name,
    String address,
    String phones
) {
}
