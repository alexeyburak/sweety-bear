package by.bsuir.sweetybear.repository;

import by.bsuir.sweetybear.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
