package by.bsuir.sweetybear.utils;

import by.bsuir.sweetybear.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

public class Utils {

    public static Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }
}
