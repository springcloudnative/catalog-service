package com.polarbookshop.catalogservice.infrastructure.repository;

import com.polarbookshop.catalogservice.infrastructure.entity.BookEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface BookRepository extends CrudRepository<BookEntity, Long> {

    Optional<BookEntity> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    @Modifying
    @Transactional
    @Query("delete from books where isbn = :isbn")
    void deleteByIsbn(String isbn);
}
