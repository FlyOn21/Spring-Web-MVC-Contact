package org.example.app.repository;

import org.example.app.domain.entity.base_entity.BaseEntity;
import org.example.app.utils.utils_obj.QueryResult;


public interface IRepository<T extends BaseEntity> {
    QueryResult<T> create(T obj);
    QueryResult<T> readAll();
    QueryResult<T> update(T obj);
    QueryResult<T> delete(Long id);
    QueryResult<T> readById(Long id);
}
