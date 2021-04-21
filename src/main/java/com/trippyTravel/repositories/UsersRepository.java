package com.trippyTravel.repositories;

import com.trippyTravel.models.Trip;
import com.trippyTravel.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface
UsersRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
    User findByEmail(String email);

    //Same function of above but with HQL
    @Query("select u from User u where u.email = ?1")
    User findByEmailQuery(String email);

    List<User> findByFirstNameContainingOrLastNameContainingOrUsernameContaining(String name, String name1, String name2);





}
