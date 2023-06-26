package com.pamelamoreiras.bookstoremanager.author.repository;

import com.pamelamoreiras.bookstoremanager.author.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

}