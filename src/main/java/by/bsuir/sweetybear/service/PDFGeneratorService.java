package by.bsuir.sweetybear.service;

import javax.servlet.http.HttpServletResponse;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

public interface PDFGeneratorService {

    /**
     * <p>Export user list in PDF</p>
     * @param response HttpServletResponse
     * @since 1.0
     */
    void exportUsersInPDF(HttpServletResponse response);
    /**
     * <p>Export user orders in PDF</p>
     * @param response HttpServletResponse
     * @param orderId Order id to export
     * @since 1.0
     */
    void exportUserOrderInPDF(HttpServletResponse response, Long orderId);
}
