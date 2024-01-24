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
        String unsubscribeLink = "https://danov-autoshow-656625355b99.herokuapp.com/newsletter-unsubscribe.html?email=" + email;
        String text = "<div id=\"popup-container\" class=\"popup-container\" style=\"display: flex;\">" +
                "    <div class=popup-content>" +
                "        <p class=\"motd-text\">" +
                "            🎉 Exciting news! New exotic cars added to our collection 🌟" +
                "            <br><br>" +
                "            <strong>Bugatti Chiron 2020</strong> - Unleash unparalleled power." +
                "            <br><br>" +
                "            <strong>Lamborghini Murcielago 2010</strong> - Embrace the roar of the bull." +
                "            <br><br>" +
                "            <strong>Mercedes Benz AMG GT 2020</strong> - Performance and sophistication in every curve." +
                "            <br><br>" +
                "            <strong>Nissan GTR 2017</strong> - Precision engineering meets pure adrenaline." +
                "            <br><br>" +
                "             Order these or explore more through the Order menu." +
                "        </p>" +
                "        <div style=\"text-align: center; margin-top: 20px;\">" +
                "            <a href=\"https://danov-autoshow-656625355b99.herokuapp.com/auto-show.html?popup=true\">Order car</a>" +
                "      <p class=\"unsub-btn\">Unsubscribe <a href=\"" + unsubscribeLink + "\">here</a></p>" +
                "        </div>" +
                "    </div>" +
                "</div>";

        String cssStyles = "<style>" +
                "/* Styles for the pop-up content */" +
                ".popup-content {" +
                "    background-color: #4e4747;" +
                "    color: #d3d2d2;" +
                "    padding: 10px;" +
                "    border-radius: 10px;" +
                "    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);" +
                "}" +
                "" +
                ".motd-text {" +
                "    font-size: 18px;" +
                "    line-height: 1.5;" +
                "    color: #161010;" +
                "}" +
                "" +
                "/* Styles for the buttons */" +
                ".popup-container button,.popup-container a,.popup-content a {" +
                "    text-decoration: none;" +
                "    cursor: pointer;" +
                "    background-color: #696767;" +
                "    color: white;" +
                "    padding: 10px 20px;" +
                "    border: none;" +
                "    border-radius: 5px;" +
                "    cursor: pointer;" +
                "    margin: 5px;" +
                "}" +
                "" +
                ".popup-container button:hover,.popup-container a:hover {" +
                "    background-color: rgb(152, 153, 152);" +
                "}" +
                ".unsub-btn {" +
                "    margin-top: 2rem;" +
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
