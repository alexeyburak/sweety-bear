package by.bsuir.sweetybear.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

@MappedSuperclass
@Data
@RequiredArgsConstructor
public class IdentifiedModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;
}
