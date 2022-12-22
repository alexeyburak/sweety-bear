package by.bsuir.sweetybear.utils;

import by.bsuir.sweetybear.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    public static List<Long> removeOnlyOneIdFromList(List<Long> list, Long id) {
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

    public static String getCurrentDateTime() {
        return new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss").format(new Date());
    }

    public static String getYearMonthDayHourMinuteSecond() {
        return LocalDateTime.now().getYear() + "" + LocalDateTime.now().getMonthValue() + "" + LocalDateTime.now().getDayOfMonth() + "" +
                LocalDateTime.now().getHour() + "" + LocalDateTime.now().getMinute() + "" + LocalDateTime.now().getSecond();
    }

}
