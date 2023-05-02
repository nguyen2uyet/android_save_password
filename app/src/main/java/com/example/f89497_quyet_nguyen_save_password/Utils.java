package com.example.f89497_quyet_nguyen_save_password;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static Character getCharFromASCIICodeRange(int a, int b) {
        return (char)(Math.floor(Math.random() *(b - a + 1) + a));
    }

    public static void getListViewSize(ListView myListView) {
        ListAdapter myListAdapter=myListView.getAdapter();
        if (myListAdapter==null) {
            //do nothing return null
            return;
        }
        //set listAdapter in loop for getting final size
        int totalHeight=0;
        for (int size=0; size < myListAdapter.getCount(); size++) {
            View listItem=myListAdapter.getView(size, null, myListView);
            listItem.measure(0, 0);
            totalHeight+=listItem.getMeasuredHeight();
        }
        //setting listview item in adapter
        ViewGroup.LayoutParams params=myListView.getLayoutParams();
        params.height=totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
        myListView.setLayoutParams(params);
        // print height of adapter on log
        Log.i("height of listItem:", String.valueOf(totalHeight));
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
