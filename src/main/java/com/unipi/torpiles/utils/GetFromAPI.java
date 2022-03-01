package com.unipi.torpiles.utils;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import static com.unipi.torpiles.utils.Constants.URL_COUNTRY_API;


public class GetFromAPI {

    public void searchByCountry(String country) throws IOException {

        //API URL for the connection
        URL apiURL = new URL(URL_COUNTRY_API + country);

        //Open Connection
        URLConnection urlConnect = apiURL.openConnection();

        //Create  buffer reader
        BufferedReader buffer = new BufferedReader(
                new InputStreamReader(
                        urlConnect.getInputStream()));


        String resultsFromAPI;
        while ((resultsFromAPI = buffer.readLine()) != null){

            //System.err.println("Results: " + resultsFromAPI + "\n");

            //  Results from API:
            //  {"data":
            //      {"location":"string","confirmed":number,"deaths":number,"recovered":0,"active":0},
            //  "dt":"date",
            //  "ts":timestamp}

            try {
                // Create Gson instance
                Gson gson = new Gson();

                // Convert JSON file to map
                Map<?, ?> result = gson.fromJson(resultsFromAPI, Map.class);

                // Create new map for data in "data"
                Map<?,?> resultData = (Map<?, ?>) result.get("data");

                // Print result in console
                System.out.println("""
                    Stats for:\040""" +resultData.get("location") +  """
                    \n-Confirmed:\040""" +resultData.get("confirmed")  + """
                    \n-Deaths:\040""" + resultData.get("deaths") + """
                    \n-Last Update:\040""" + result.get("dt") + """
                """
                );
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        buffer.close();
    }
}
