package com.unipi.torpiles.utils;

import java.time.Month;
import java.util.List;
import java.util.Scanner;

import static com.unipi.torpiles.utils.Constants.*;

public class UserInput {

    Scanner sc = new Scanner(System.in);

    public String country(){
        System.out.println("Enter country:" + Color.WHITE + " ex: Greece " + Color.RESET);
        String country = sc.nextLine();
        System.out.println(Color.YELLOW + "Search for " + country + "....\n" + Color.RESET);

        return country;
    }

    public List<String> months(){
        System.out.println("""
                            Input time period of months to display results:
                            """+ Color.WHITE + """
                            Examples:
                            - 1-3 or 3-1 is January to March etc.
                            - 1-1 is a single month (January).""" + Color.RESET + """
                            """);

        List<String> sortMonths = null;
        try {
            String monthUserInput = sc.nextLine();
            sortMonths = List.of(monthUserInput.split("-"));

            sortMonths = sortMonths
                    .stream()
                    .sorted(String::compareTo).toList();

            //System.err.println(sortMonths.get(0) + "  " + sortMonths.get(1));
            //Month month1 = Month.of(Integer.parseInt(sortMonths.get(0)));
            //Month month2 = Month.of(Integer.parseInt(sortMonths.get(1)));

            System.out.println(Color.YELLOW+
                    "Search from\040" +
                    Month.of(Integer.parseInt(sortMonths.get(0))) + "\040to\040" +
                    Month.of(Integer.parseInt(sortMonths.get(1))) + "....\n" + Color.RESET);

            return sortMonths;
        } catch (Exception e) {
            System.out.println(Color.RED + ERR_WRONG + Color.RESET);
        }
        return sortMonths;
    }

    public String choices(){
        System.out.println(
                """ 
                    """ + LINE + """
                    - Press 1:""" + SEARCH_BY_COUNTRY  + """
                    - Press 2:""" + SEARCH_BY_MONTHS_AND_COUNTRY + """
                    - Press 3:""" + VIEW_STATISTICS + """
                    - Write "exit" for exit:
                    """ + LINE + """
                """
        );

        final String[] listChoices = {"1", "2","3","exit"};
        String choice ;
        while (true){
            System.out.println("Please press a number [1 or 2 or 3] or exit:");
            choice = sc.nextLine();
            for (String c : listChoices) {
                if(c.equals(choice)) return choice ;
             }
        }
    }
}
