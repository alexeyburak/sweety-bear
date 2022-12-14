package by.bsuir.sweetybear.repository;

import by.bsuir.sweetybear.model.BankCard;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

public interface BankCardRepository extends JpaRepository<BankCard, Long> {
}
