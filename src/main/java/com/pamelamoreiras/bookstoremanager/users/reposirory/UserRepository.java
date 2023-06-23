package com.pamelamoreiras.bookstoremanager.users.reposirory;

import com.pamelamoreiras.bookstoremanager.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
