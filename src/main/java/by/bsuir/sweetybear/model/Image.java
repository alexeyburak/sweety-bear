package by.bsuir.sweetybear.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

@Entity
@Table(name = "images")
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Image extends IdentifiedModel {
    @Column(name = "name")
    String name;
    @Column(name = "original_File_Name")
    String originalFileName;
    @Column(name = "size")
    Long size;
    @Column(name = "content_Type")
    String contentType;
    @Column(name = "is_Preview_Image")
    boolean isPreviewImage;
    @Lob
    byte[] bytes;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    Product product;
    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    User user;

}
