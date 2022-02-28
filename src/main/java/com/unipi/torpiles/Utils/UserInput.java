package com.unipi.torpiles.Utils;

import java.util.Scanner;
import com.unipi.torpiles.Utils.Constants;
import jdk.swing.interop.SwingInterOpUtils;

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
                    """ + LINE + """
                    - Press 1:""" + SEARCH_BY_COUNTRY  + """
                    - Press 2:""" + SEARCH_BY_MONTHS_AND_COUNTRY + """
                    - Press 3:""" + VIEW_STATISTICS + LINE + """
                """
        );

        System.out.println("Press a number [1 or 2 or 3]:");
        int choice = sc.nextInt();

        return choice;
    }

}
