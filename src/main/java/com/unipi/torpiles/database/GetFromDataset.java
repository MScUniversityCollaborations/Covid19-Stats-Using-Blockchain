package com.unipi.torpiles.database;

import com.google.gson.Gson;
import com.unipi.torpiles.models.Records;
import com.unipi.torpiles.utils.console.Color;
import com.unipi.torpiles.utils.console.ConsoleProgress;


import java.io.BufferedReader;
import java.io.FileReader;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.unipi.torpiles.utils.Constants.*;

public class GetFromDataset {
    private int totalDeaths = 0;
    private int totalCases = 0;

    private final List<String> monthsForResult = new ArrayList<>();

    public void searchByCountryAndMonths(List<String> months, String countryFromUser) {

        ConsoleProgress progress = new ConsoleProgress();
        progress.setDaemon(true);
        progress.start();

        try {

            // Create Gson instance
            Gson gson = new Gson();

            // Create a reader
            BufferedReader buffer = new BufferedReader(
                    new FileReader(PATH_COVID_DATA));

            // Create an array of JSON File
            Records[] recordArray = gson.fromJson(buffer, Records[].class);

            progress.join();

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

                System.out.println(
                        Color.BLUE + STATS + COUNTRY +
                                " from " + Month.of(month1).toString() +
                                " to " + Month.of(month2) + ": " + Color.RESET +
                        Color.CYAN + TOTAL_DEATHS + Color.RESET + totalDeaths +
                        Color.CYAN + TOTAL_CASES  + Color.RESET + totalCases
                );
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
}
