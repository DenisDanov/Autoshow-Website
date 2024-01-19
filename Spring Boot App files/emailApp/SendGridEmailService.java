package com.example.demo.emailApp;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SendGridEmailService {

    @Value("${spring.sendgrid.api-key}")
    private String sendGridApiKey;

    public void sendSimpleMessage(String to, String subject, String text) throws IOException {
        if (subject.contains("Newsletter")) {
            String unsubscribeLink = "https://danov-autoshow-656625355b99.herokuapp.com/newsletter-unsubscribe.html?email=" + to;
            text = text + "\n\nTo unsubscribe, click here: " + unsubscribeLink;
        }
        Email from = new Email("danovautoshow@gmail.com");
        Email toEmail = new Email(to);
        Content content = new Content("text/plain", text);
        Mail mail = new Mail(from, subject, toEmail, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        Response response = sg.api(request);
        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
        System.out.println(response.getHeaders());
    }

    public void sendWeeklyNewsletter(String email, String subject) throws IOException {
        String text = "<p class=\"motd-text\">" +
                "🎉 We're thrilled to announce the latest additions to our exquisite collection of exotic cars! 🌟" +
                "<br><br>" +
                "<strong>Ford Raptor F-150 - 4X4 Beast." +
                "<br><br>" +
                "<strong>Lamborghini Murcielago 2010</strong> - Embrace the roar of the bull in this iconic masterpiece." +
                "<br><br>" +
                "<strong>Mercedes Benz E-Class 2014</strong> - Combining performance and sophistication in every curve." +
                "<br><br>" +
                "<strong>BMW M4 2022</strong> - Precision engineering meets pure adrenaline." +
                "<br><br>" +
                "These are just a few of the cars that have been added. You can order these, or explore many more through the Order menu." +
                "</p>";

        String cssStyles = "<style>" +
                ".popup-container {" +
                "    display: none;" +
                "    position: fixed;" +
                "    top: 0;" +
                "    left: 0;" +
                "    width: 100%;" +
                "    height: 100%;" +
                "    background: rgba(0, 0, 0, 0.5);" +
                "    /* Semi-transparent overlay */" +
                "    z-index: 1;" +
                "    overflow: hidden;" +
                "    /* Disable scrolling on the body when pop-up is displayed */" +
                "}" +
                "" +
                "/* Styles for the pop-up content */" +
                ".popup-content {" +
                "    background-color: #4e4747;" +
                "    color: #d3d2d2;" +
                "    padding: 10px;" +
                "    border-radius: 10px;" +
                "    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);" +
                "    max-width: 800px;" +
                "    margin: 13% auto;" +
                "}" +
                "" +
                ".motd-text {" +
                "    font-size: 18px;" +
                "    line-height: 1.5;" +
                "    color: #161010;" +
                "}" +
                "" +
                "/* Styles for the buttons */" +
                ".popup-container button {" +
                "    background-color: #696767;" +
                "    color: white;" +
                "    padding: 10px 20px;" +
                "    border: none;" +
                "    border-radius: 5px;" +
                "    cursor: pointer;" +
                "    margin: 5px;" +
                "}" +
                "" +
                ".popup-container button:hover {" +
                "    background-color: rgb(152, 153, 152);" +
                "}" +
                "" +
                "@media (max-width: 600px) {" +
                "    .popup-content {" +
                "        max-width: 90%;" +
                "        margin: 22% auto;" +
                "    }" +
                "" +
                "    .motd-text {" +
                "        font-size: 13px !important;" +
                "    }" +
                "}" +
                "</style>";

        String fullHtmlContent = "<html><head>" + cssStyles + "</head><body>" + text + "</body></html>";

        Email from = new Email("danovautoshow@gmail.com");
        Email toEmail = new Email(email);
        Content content = new Content("text/html", fullHtmlContent);
        Mail mail = new Mail(from, subject, toEmail, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        Response response = sg.api(request);
        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
        System.out.println(response.getHeaders());
    }
}
