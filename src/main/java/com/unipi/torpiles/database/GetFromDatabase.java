package com.unipi.torpiles.database;

import com.unipi.torpiles.models.RecordForStats;
import com.unipi.torpiles.utils.console.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.unipi.torpiles.utils.Constants.*;

public class GetFromDatabase {
    int totalDeaths = 0 ;
    int totalCases = 0;
    int totalMonths = 0 ;

    String tempCountry;
    String tempCountry2 = null;
    int tempMonth;
    int tempMonth2 = 0;
    final List<String> countries = new ArrayList<>();
    final List<String> countries2 = new ArrayList<>();
    public void resultStats() {

        System.out.println(Color.CYAN + "COVID STATS FROM DATABASE" + Color.RESET);
        List<RecordForStats> data = DBManager.getInstance().dataForStats();
        RecordForStats[] dataArray = DBManager.getInstance().dataForStats().toArray(new RecordForStats[0]);
        if (!DBManager.getInstance().dataForStats().isEmpty()) {

            System.out.print(LINE);
            // All Countries
            Arrays.stream(dataArray)

                    .forEach(asd -> {
//                        tempCountry = asd.getCountry();
//                        if (!tempCountry.equalsIgnoreCase(tempCountry2)) {
//                            tempCountry2 = tempCountry;
//                            countries.add(tempCountry2);
//                            //System.out.println(tempCountry2);
//                        }
                        System.out.println(asd.getCountry());
                        for (String c: countries) {
                            if(asd.getCountry().equalsIgnoreCase(c)){
                                countries.add(asd.getCountry());
                            }

                        }

                    });

            System.out.println(Color.YELLOW + "You have searched the following countries:\n" + Color.RESET);
            countries.forEach(System.out::println);


            System.out.println("You have searched the following countries:");
            for (RecordForStats c2 : data) {

                for (String c: countries2) {
                    if(c.equalsIgnoreCase(c2.getCountry())){
                        countries2.add(c2.getCountry());
                    }

                }
            }
            countries2.forEach(System.out::println);

            //countries.forEach(System.out::println);
            //countries2.forEach(System.out::println);
            System.out.print(LINE);

            // Calculate total deaths and cases
//            for (String country: countries) {
//                Arrays.stream(dataArray)
//                        .filter(country2 -> country2.getCountry().equals(country))
//                        .forEach(data1 -> {
//                            if(data1.getMonth() == 0){
//                                        tempCountry = country;
//                                        if(!tempCountry.equals(tempCountry2)){
//                                            tempCountry2 = tempCountry;
//
//                                            totalDeaths += data1.getDeaths();
//                                            totalCases += data1.getCases();
//
//
//                                        }
//                            }
//                        }
//                );
//            }

            final int[] LIST_MONTHS = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
//            for (int month: LIST_MONTHS ) {
//                Arrays.stream(dataArray)
//                        .filter(m -> m.getMonth() == month)
//                        .forEach(data -> {
//                            if(data.getMonth() > 0){
//                                tempMonth = month;
//                                if(!(tempMonth == tempMonth2)){
//                                    tempMonth2 = tempMonth;
//                                    totalMonths += data.getDeaths();
//
//                                    System.out.println(totalMonths);
//                                }
//                            }
//                        });
//            }

            // Display Stats
            System.out.println(Color.YELLOW + "Total Deaths and Case from all countries:"  + Color.RESET );
            System.out.println(Color.CYAN + TOTAL_DEATHS + Color.RESET + totalDeaths +
                    Color.CYAN + TOTAL_CASES  + Color.RESET + totalCases  );
            System.out.println(LINE);


        }else {
            System.out.println(Color.RED + ERR_NOT_STATS_FOUND + Color.RESET);
        }



    }

}
