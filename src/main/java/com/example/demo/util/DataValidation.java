package com.example.demo.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataValidation {
    public static boolean isValidPhoneNumber(String number) {
        String regex = "^\\+?\\d{10,12}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(number);

        return !matcher.matches();
    }

    public static boolean isValidUsername(String username) {
        String regex = "^[A-Za-zА-Яа-я0-9'-.\\_]{0,30}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(username);

        return !matcher.matches();
    }

    public static boolean isValidName(String name) {
        String regex = "^[A-Za-zА-Яа-я ]{0,30}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);

        return !matcher.matches();
    }

    public static boolean isValidEmail(String email){
        String regex = "^[\\w\\-\\.]+@([\\w-]{3,5}+\\.)+[\\w-]{2,4}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        return !matcher.matches();
    }

    public static boolean isValidPassword(String password){
        String regex = "^.{5,}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);

        return !matcher.matches();
    }
}