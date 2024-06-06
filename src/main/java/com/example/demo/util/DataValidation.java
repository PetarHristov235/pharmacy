package com.example.demo.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataValidation {
    public static boolean isValidPhoneNumber(String number) {
        String regex = "^[+]?(\\d{1,2})?[\\s.-]?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(number);

        return !matcher.matches();
    }

    public static boolean isValidUsername(String username) {
        String regex = "^[A-Za-z0-9'-. \\_]{0,30}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(username);

        return !matcher.matches();
    }

    public static boolean isValidName(String name) {
        String regex = "^[A-Z][a-z]{0,29}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);

        return !matcher.matches();
    }

    public static boolean isValidEmail(String email){
        String regex = "^[\\w\\-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        return !matcher.matches();
    }

    public static boolean isValidPassword(String password){
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?!.* ).{8,20}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);

        return !matcher.matches();
    }

}
