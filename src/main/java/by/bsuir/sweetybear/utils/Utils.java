package by.bsuir.sweetybear.utils;

import by.bsuir.sweetybear.model.Image;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

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

    public static List<Long> remove(List<Long> list, Long id) {
        List<Long> refactorList = new ArrayList<>(list);
        int flag = 0;
        for (int i = 0; i < list.size(); i++) {
            if (Objects.equals(list.get(i), id)) {
                flag = i;
                break;
            }
        }
        refactorList.remove(flag);
        return refactorList;
    }

    private static <T> List<T> fromIteratorToList(Iterator<T> iter) {
        List<T> copy = new ArrayList<T>();
        while (iter.hasNext())
            copy.add(iter.next());
        return copy;
    }
}
