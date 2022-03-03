package com.unipi.torpiles.utils;

import java.time.Month;
import java.util.List;
import java.util.Scanner;

import static com.unipi.torpiles.utils.Constants.*;

public class UserInput {

    Scanner sc = new Scanner(System.in);

    public String country(){
        System.out.println("Enter country, ex: Greece ");
        String country = sc.nextLine();
        System.out.println("Search for " + country + "....\n" );

        return country;
    }

    public List<String> months(){
        System.out.println("""
                            Input time period of months to display results
                            (examples: 1-3 or 3-1  is January to March etc.):\040
                            """);

        List<String> sortMonths = null;
        try {
            String monthUserInput = sc.nextLine();
            sortMonths = List.of(monthUserInput.split("-"));
            //System.err.println("Sort:" + month1 + "," + month2);
            sortMonths = sortMonths
                    .stream()
                    .sorted(String::compareTo).toList();

            //System.err.println(sortMonths.get(0) + "  " + sortMonths.get(1));
            //Month month1 = Month.of(Integer.parseInt(sortMonths.get(0)));
            //Month month2 = Month.of(Integer.parseInt(sortMonths.get(1)));

            System.err.println(
                    "Search from\040" +
                    Month.of(Integer.parseInt(sortMonths.get(0))) + "\040to\040" +
                            Month.of(Integer.parseInt(sortMonths.get(1))));

            return sortMonths;
        } catch (Exception e) {
            System.err.println(ERR_WRONG);
        }
        return sortMonths;
    }

    public String choices(){
        //TODO press kye for exit
        System.out.println(
                """ 
                    """ + LINE + """
                    - Press 1:""" + SEARCH_BY_COUNTRY  + """
                    - Press 2:""" + SEARCH_BY_MONTHS_AND_COUNTRY + """
                    - Press 3:""" + VIEW_STATISTICS + LINE + """
                    - Press e for exit
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
