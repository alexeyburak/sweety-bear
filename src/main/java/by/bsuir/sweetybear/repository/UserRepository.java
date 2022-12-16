package by.bsuir.sweetybear.repository;

import by.bsuir.sweetybear.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    @Modifying
    @Query(value = "SELECT * FROM users WHERE email LIKE ?1" + "%", nativeQuery = true)
    List<User> findAllByEmail(String email);
    User findByActivationCode(String code);
    User findByResetPasswordCode(String code);
}
