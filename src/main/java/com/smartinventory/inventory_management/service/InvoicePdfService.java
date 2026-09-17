package com.smartinventory.inventory_management.service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import com.smartinventory.inventory_management.entity.Sale;
import com.smartinventory.inventory_management.repository.SaleRepository;

@Service
public class InvoicePdfService {

    private final SaleRepository saleRepository;

    public InvoicePdfService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    public byte[] generateInvoicePdf(Long saleId) {

        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Sale not found"));

        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Document document = new Document();

            PdfWriter.getInstance(document, outputStream);

            document.open();

            // Fonts
            Font shopNameFont = new Font(Font.HELVETICA, 20, Font.BOLD);
            Font normalFont = new Font(Font.HELVETICA, 10, Font.NORMAL);
            Font boldFont = new Font(Font.HELVETICA, 10, Font.BOLD);
            Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);

            // Shop Name
            Paragraph shopName = new Paragraph(
                    "BARNWAL ENTERPRISES",
                    shopNameFont
            );
            shopName.setAlignment(Element.ALIGN_CENTER);
            document.add(shopName);

            // Address
            Paragraph address = new Paragraph(
                    "Your Shop Address Here",
                    normalFont
            );
            address.setAlignment(Element.ALIGN_CENTER);
            document.add(address);

            // Phone
            Paragraph phone = new Paragraph(
                    "Phone: XXXXX XXXXXXX",
                    normalFont
            );
            phone.setAlignment(Element.ALIGN_CENTER);
            document.add(phone);

            // GSTIN - show only when provided
            if (sale.getGstNumber() != null
                    && !sale.getGstNumber().trim().isEmpty()) {

                Paragraph gstin = new Paragraph(
                        "GSTIN: " + sale.getGstNumber(),
                        boldFont
                );

                gstin.setAlignment(Element.ALIGN_CENTER);
                document.add(gstin);
            }

            document.add(new Paragraph(" "));

            // Invoice title
            Paragraph invoiceTitle = new Paragraph(
                    "INVOICE",
                    titleFont
            );
            invoiceTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(invoiceTitle);

            document.add(new Paragraph(" "));

            // Invoice information table
            PdfPTable invoiceInfoTable = new PdfPTable(2);
            invoiceInfoTable.setWidthPercentage(100);

            addInfoCell(invoiceInfoTable, "Invoice No.", String.valueOf(sale.getId()));
            addInfoCell(
                    invoiceInfoTable,
                    "Date",
                    sale.getDate().format(
                            DateTimeFormatter.ofPattern("dd-MM-yyyy")
                    )
            );

            addInfoCell(
                    invoiceInfoTable,
                    "Customer Name",
                    sale.getCustomerName()
            );

            document.add(invoiceInfoTable);

            document.add(new Paragraph(" "));

            // Product details heading
            Paragraph productHeading = new Paragraph(
                    "Product Details",
                    boldFont
            );
            document.add(productHeading);

            // Product table
            PdfPTable productTable = new PdfPTable(3);
            productTable.setWidthPercentage(100);

            addHeaderCell(productTable, "Product ID");
            addHeaderCell(productTable, "Quantity");
            addHeaderCell(productTable, "Price");

            addTableCell(productTable, String.valueOf(sale.getProductId()));
            addTableCell(productTable, String.valueOf(sale.getQuantity()));
            addTableCell(
                    productTable,
                    String.format("Rs. %.2f", sale.getSellingPrice())
            );

            document.add(productTable);

            document.add(new Paragraph(" "));

            // Amount summary table
            PdfPTable amountTable = new PdfPTable(2);
            amountTable.setWidthPercentage(100);

            addAmountRow(
                    amountTable,
                    "Subtotal",
                    String.format("Rs. %.2f", sale.getSubtotal())
            );

            double discountAmount
                    = sale.getSubtotal()
                    * sale.getDiscountPercentage()
                    / 100;

