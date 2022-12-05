package by.bsuir.sweetybear.service;

import javax.servlet.http.HttpServletResponse;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

public interface PDFGeneratorService {

    void exportUsersInPDF(HttpServletResponse response);
}
