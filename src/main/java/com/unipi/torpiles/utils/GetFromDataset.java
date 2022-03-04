package com.unipi.torpiles.utils;

import com.google.gson.Gson;
import com.unipi.torpiles.models.Records;


import java.io.BufferedReader;
import java.io.FileReader;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.unipi.torpiles.utils.Constants.ERR_NOT_FOUND_COUNTRY;
import static com.unipi.torpiles.utils.Constants.PATH_COVID_DATA;

public class GetFromDataset {
    private int totalDeaths = 0;
    private int totalCases = 0;

    private final List<String> monthsForResult = new ArrayList<>();

    public void searchByCountryAndMonths(List<String> months, String countryFromUser) {

        try {

            // Create Gson instance
            Gson gson = new Gson();

            // Create a reader
            BufferedReader buffer = new BufferedReader(
                    new FileReader(PATH_COVID_DATA));

            // Create an array of JSON File
            Records[] recordArray = gson.fromJson(buffer, Records[].class);

            // Calculate months from search
            // ex: if user input: 3-8 then monthsForResult = {3,4,5,6,7,8}
            final int month1 = Integer.parseInt(months.get(0));
            final int month2 = Integer.parseInt(months.get(1));
            int i = 0;
            while (month1 + i <= month2) {
                monthsForResult.add(Integer.toString(month1 + i));
                i++;
            }

            final String COUNTRY = countryFromUser.substring(0,1).toUpperCase()
                    + countryFromUser.substring(1).toLowerCase();

            // Search if country from user input exist
            boolean existCountry = Arrays.stream(recordArray)
                    .anyMatch(records -> records.getCountry().equals(COUNTRY));



            if(!existCountry){
                System.out.println(ERR_NOT_FOUND_COUNTRY);
            } else {
                Arrays.stream(recordArray)
                        .filter(country -> country.getCountry().equals(COUNTRY))
                        .forEach(data -> {
                            for (String month : monthsForResult) {
                                if (data.getMonth().equals(month)) {
                                    totalDeaths += data.getDeaths();
                                    totalCases += data.getCases();
                                }
                            }
                        });

                // Display information
                System.out.print(Color.BLUE + "Results for " + COUNTRY + Color.RESET);
                System.out.println(Color.BLUE + " from " + Month.of(month1).toString() +
                        " to " + Month.of(month2) + ": " + Color.RESET);
                System.out.println(Color.CYAN + "Total deaths: " + Color.RESET + totalDeaths);
                System.out.println(Color.CYAN + "Total cases: " + Color.RESET + totalCases);
            }

//            Arrays.stream(recordArray)
//                    .filter(country -> country.getCountry().equals("Greece"))
//                    .forEach(data->{
//                        totalDeaths += data.getDeaths();
//                        totalCases += data.getCases();
//                    });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    String toCamelCase(String inputVal) {

            // Empty strings should be returned as-is.

//            if (inputVal.length() == 0) return "";
//
//            // Strings with only one character uppercased.
//
//            if (inputVal.length() == 1) return inputVal.toUpperCase();
//
//            // Otherwise uppercase first letter, lowercase the rest.

            return inputVal.substring(0,1).toUpperCase()
                    + inputVal.substring(1).toLowerCase();
        }
}
