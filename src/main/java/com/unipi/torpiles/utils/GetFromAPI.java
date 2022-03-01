package com.unipi.torpiles.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static com.unipi.torpiles.utils.Constants.URL_COUNTRY_API;

public class GetFromAPI {

    public void searchByCountry(String country) throws IOException {
        URL apiURL = new URL(URL_COUNTRY_API + country);
        URLConnection URLConnect = apiURL.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        URLConnect.getInputStream()));
        String inputLine;
        ArrayList<String> result = new ArrayList<>();
        while ((inputLine = in.readLine()) != null){
            result.add(inputLine);
            System.out.println(inputLine);
        }

        result.stream()
                .sorted((s1,s2)-> {
                    System.out.println("sort:"+s1+","+s2);
                    return s1.compareTo(s2);
                })
                .filter(s -> {
                    s.replace("{", "/");
                    System.out.println("filter:"+s);
                    return true;
                })
                .map(s -> {
                    System.out.println("map:"+s);
                    return s;
                })
                .forEach(s -> System.out.println("forEach:"+s));

        in.close();

    }

}
