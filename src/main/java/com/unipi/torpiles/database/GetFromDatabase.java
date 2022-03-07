package com.unipi.torpiles.database;

import com.unipi.torpiles.models.Country;
import com.unipi.torpiles.models.RecordForStats;
import com.unipi.torpiles.utils.console.Color;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.unipi.torpiles.utils.Constants.*;
import static java.util.Collections.reverse;

public class GetFromDatabase {
    long totalDeaths = 0 ;
    long totalCases = 0;
    long totalMonthlyCases = 0 ;
    long totalMonthlyDeaths = 0;

    public void resultStats() {

        System.out.println(Color.CYAN + "COVID STATS FROM DATABASE" + Color.RESET);
        RecordForStats[] dataArray = DBManager.getInstance().dataForStats().toArray(new RecordForStats[0]);
        if (!DBManager.getInstance().dataForStats().isEmpty()) {

            ArrayList<String> searchedCountries = new ArrayList<>();
            ArrayList<String> checkCountries = new ArrayList<>();
            List<Country> countries = new ArrayList<>();

            System.out.print(LINE);
            // All Countries
            Arrays.stream(dataArray)
                    .sorted((s1,s2)-> 0)
                    .forEach(c -> {
                        String country = c.getCountry();
                        if (searchedCountries.isEmpty()) {
                            searchedCountries.add(country);
                            countries.add(new Country(country,c.getDeaths(),c.getCases()));
                        }
                        else {
                            if (!containsCaseInsensitive(country, searchedCountries)) {
                                searchedCountries.add(country);
                                countries.add(new Country(country,c.getDeaths(),c.getCases()));
                            }
                        }
                    }
                    );

            System.out.println(Color.YELLOW + "You have searched the following countries:\n" + Color.RESET);
            searchedCountries.forEach(System.out::println);

            System.out.print(LINE);

            // Calculate total deaths and cases
            System.out.println(Color.YELLOW + "Deaths and Cases for each country:\n"  + Color.RESET );
            Arrays.stream(dataArray).forEach(c -> {
                if(c.getMonth() == 0){
                    String finalCountry = c.getCountry();
                    if (checkCountries.isEmpty()) {
                        totalDeaths += c.getDeaths();
                        totalCases += c.getCases();
                        System.out.print( Color.CYAN + COUNTRY + Color.RESET + c.getCountry());
                        System.out.print( Color.CYAN + CASES + Color.RESET + c.getCases());
                        System.out.println( Color.CYAN + DEATHS + Color.RESET + c.getDeaths());
                        checkCountries.add(finalCountry);
                    }
                    else {
                        if (!containsCaseInsensitive(finalCountry, checkCountries)) {
                            totalDeaths += c.getDeaths();
                            totalCases += c.getCases();
                            System.out.print( Color.CYAN + COUNTRY + Color.RESET + c.getCountry());
                            System.out.print( Color.CYAN + CASES + Color.RESET + c.getCases());
                            System.out.println( Color.CYAN + DEATHS + Color.RESET + c.getDeaths());
                            checkCountries.add(finalCountry);
                        }
                    }
                }
            });

            System.out.print(LINE);
            // Display Stats
            System.out.println(Color.YELLOW + "Total Deaths and Cases from all countries:"  + Color.RESET );
            System.out.println(Color.CYAN + TOTAL_DEATHS + Color.RESET + totalDeaths +
                    Color.CYAN + TOTAL_CASES  + Color.RESET + totalCases );

            System.out.print(LINE);

            System.out.println(Color.YELLOW + "The countries with the most and the fewest deaths-cases:\n"  + Color.RESET );
            List<Country> sortedListDeaths = countries.stream()
                    .sorted(Comparator.comparingInt(o -> o.deaths)).toList();

            List<Country> sortedListCases = countries.stream()
                    .sorted(Comparator.comparingInt(o -> o.cases)).toList();
            //System.out.println(sortedList);
            String minDeaths = String.valueOf(sortedListDeaths.stream().findFirst().stream().toList());
            String maxDeaths = String.valueOf(sortedListDeaths.get(sortedListDeaths.size() - 1));
            String minCases = String.valueOf(sortedListCases.stream().findFirst().stream().toList());
            String maxCases = String.valueOf(sortedListCases.get(sortedListCases.size() - 1));

            minDeaths = minDeaths.replace("]","");
            minDeaths = minDeaths.replace("[","");
            minCases = minCases.replace("]","");
            minCases = minCases.replace("[","");

            System.out.println(Color.CYAN + "More deaths : "  + Color.RESET + maxDeaths);
            System.out.println(Color.CYAN + "More cases : "  + Color.RESET + maxCases + "\n");
            System.out.println(Color.CYAN + "Fewer deaths : "  + Color.RESET + minDeaths );
            System.out.println(Color.CYAN + "Fewer cases : "  + Color.RESET + minCases);


            System.out.print(LINE);

            Double averageCase = countries.stream().collect(Collectors.averagingInt(s->s.cases));
            Double averageDeaths = countries.stream().collect(Collectors.averagingInt(s->s.deaths));
            System.out.println(Color.YELLOW + "Deaths and Cases Average:\n"  + Color.RESET );
            System.out.println(Color.CYAN + "Average Deaths: "  + Color.RESET + averageDeaths);
            System.out.println(Color.CYAN + "Average Cases: "  + Color.RESET + averageCase);

            System.out.println(LINE);


        }else {
            System.out.println(Color.RED + ERR_NOT_STATS_FOUND + Color.RESET);
        }



    }

    public boolean containsCaseInsensitive(String s, List<String> l) {
        for (String string : l) {
            if (string.equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }

}
