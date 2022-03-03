package com.unipi.torpiles.utils;

import com.google.gson.Gson;
import com.unipi.torpiles.models.Records;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.unipi.torpiles.utils.Constants.PATH_COVID_DATA;

public class GetFromDataset {
    private int totalDeaths = 0 ;
    private int totalCases = 0;
    
    List<String> monthsForResult = new ArrayList<>();

    public void searchByCountryAndMonths(/*String country, List<String> months*/){

        try {

            // Create Gson instance
            Gson gson = new Gson();

            // Create a reader
            BufferedReader buffer = new BufferedReader(
                    new FileReader(PATH_COVID_DATA));

            // Create an array of JSON File
            Records[] recordArray = gson.fromJson(buffer, Records[].class);

            String[] months = {"1","12"}; // array for debug
            int month1 = Integer.parseInt(months[0]);
            int month2 = Integer.parseInt(months[1]);
            int i = 0;
            while(month1 + i <= month2){
                monthsForResult.add(Integer.toString(month1 + i));
                i++;
            }

            Arrays.stream(recordArray)
                    .filter(country -> country.getCountry().equals("Greece"))
                    .forEach(data->{
                        for(String month : monthsForResult){
                            if(data.getMonth().equals(month)){
                                totalDeaths += data.getDeaths();
                                totalCases  += data.getCases();
                            }
                        }
                        });

            System.err.println("Total deaths : " + totalDeaths);
            System.err.println("Total cases  :" + totalCases);

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
