package by.bsuir.sweetybear.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

@Entity
@Table(name = "bank_cards")
@Getter
@Setter
@ToString
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class BankCard extends IdentifiedModel {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "card_number")
    private Long cardNumber;
    @Column(name = "expiration_month")
    private Integer expirationMonth;
    @Column(name = "expiration_year")
    private Integer expirationYear;
    @Column(name = "cardholder_name")
    private String cardholderName;
    @Column(name = "balance")
    private BigDecimal balance;
    @Column(name = "cvv")
    private Integer cvv;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BankCard bankCard = (BankCard) o;
        return id != null && Objects.equals(id, bankCard.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
