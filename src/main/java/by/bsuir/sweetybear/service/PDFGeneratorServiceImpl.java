package by.bsuir.sweetybear.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

@Service
@Slf4j
public class PDFGeneratorServiceImpl implements PDFGeneratorService {

    @Override
    public void exportUsersInPDF(HttpServletResponse response) {

        try (Document document = new Document(PageSize.A4)) {
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE);
            fontTitle.setSize(18);

            Paragraph paragraph = new Paragraph("Sweety Bear\nUsers report", fontTitle);
            paragraph.setAlignment(Paragraph.ALIGN_CENTER);

            document.add(paragraph);

        } catch (IOException e) {
            log.error("Error exporting users. {}", e.getMessage());
            e.printStackTrace();
        }
    }
}
