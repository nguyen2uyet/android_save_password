package com.example.f89497_quyet_nguyen_save_password;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static Character getCharFromASCIICodeRange(int a, int b) {
        return (char)(Math.floor(Math.random() *(b - a + 1) + a));
    }

    public static String generateStrongPassword(int number){
        List<Character> password = new ArrayList<>();
        for (int i = 1; i <= number; i++) {
            List<Character> list_of_char = new ArrayList<>();
            char lowercase = getCharFromASCIICodeRange(97, 122);
            char uppercase = getCharFromASCIICodeRange(65, 90);
            char special_character_1 = getCharFromASCIICodeRange(32, 47);
            char special_character_2 = getCharFromASCIICodeRange(58, 64);
            char special_character_3 = getCharFromASCIICodeRange(91, 96);
            char special_character_4 = getCharFromASCIICodeRange(123, 126);
            list_of_char.add(lowercase);
            list_of_char.add(uppercase);
            list_of_char.add(special_character_1);
            list_of_char.add(special_character_2);
            list_of_char.add(special_character_3);
            list_of_char.add(special_character_4);
            char randomChar = list_of_char.get((int)Math.floor(Math.random() *6));
            password.add(randomChar);
        }
        StringBuilder sb = new StringBuilder();

        // Appends characters one by one
        for (Character ch : password) {
            sb.append(ch);
        }
        // convert in string
        String str = sb.toString();
        return str;
    }

}
