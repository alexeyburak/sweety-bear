package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.model.Order;
import by.bsuir.sweetybear.model.OrderDetails;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.alignment.HorizontalAlignment;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class PDFGeneratorServiceImpl implements PDFGeneratorService {

    private final OrderServiceImpl orderService;

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

    @Override
    public void exportUserOrderInPDF(HttpServletResponse response, Long orderId) {

        Order order = orderService.getOrderById(orderId);

        try (Document document = new Document(PageSize.A4)) {
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE);
            fontTitle.setSize(20);
            Paragraph title = new Paragraph("'Sweety Bear' Order report", fontTitle);
            title.setAlignment(Paragraph.ALIGN_RIGHT);

            Font fontBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            fontBold.setSize(17);
            Paragraph orderSum = new Paragraph(order.getSum() + " BYN", fontBold);

            Font fontParagraph = FontFactory.getFont(FontFactory.TIMES_ROMAN);
            fontParagraph.setSize(15);
            Paragraph orderAddress = new Paragraph(order.getDelivery() + "\n" + order.getAddress(), fontParagraph);
            Paragraph userDescription = new Paragraph(order.getUser().getEmail(), fontParagraph);
            Paragraph dateDescription = new Paragraph("Order date: " + order.getDateOfCreated() + "\nDelivery date: " + order.getDateOfDelivery(), fontParagraph);

            Table table = new Table(3);
            table.setHorizontalAlignment(HorizontalAlignment.CENTER);
            table.setPadding(2f);
            table.addCell("Product");
            table.addCell("Amount");
            table.addCell("Price");
            table.setWidth(100f);
            table.setBackgroundColor(Color.LIGHT_GRAY);
            for (OrderDetails details : order.getDetails()) {
                table.addCell(details.getProduct().getTitle());
                table.addCell(String.valueOf(details.getAmount()));
                table.addCell(String.valueOf(details.getPrice()));
            }
            document.add(title);
            document.add(orderSum);
            document.add(orderAddress);
            document.add(userDescription);
            document.add(dateDescription);
            document.add(table);
        } catch (IOException e) {
            log.error("Error exporting users. {}", e.getMessage());
            e.printStackTrace();
        }
    }
}
