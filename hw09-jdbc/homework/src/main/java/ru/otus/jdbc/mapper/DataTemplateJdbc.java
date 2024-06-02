package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.executor.DbExecutor;

/** Сохратяет объект в базу, читает объект из базы */
@SuppressWarnings("java:S1068")
public class DataTemplateJdbc<T> implements DataTemplate<T> {
    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(
            DbExecutor dbExecutor,
            EntitySQLMetaData entitySQLMetaData,
            EntityClassMetaData<T> entityClassMetaData
    ) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        String selectSql = entitySQLMetaData.getSelectByIdSql();
        return dbExecutor.executeSelect(connection, selectSql, List.of(id), rs -> {
            try {
                return rsToEntity(rs);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException | SQLException e) {
                throw new UnsupportedOperationException();
            }
        });
    }

    private T rsToEntity(ResultSet rs) throws InvocationTargetException, InstantiationException, IllegalAccessException, SQLException {
        if (!rs.next()) {
            return null;
        }
        Object[] args = new Object[entityClassMetaData.getAllFields().size()];
        for (int i = 0; i < args.length; i++) {
            args[i] = rs.getObject(entityClassMetaData.getAllFields().get(i).getName());
        }
        return entityClassMetaData.getConstructor().newInstance(args);
    }

    @Override
    public List<T> findAll(Connection connection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long insert(Connection connection, T client) {
        String insertSql = entitySQLMetaData.getInsertSql();
        try {
            List<Object> params = getParamsWithoutId(client);
            return dbExecutor.executeStatement(connection, insertSql, params);
        } catch (IllegalAccessException e) {
            throw new UnsupportedOperationException();
        }
    }

    private List<Object> getParamsWithoutId(T obj) throws IllegalAccessException {
        List<Object> params = new ArrayList<>();
        for (Field field : entityClassMetaData.getFieldsWithoutId()) {
            field.setAccessible(true);
            params.add(field.get(obj));
            field.setAccessible(false);
        }
        return params;
    }

    private List<Object> getAllParams(T obj) throws IllegalAccessException {
        List<Object> params = new ArrayList<>();
        for (Field field : entityClassMetaData.getAllFields()) {
            field.setAccessible(true);
            params.add(field.get(obj));
            field.setAccessible(false);
        }
        return params;
    }

    @Override
    public void update(Connection connection, T client) {
        String updateSql = entitySQLMetaData.getUpdateSql();
        try {
            dbExecutor.executeStatement(connection, updateSql, getAllParams(client));
        } catch (IllegalAccessException e) {
            throw new UnsupportedOperationException();
        }
    }
}
