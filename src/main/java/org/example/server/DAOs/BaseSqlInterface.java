package org.example.server.DAOs;

import org.example.server.Exception.DaoException;

import java.util.Comparator;
import java.util.List;

public interface BaseSqlInterface<T> {
    List<T> getAllEntities() throws DaoException;

    List<T> getEntitiesByField(String field, String value) throws  DaoException;

    T getEntityById(int id) throws DaoException;

    void insertEntity(T t);

    void updateEntity(int id, T t) throws DaoException;

    void deleteEntity(int id) throws DaoException;

    List<T> findEntitiesByFilter(Comparator<T> comparator) throws DaoException;
}
