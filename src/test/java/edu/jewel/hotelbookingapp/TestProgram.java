package edu.jewel.hotelbookingapp;

import java.util.*;

public class TestProgram {
    public static void main(String[] arg) {
        String[] strs = {"eat","tea","tan","ate","nat","bat"};


        Map<String, List<String>> res = new HashMap<>();

        for( String str : strs){
            char[] letters = new char[26];
            for(char c : str.toCharArray()){
                letters[c-'a' ]++;
            }

            String key = new String(letters);
            List<String> list = res.getOrDefault(key, new ArrayList<>());
            list.add(str);
            res.put(key, list);
        }

    }
}
