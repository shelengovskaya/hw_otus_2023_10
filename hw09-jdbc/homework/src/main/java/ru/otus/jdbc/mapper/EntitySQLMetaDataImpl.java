package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {
    private final EntityClassMetaData<T> entityClassMetaDataClient;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaDataClient) {
        this.entityClassMetaDataClient = entityClassMetaDataClient;
    }

    @Override
    public String getSelectAllSql() {
        return null;
    }

    @Override
    public String getSelectByIdSql() {
        return String.format(
                "SELECT %s FROM %s WHERE %s = ?",
                getStringAllFields(),
                entityClassMetaDataClient.getName(),
                entityClassMetaDataClient.getIdField().getName()
        );
    }

    private String getStringAllFields() {
        return entityClassMetaDataClient.getAllFields().stream().map(Field::getName).collect(Collectors.joining(", "));
    }

    private String getStringFieldsWithoutId() {
        return entityClassMetaDataClient.getFieldsWithoutId().stream().map(Field::getName).collect(Collectors.joining(", "));
    }

    private String getEmptyValuesStringFieldsWithoutId() {
        return entityClassMetaDataClient.getFieldsWithoutId().stream().map(field -> "?").collect(Collectors.joining(", "));
    }

    @Override
    public String getInsertSql() {
        return String.format(
                "INSERT INTO %s (%s) VALUES (%s)",
                entityClassMetaDataClient.getName(),
                getStringFieldsWithoutId(),
                getEmptyValuesStringFieldsWithoutId()
        );
    }

    @Override
    public String getUpdateSql() {
        return String.format(
                "UPDATE %s SET (%s) = (%s) WHERE %s = ?",
                entityClassMetaDataClient.getName(),
                getStringFieldsWithoutId(),
                getEmptyValuesStringFieldsWithoutId(),
                entityClassMetaDataClient.getIdField().getName()
        );
    }
}
