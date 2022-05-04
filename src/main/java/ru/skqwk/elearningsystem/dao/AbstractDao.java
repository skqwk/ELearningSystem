package ru.skqwk.elearningsystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AbstractDao<T> extends JpaRepository<T, Long> {
}
