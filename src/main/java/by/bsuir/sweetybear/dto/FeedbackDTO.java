package by.bsuir.sweetybear.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Jan 2023
 */

@Data
@NoArgsConstructor
public class FeedbackDTO {

    private String description;
    private String title;
    private byte stars;
}
