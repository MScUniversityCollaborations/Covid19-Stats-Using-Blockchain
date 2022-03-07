package com.unipi.torpiles.database;

import com.google.gson.Gson;
import com.unipi.torpiles.utils.console.Color;
import com.unipi.torpiles.utils.console.ConsoleProgress;
import com.unipi.torpiles.utils.UserInput;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Map;

import static com.unipi.torpiles.utils.Constants.*;


public class GetFromAPI {

    public void searchByCountry(String country) throws IOException, InterruptedException {

        ConsoleProgress progress = new ConsoleProgress();
        progress.setTimeSleep(850);
        progress.setDaemon(true);
        progress.start();

        //API URL for the connection
        URL apiURL = new URL(URL_COUNTRY_API + country);

        try {

            //Open Connection
            URLConnection urlConnect = apiURL.openConnection();

            //Create  buffer reader
            BufferedReader  buffer = new BufferedReader(
                    new InputStreamReader(
                            urlConnect.getInputStream()));

            progress.join();

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
                    Map<?,?> result = gson.fromJson(resultsFromAPI, Map.class);

                    // Create new map for data in "data"
                    Map<?,?> resultData = (Map<?, ?>) result.get("data");

                    //Check if country exists
                    if(resultData.get("location") != null){

                        String location = resultData.get("location").toString();
                        long confirmed = Double.valueOf(String.valueOf(resultData.get("confirmed"))).longValue();
                        long deaths = Double.valueOf(String.valueOf(resultData.get("deaths"))).longValue();
                        long recovered = Double.valueOf(String.valueOf(resultData.get("recovered"))).longValue();
                        long active = Double.valueOf(String.valueOf(resultData.get("active"))).longValue();

                        // Display result in console
                        System.out.println(
                            Color.CYAN + STATS + Color.RESET + location +
                            Color.CYAN + TOTAL_CASES + Color.RESET + confirmed +
                            Color.CYAN + TOTAL_DEATHS + Color.RESET + deaths +
                            Color.CYAN + TOTAL_RECOVERED + Color.RESET + recovered +
                            Color.CYAN + TOTAL_ACTIVES + Color.RESET + active +
                            Color.CYAN + LAST_UPDATE + Color.RESET+ result.get("dt")
                        );

                        DBManager.getInstance().addNewEntry(
                                location,
                                (int) confirmed,
                                (int) deaths,
                                (int) recovered,
                                (int) active,
                                0);

                    }else {
                        System.out.print(Color.RED + ERR_WRONG + Color.RESET);
                        System.out.println(Color.RED + ERR_NOT_FOUND_COUNTRY + Color.RESET);
                        searchByCountry(new UserInput().country());
                    }
                } catch (Exception ex) {
                    System.err.println(ERR_WRONG);
                    ex.printStackTrace();
                }
            }
            buffer.close();

        }catch(IOException | InterruptedException e){
                System.out.println(Color.RED + ERR_INTERNET_CONN + Color.RESET);
                e.printStackTrace();
        }
    }
}