            addAmountRow(
                    amountTable,
                    String.format(
                            "Discount (%.2f%%)",
                            sale.getDiscountPercentage()
                    ),
                    String.format("-Rs. %.2f", discountAmount)
            );

            addAmountRow(
                    amountTable,
                    "Taxable Amount",
                    String.format("Rs. %.2f", sale.getFinalAmount())
            );

            addAmountRow(
                    amountTable,
                    String.format(
                            "CGST (%.2f%%)",
                            sale.getCgstPercentage()
                    ),
                    String.format("Rs. %.2f", sale.getCgstAmount())
            );

            addAmountRow(
                    amountTable,
                    String.format(
                            "SGST (%.2f%%)",
                            sale.getSgstPercentage()
                    ),
                    String.format("Rs. %.2f", sale.getSgstAmount())
            );

            // Grand Total
            PdfPCell grandTotalLabel = new PdfPCell(
                    new Phrase("GRAND TOTAL", boldFont)
            );
            grandTotalLabel.setBorder(Rectangle.TOP);
            grandTotalLabel.setPadding(6);

            PdfPCell grandTotalValue = new PdfPCell(
                    new Phrase(
                            String.format(
                                    "Rs. %.2f",
                                    sale.getGrandTotal()
                            ),
                            boldFont
                    )
            );
            grandTotalValue.setBorder(Rectangle.TOP);
            grandTotalValue.setPadding(6);
            grandTotalValue.setHorizontalAlignment(
                    Element.ALIGN_RIGHT
            );

            amountTable.addCell(grandTotalLabel);
            amountTable.addCell(grandTotalValue);

            document.add(amountTable);

            document.add(new Paragraph(" "));

            // Thank you
            Paragraph thankYou = new Paragraph(
                    "Thank you for your business!",
                    normalFont
            );
            thankYou.setAlignment(Element.ALIGN_CENTER);
            document.add(thankYou);

            document.close();

            return outputStream.toByteArray();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error while generating invoice PDF",
                    e
            );
        }
    }

    // Add invoice information cell
    private void addInfoCell(
            PdfPTable table,
            String label,
            String value) {

        PdfPCell labelCell = new PdfPCell(
                new Phrase(label, new Font(
                        Font.HELVETICA,
                        10,
                        Font.BOLD
                ))
        );

        labelCell.setPadding(5);

        PdfPCell valueCell = new PdfPCell(
                new Phrase(
                        value != null ? value : "",
                        new Font(
                                Font.HELVETICA,
                                10,
                                Font.NORMAL
                        )
                )
        );

        valueCell.setPadding(5);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    // Add table header cell
    private void addHeaderCell(
            PdfPTable table,
            String text) {

        PdfPCell cell = new PdfPCell(
                new Phrase(
                        text,
                        new Font(
                                Font.HELVETICA,
                                10,
                                Font.BOLD
                        )
                )
        );

        cell.setPadding(6);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        table.addCell(cell);
    }

    // Add normal table cell
    private void addTableCell(
            PdfPTable table,
            String text) {

        PdfPCell cell = new PdfPCell(
                new Phrase(
                        text,
                        new Font(
                                Font.HELVETICA,
                                10,
                                Font.NORMAL
                        )
                )
        );

        cell.setPadding(6);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        table.addCell(cell);
    }

    // Add amount row
    private void addAmountRow(
            PdfPTable table,
            String label,
            String value) {

        PdfPCell labelCell = new PdfPCell(
                new Phrase(
                        label,
                        new Font(
                                Font.HELVETICA,
                                10,
                                Font.NORMAL
                        )
                )
        );

        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPadding(5);

        PdfPCell valueCell = new PdfPCell(
                new Phrase(
                        value,
                        new Font(
                                Font.HELVETICA,
                                10,
                                Font.NORMAL
                        )
                )
        );

        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPadding(5);
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }
}
