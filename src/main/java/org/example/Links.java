package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Links {
    private static String line;
    public static String hollyToDay() throws IOException {

        String stringUrl= "https://kakoysegodnyaprazdnik.ru";
        URL url1 = new URL(stringUrl);
        URLConnection connection = url1.openConnection();
        HttpURLConnection httpConnection = (HttpURLConnection) connection;
        httpConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36 Edg/120.0.0.0");
        int responseCode = httpConnection.getResponseCode();
        System.out.println("Response Code: " + responseCode);
        try(BufferedReader in = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()))) {
            String temp;
            while ((temp = in.readLine()) != null){
                if(temp.contains("itemprop=\"acceptedAnswer")){
                    line = temp;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Pattern pattern = Pattern.compile("<span itemprop=\"text\">(.+?)</span>");
        Matcher matcher = pattern.matcher(line);
        String result = "";
        matcher.find();
        int num = 1;
        result += String.valueOf(num) + ". " +  matcher.group(1) + "\n" + "\n";

        return result;
    }
}

