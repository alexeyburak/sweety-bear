package by.bsuir.sweetybear.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Jan 2023
 */

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "feedbacks")
public class Feedback extends IdentifiedModel {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;
    @Column(name = "description")
    private String description;
    @Column(name = "title")
    private String title;
    @Column(name = "stars")
    private byte stars;
    @Column(name = "date_Of_Created")
    private LocalDateTime dateOfCreated;

    @PrePersist
    private void init() {
        this.dateOfCreated = LocalDateTime.now();
    }

}