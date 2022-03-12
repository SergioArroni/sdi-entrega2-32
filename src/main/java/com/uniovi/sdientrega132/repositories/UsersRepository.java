package com.uniovi.sdientrega132.repositories;

import com.uniovi.sdientrega132.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UsersRepository extends CrudRepository<User, Long> {

    User findByEmail(String email);

    Page<User> findAll(Pageable pageable);

    @Query("SELECT u FROM User u WHERE u <> ?1 AND u.role = 'ROLE_USER' ORDER BY u.id ASC")
    Page<User> findAllStandard(Pageable pageable, User user);

}
