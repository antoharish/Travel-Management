package com.cts.project.Travelling_package.Service;

import com.cts.project.Travelling_package.Model.FlightBooking;
import com.cts.project.Travelling_package.Model.HotelBooking;
import com.cts.project.Travelling_package.Model.Payment;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfGeneratorService {

    public byte[] generateHotelBookingPdf(HotelBooking booking) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            document.add(new Paragraph("Hotel Booking E-Ticket", titleFont));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Booking ID: " + booking.getBookingId(), bodyFont));
            document.add(new Paragraph("Hotel: " + booking.getHotel().getName(), bodyFont));
            document.add(new Paragraph("Location: " + booking.getHotel().getLocation(), bodyFont));
            document.add(new Paragraph("Booking Date: " + booking.getBookingDate(), bodyFont));
            document.add(new Paragraph("Rooms Booked: " + booking.getQuantity(), bodyFont));
            document.add(new Paragraph("Price per Night: ₹" + booking.getHotel().getPricePerNight(), bodyFont));
            document.add(new Paragraph("Total Price: ₹" + (booking.getQuantity() * booking.getHotel().getPricePerNight()), bodyFont));
            document.add(new Paragraph("Booked By: " + booking.getUser().getUsername(), bodyFont));
            document.add(new Paragraph("Email: " + booking.getEmail(), bodyFont));

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }

    public byte[] generateFlightBookingPdf(FlightBooking booking) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            document.add(new Paragraph("Flight Booking E-Ticket", titleFont));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Booking ID: " + booking.getBookingId(), bodyFont));
            document.add(new Paragraph("Flight: " + booking.getFlight().getDepartureCity() + " → " + booking.getFlight().getDestinationCity(), bodyFont));
            document.add(new Paragraph("Flight Date: " + booking.getFlightDate(), bodyFont));
            document.add(new Paragraph("Departure Time: " + booking.getFlight().getDepartureTime(), bodyFont));
            document.add(new Paragraph("Arrival Time: " + booking.getFlight().getArrivalTime(), bodyFont));
            document.add(new Paragraph("Seats Booked: " + booking.getQuantity(), bodyFont));
            document.add(new Paragraph("Price per Seat: ₹" + booking.getFlight().getPrice(), bodyFont));
            document.add(new Paragraph("Total Price: ₹" + (booking.getQuantity() * booking.getFlight().getPrice()), bodyFont));
            document.add(new Paragraph("Booked By: " + booking.getUser().getUsername(), bodyFont));
            document.add(new Paragraph("Email: " + booking.getEmailId(), bodyFont));

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }
    public byte[] generatePaymentPdf(Payment payment) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            document.add(new Paragraph("Payment Invoice Details", titleFont));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Booking ID: " + payment.getBookingId(), bodyFont));
            document.add(new Paragraph("Booked By: " + payment.getUser().getUsername(), bodyFont));
            document.add(new Paragraph("Email: " + payment.getUser().getEmail(), bodyFont));
            document.add(new Paragraph("Total Amount:  " + payment.getAmount() + " (With Tax)", bodyFont));
            document.add(new Paragraph(": " + payment.getUser().getEmail(), bodyFont));

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }
}
