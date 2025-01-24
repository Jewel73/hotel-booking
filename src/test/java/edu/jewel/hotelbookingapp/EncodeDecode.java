package edu.jewel.hotelbookingapp;

import java.util.ArrayList;
import java.util.List;

public class EncodeDecode {
    public String encode(List<String> strs) {
        StringBuilder builder = new StringBuilder();
        for (String str : strs){
            builder.append(str.length()).append('#').append(str);
        }
        return builder.toString();
    }

    public List<String> decode(String str) {
        List<String> res = new ArrayList<>();
        int i = 0;
        while (i < str.length()){
            int j = i;
            while (str.charAt(j) != '#'){
                j++;
            }
            int length = Integer.parseInt(str.substring(i , j));
            i = j+1;
            j = i + length;
            res.add(str.substring(i, j));
            i = j;
        }
        return res;
    }

    public static void main(String[] args) {
        EncodeDecode encodeDecode = new EncodeDecode();
        String encoded = encodeDecode.encode(new ArrayList<>());
        encodeDecode.decode(encoded);
    }
}
