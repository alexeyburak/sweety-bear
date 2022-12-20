package by.bsuir.sweetybear.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@MappedSuperclass
@Data
@RequiredArgsConstructor
public class IdentifiedModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;
}
