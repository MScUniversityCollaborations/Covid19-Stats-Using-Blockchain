package com.unipi.torpiles.utils;

import java.time.Month;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.unipi.torpiles.utils.Constants.*;

public class UserInput {

    Scanner sc = new Scanner(System.in);

    public String country(){
        System.out.println("Enter country:" + Color.WHITE + " ex: Greece " + Color.RESET);
        String country = sc.nextLine();
        System.out.println(Color.YELLOW + "Search for " + country + "....\n" + Color.RESET);

        return country;
    }

//    public String country(Boolean fromDataSet){
//        while (true) {
//
//
//        }
//
//
//        System.out.println("Enter country:" + Color.WHITE + " ex: Greece " + Color.RESET);
//        String country = sc.nextLine();
//        System.out.println(Color.YELLOW + "Search for " + country + "....\n" + Color.RESET);
//
//        return country;
//    }


    public List<String> months(){
        List<String> sortMonths = null;

        try {
                while (true) {
                    System.out.println(INPUT_MONTHS);
                    String monthUserInput = sc.nextLine();
                    //System.err.println(monthUserInput);
                    Pattern pattern = Pattern.compile("\\d\\d?[-]\\d\\d?");
                    Matcher matcher = pattern.matcher(monthUserInput);

                    if (matcher.matches()) {
                        sortMonths = List.of(monthUserInput.split("-"));

                        sortMonths = sortMonths
                                .stream()
                                .sorted(String::compareTo).toList();

                        for (String month1 : ALL_MONTHS) {
                            for (String month2 : ALL_MONTHS) {
                                if (sortMonths.get(0).equals(month1) && sortMonths.get(1).equals(month2)) {
                                    System.out.println(Color.YELLOW+
                                            "Search from\040" +
                                            Month.of(Integer.parseInt(sortMonths.get(0))) + "\040to\040" +
                                            Month.of(Integer.parseInt(sortMonths.get(1))) + "....\n" + Color.RESET);
                                    return sortMonths;
                                }
                            }
                        }
                    }
                }
        } catch (Exception e) {
            System.out.println(Color.RED + ERR_WRONG + Color.RESET);
            System.out.println(Color.RED + ERR_INVALID_IMPORT + Color.RESET);
        }
        return sortMonths;
    }

    public String choices(){
        System.out.println(INPUT_CHOICE);

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
