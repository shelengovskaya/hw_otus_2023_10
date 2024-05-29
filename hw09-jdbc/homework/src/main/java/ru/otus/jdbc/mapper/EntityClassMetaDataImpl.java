package ru.otus.jdbc.mapper;

import ru.otus.crm.annotations.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData {
    private final Class<T> clientClass;

    public EntityClassMetaDataImpl(Class<T> clientClass) {
        this.clientClass = clientClass;
    }

    @Override
    public String getName() {
        return clientClass.getSimpleName().toLowerCase();
    }

    @Override
    public Constructor<T> getConstructor() {
        List<Object> fieldTypes = Arrays.stream(clientClass.getDeclaredFields()).map(Field::getType).collect(Collectors.toList());
        try {
            return clientClass.getDeclaredConstructor(fieldTypes.toArray(Class[]::new));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Exception while getting constructor.");
        }
    }

    @Override
    public Field getIdField() {
        return Arrays.stream(clientClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findAny()
                .orElseThrow(() -> new RuntimeException("There is no such annotation"));
    }

    @Override
    public List<Field> getAllFields() {
        return List.of(clientClass.getDeclaredFields());
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return Arrays.stream(clientClass.getDeclaredFields())
                .filter(field -> !field.isAnnotationPresent(Id.class))
                .toList();
    }
}
