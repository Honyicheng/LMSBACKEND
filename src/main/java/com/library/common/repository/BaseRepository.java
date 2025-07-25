package com.library.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID> {
    default T findByIdOrThrow(ID id) {
        return findById(id).orElseThrow(() ->
                new RuntimeException("Entity not found with id: " + id));
    }
}