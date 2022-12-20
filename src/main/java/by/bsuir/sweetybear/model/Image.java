package by.bsuir.sweetybear.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "images")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image extends IdentifiedModel {

    @Column(name = "name")
    private String name;
    @Column(name = "original_File_Name")
    private String originalFileName;
    @Column(name = "size")
    private Long size;
    @Column(name = "content_Type")
    private String contentType;
    @Column(name = "is_Preview_Image")
    private boolean isPreviewImage;
    @Lob
    private byte[] bytes;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private Product product;
    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private User user;

}
