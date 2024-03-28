package com.example.app.services.impl;

import com.example.app.services.SendGridEmailService;
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class SendGridEmailServiceImpl implements SendGridEmailService {

    @Value("${spring.sendgrid.api-key}")
    private String sendGridApiKey;

    private static final List<String> vehicles = new ArrayList<>();

    static {
        vehicles.add("Jeep Compass 2020");
        vehicles.add("Mercedes-Benz Brabus 800 S63 2022");
        vehicles.add("Porsche 918 Spyder 2015");
        vehicles.add("Ford F-150 2022");
        vehicles.add("Jeep Grand Cherokee SRT 2017");
        vehicles.add("Mercedes-Benz SLS AMG GT Final Edition 2020");
        vehicles.add("Mercedes-Benz Maybach GLS 600 2023");
        vehicles.add("Porsche GT3 RS 2023");
        vehicles.add("McLaren Senna 2020");
        vehicles.add("McLaren F1 GTR 1995");
        vehicles.add("Volkswagen Golf 2021");
        vehicles.add("Mercedes-Benz S-Class 2022");
        vehicles.add("Nissan Silvia 180SX 1996");
        vehicles.add("Mercedes-Benz Brabus G900 2023");
        vehicles.add("Mercedes-Benz G-Class 2022");
        vehicles.add("Mercedes-Benz SL-Class 2022");
        vehicles.add("Lamborghini Murcielago 2010");
        vehicles.add("Lamborghini Aventador 2019");
        vehicles.add("Mercedes-Benz E-Class 2014");
        vehicles.add("Rolls-Royce Ghost 2022");
        vehicles.add("Nissan GT-R 2017");
        vehicles.add("Nissan GT-R R33 1995");
        vehicles.add("Subaru Impreza 1998");
        vehicles.add("BMW M4 2022");
        vehicles.add("BMW M5 1999");
        vehicles.add("Bugatti Chiron 2005");
        vehicles.add("Ferrari F40 1992");
    }

    @Override
    public void sendSimpleMessage(String to, String subject, String text) throws IOException {
        if (subject.contains("Newsletter")) {
            String unsubscribeLink = "https://danov-autoshow.azurewebsites.net/newsletter-unsubscribe.html?email=" + to;
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

    @Override
    public void sendWeeklyNewsletter(String email, String subject) throws IOException {
        String unsubscribeLink = "https://danov-autoshow.azurewebsites.net/newsletter-unsubscribe.html?email=" + email;
        List<String> randomVehicles = getRandomVehicles();
        String text = """
                    <div id=popup-container class=popup-container style=display: flex;>\s
                    <div class=popup-content>
                      <p class=motd-text>
                        🎉 Exciting news! New exotic cars added to our collection 🌟
                          <br><br>
                          <strong>%s</strong> - Unleash unparalleled power.
                          <br><br>
                            <strong>%s</strong> - Embrace the power of beast.
                            <br><br>
                            <strong>%s</strong> - Performance and sophistication in every curve.\s
                            <br><br>\s
                            <strong>%s</strong> - Precision engineering meets pure adrenaline.
                           <br><br>
                             Order these or explore more through the Order menu.
                        </p>"\s
                        <div style=text-align: center; margin-top: 20px;>"\s
                           <a href=https://danov-autoshow.azurewebsites.net/auto-show.html?popup=true>Order car</a>"\s
                      <p class=unsub-btn>Unsubscribe<a href="%s">here</a></p>"\s
                        </div>"\s
                    </div>"\s
                "</div>
                """.formatted(randomVehicles.get(0), randomVehicles.get(1), randomVehicles.get(2), randomVehicles.get(3),
                unsubscribeLink);

        Mail mail = constructEmail(email, subject, text);

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

    private List<String> getRandomVehicles() {
        return List.of(vehicles.get(ThreadLocalRandom.current().nextInt(0,29)),
                vehicles.get(ThreadLocalRandom.current().nextInt(0,29)),
                vehicles.get(ThreadLocalRandom.current().nextInt(0,29)),
                vehicles.get(ThreadLocalRandom.current().nextInt(0,29)));
    }

    private static Mail constructEmail(String email, String subject, String text) {
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
        return mail;
    }

    @Override
    public void sendAccountLockEmail(String email, Timestamp expireDate) throws IOException {
        String text = "Your account has been temporarily locked due to too many failed login attempts.<br>" +
                "If this wasn't you, your account may be compromised.<br>" +
                "You can request a password reset via email " +
                "<a href=\"https://danov-autoshow.azurewebsites.net/login\">here</a>.<br>" +
                "Your account lock will be lifted on " + expireDate.toString() + ".";
        String subject = "Account Lock";
        Email from = new Email("danovautoshow@gmail.com");
        Email toEmail = new Email(email);
        Content content = new Content("text/html", text);
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
