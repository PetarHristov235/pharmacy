package com.example.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void orderConfirmationEmail(String receiverEmail,
                                       String receiverName,
                                       String title,
                                       String address,
                                       String phoneNumber,
                                       String date,
                                       String orderNumber){
        SimpleMailMessage message=new SimpleMailMessage();

        message.setFrom("blogemailservice512@gmail.com");
        message.setTo(receiverEmail);
        message.setText("Скъпи "+receiverName+",\n" +
                "\n" +
                "Радваме се да ви информираме, че вашата поръчка от нашата библиотека беше успешно обработена и потвърдена.\n" +
                "\n" +
                "Детайлите на вашата поръчка:\n" +
                "\n" +
                "Номер на поръчка : "+orderNumber+"\n" +
                "Дата на поръчка : "+date+"\n" +
                "Адрес за доставка : "+address+"\n" +
                "Телефонен номер : "+phoneNumber+"\n"+
                "Поръчана книга : \""+title+"\"\n" +
                "\n" +
                "Моля, прегледайте горепосочените детайли и ни уведомете, ако имате въпроси или притеснения. Ако трябва да направите някакви промени в поръчката си, моля, свържете се с нас възможно най-скоро.\n" +
                "\n" +
                "Благодарим ви още веднъж, че избрахте нашата библиотека, за да изберете вашата книга. Приятно четене и очакваме с нетърпение следващата ни среща!\n" );
        message.setSubject("Михбук потвърждаване на поръчка - "+orderNumber);


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
                "Радваме се да ви информираме, че вашата регистрация е направена успешно.\n" +
                "\n" +
                "С удолволстие ще Ви помогнем да се потопите в магическия свят на книгите!\n"
        );
        message.setSubject("Успешна регистрация");

        mailSender.send(message);
    }

    public void orderDeadlineOverdueEmail(String receiverEmail,
                                                 String receiverName,
                                                 String book
    ) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("blogemailservice512@gmail.com");
        message.setTo(receiverEmail);
        message.setText("Скъпи " + receiverName + ",\n" +
                "\n" +
                "Искаме да ви уведомим, че срокът за връщане на книгата "+ book +" е изтекъл.\n" +
                "\n" +
                "В знак на добра воля ще удължим срока с още 1 седмица! \n" +
                "\n" +
                "Приятно четене! \n"

        );
        message.setSubject("Просрочен срок за връщане");

        mailSender.send(message);
    }
}
