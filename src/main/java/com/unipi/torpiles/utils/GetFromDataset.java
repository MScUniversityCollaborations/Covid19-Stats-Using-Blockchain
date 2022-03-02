package com.unipi.torpiles.utils;

import com.google.gson.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.unipi.torpiles.utils.Constants.PATH_COVID_DATA;

public class GetFromDataset {

    public void searchByCountryAndMonths(/*String country, List<String> months*/){

        try {

            // create Gson instance
            Gson gson = new Gson();

            // create a reader
            BufferedReader buffer = new BufferedReader(
                    new FileReader(PATH_COVID_DATA));

            //convert the json string back to object
            Map<?, ?> result = gson.fromJson(buffer, Map.class);
            Map<?,?> resultData = (Map<?, ?>) result.get("records");

            System.out.println(resultData);
            // print map entries
//          for (Map.Entry<?, ?> entry : result.entrySet()) {
//                System.out.println(entry.getKey() + "=" + entry.getValue());
//
//            }

           String result2 = String.valueOf(result.values().
                    stream() // converting into Stream
                    .filter("records"::equals).toList());;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
