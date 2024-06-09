package com.example.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    String link = "http://localhost:8080/rateUs";
    public void orderConfirmationEmail(String receiverEmail,
                                       String receiverName,
                                       String orderedItems,
                                       BigDecimal totalPrice,
                                       String address,
                                       String phoneNumber,
                                       String date,
                                       String orderNumber){
        SimpleMailMessage message=new SimpleMailMessage();

        message.setFrom("blogemailservice512@gmail.com");
        message.setTo(receiverEmail);
        message.setText("Скъпи "+receiverName+",\n" +
                "\n" +
                "Радваме се да ви информираме, че вашата поръчка беше успешно обработена и потвърдена.\n" +
                "\n" +
                "Детайлите на вашата поръчка:\n" +
                "\n" +
                "Номер на поръчка : "+orderNumber+"\n" +
                "Дата на поръчка : "+date+"\n" +
                "Адрес за доставка : "+address+"\n" +
                "Телефонен номер : "+phoneNumber+"\n"+
                "Поръчани продукти: \n"
                +editItems(orderedItems)+
                "\n" +
                "Обща сума : "+totalPrice+"\n" +
                "\n" +
                "Моля, прегледайте горепосочените детайли и ни уведомете, ако имате въпроси или притеснения. Ако трябва да направите някакви промени в поръчката си, моля, свържете се с нас възможно най-скоро.\n" +
                "\n" +
                "Благодарим Ви, че ни се доверихте, за да се погрижим за вашето здраве!\n"+
                "\n"+
                "Ако сте доволни от нашето обслужване може да ни оцените като кликнете тук:\n"+
                link+
                "\n"+
                "Поздрави,\n"+
                "Екипът на Аптека Сияна"
        );
        message.setSubject("Аптека Сияна - потвърждаване на поръчка: "+orderNumber);


        mailSender.send(message);
    }



    public void registrationConfirmationEmail(String receiverEmail,
                                          String receiverName
                                      ) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("blogemailservice512@gmail.com");
        message.setTo(receiverEmail);
        message.setText("Скъпи " + receiverName + ",\n" +
                "\n" +
                "Радваме се да ви информираме, че вашата регистрация е направена успешно!\n" +
                "\n" +
                "Ще се радваме да помогнем за вашето здраве и красота!\n"+
                "\n" +
                "Поздрави,\n"+
                "Екипът на Аптека Сияна"
        );
        message.setSubject("Успешна регистрация");

        mailSender.send(message);
    }

    private String editItems(String items){
        return  items.replace("[", "").replace("]", "").replace(",", "");
    }

}
