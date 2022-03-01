package com.unipi.torpiles.utils;

import java.util.Scanner;

import static com.unipi.torpiles.utils.Constants.*;

public class UserInput {

    Scanner sc = new Scanner(System.in);

    public String country(){
        System.out.println("Enter country, ex: Greece ");
        String country = sc.nextLine();
        System.out.println("Search for " + country + "...." );

        return country;
    }

    public String months(){
        System.out.println("Enter months, ex: 1-5 ");
        String months = sc.nextLine();
        System.out.println(" Search for " + months + "...." );

        return months;
    }

    public String choices(){
        System.out.println(
                """ 
                    """ + LINE + """
                    - Press 1:""" + SEARCH_BY_COUNTRY  + """
                    - Press 2:""" + SEARCH_BY_MONTHS_AND_COUNTRY + """
                    - Press 3:""" + VIEW_STATISTICS + LINE + """
                """
        );

        final String[] listChoices = {"1", "2","3"};
        String choice ;
        while (true){
            System.out.println("Please press a number [1 or 2 or 3]:");
            choice = sc.nextLine();
            for (String c : listChoices) {
                if(c.equals(choice)) return choice ;
             }
        }

    }
}
