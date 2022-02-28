package com.unipi.torpiles.Utils;

import java.util.Scanner;
import com.unipi.torpiles.Utils.Constants;

import static com.unipi.torpiles.Utils.Constants.*;

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
        System.out.println("Search for " + months + "...." );

        return months;
    }

    public int choices(){
        System.out.println(
                """
                    -----------------------------------------------------------------------
                    - Press 1:""" + SEARCH_BY_COUNTRY + """
                    - Press 2: Search for monthly results for a specific year by country.
                    - Press 3: View statistics.
                    -----------------------------------------------------------------------
                    """);
        int choice = sc.nextInt();

        return choice;
    }

}
