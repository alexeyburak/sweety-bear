package by.bsuir.sweetybear.dto;

import by.bsuir.sweetybear.model.Image;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Jan 2023
 */

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductViewingDTO {
    Long id;
    String title;
    String description;
    Double price;
    int weight;
    boolean availability;
    List<Image> images = new ArrayList<>();
    Long previewImageId;
    LocalDateTime dateOfCreated;
}
