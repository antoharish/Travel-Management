//package com.cts.project.Travelling_package.Service;
//
//import com.cts.project.Travelling_package.Model.FlightBooking;
//import com.cts.project.Travelling_package.Model.HotelBooking;
//import com.cts.project.Travelling_package.Model.Payment;
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
//import jakarta.mail.util.ByteArrayDataSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Service;
//
//@Service
//public class EmailService {
//
//    @Autowired
//    private JavaMailSender mailSender;
//
//    public void sendBookingConfirmation(String to, Object booking) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(to);
//        if (booking instanceof HotelBooking hotelBooking) {
//            message.setText("Your hotel booking is confirmed. Booking details: " + hotelBooking.toString());
//        } else if (booking instanceof FlightBooking flightBooking) {
//            message.setText("Your flight booking is confirmed. Booking details: " + flightBooking.toString());
//        }
//
//        mailSender.send(message);
//    }
//
//    public void sendBookingConfirmationWithPdf(String to, HotelBooking booking, byte[] pdfBytes) {
//        sendMimeEmail(to, "Hotel Booking Confirmation with E-Ticket",
//                "Dear " + booking.getUser().getUsername() + ",\n\nYour hotel booking is confirmed. Please find your e-ticket attached.\n\nThank you!",
//                "Hotel_E-Ticket.pdf", pdfBytes);
//    }
//
//    public void sendBookingConfirmationWithPdf(String to, FlightBooking booking, byte[] pdfBytes) {
//        sendMimeEmail(to, "Flight Booking Confirmation with E-Ticket",
//                "Dear " + booking.getUser().getUsername() + ",\n\nYour flight booking is confirmed. Please find your e-ticket attached.\n\nThank you!",
//                "Flight_E-Ticket.pdf", pdfBytes);
//    }
//    // Payment mail
//    public void sendPaymentConfirmationEmail(String toEmail, Payment payment, byte[] pdfBytes) {
////        SimpleMailMessage message = new SimpleMailMessage();
////        message.setTo(to);
////        message.setSubject(subject);
////        message.setText(body);
////        mailSender.send(message);
//        sendMimePaymentEmail(toEmail, "Payment Confirmation - Booking ID: " + payment.getBookingId(),
//                "Dear " + payment.getUser().getEmail() + ",\n\n" +
//                        "Thank you for using our service.\n\n" +
//                        "Payment Details:\n" +
//                        "Booking ID: " + payment.getBookingId() + "\n" +
//                        "Amount Paid: " + payment.getAmount() + " " + payment.getPaymentMethod() + "\n\n" +
//                        "Your payment was successfully processed. For any queries, please contact us.\n\n" +
//                        "Regards,\nTravelling Package Team.\n Have a Great Vacation!",
//                "PaymentConfirmation.pdf", pdfBytes);
//    }
//
//    private void sendMimeEmail(String to, String subject, String body, String attachmentName, byte[] pdfBytes) {
//        MimeMessage message = mailSender.createMimeMessage();
//
//        try {
//            MimeMessageHelper helper = new MimeMessageHelper(message, true);
//            helper.setTo(to);
//            helper.setSubject(subject);
//            helper.setText(body);
//            helper.addAttachment(attachmentName, new ByteArrayDataSource(pdfBytes, "application/pdf"));
//            mailSender.send(message);
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//    }
//    private void sendMimePaymentEmail(String to, String subject, String body, String attachmentName, byte[] pdfBytes) {
//        MimeMessage message = mailSender.createMimeMessage();
//
//        try {
//            MimeMessageHelper helper = new MimeMessageHelper(message, true);
//            helper.setTo(to);
//            helper.setSubject(subject);
//            helper.setText(body);
//            helper.addAttachment(attachmentName, new ByteArrayDataSource(pdfBytes, "application/pdf"));
//            mailSender.send(message);
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//}
