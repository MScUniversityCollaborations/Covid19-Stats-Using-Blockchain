package com.unipi.torpiles.utils;

import com.unipi.torpiles.utils.console.Color;

public final class Constants {

    // Display
    public static final String TITLE = "\nCovid-19 Stats\n";
    public static final String LINE = "-------------------------------------------------------------------------\n";
    public static final String STATS = "\nStats for:\040";
    public static final String TOTAL_CASES = "\n-Total Cases:\040";
    public static final String TOTAL_DEATHS = "\n-Total Deaths:\040";
    public static final String TOTAL_RECOVERED = "\n-Total Recovered:\040";
    public static final String TOTAL_ACTIVES = "\n-Total Active:\040";
    public static final String LAST_UPDATE = "\n-Last Update:\040";
    public static final String CASES = "\040|Cases:\040";
    public static final String DEATHS = "\040|Deaths:\040";
    public static final String COUNTRY = "Country:\040";


    // For Search
    public static final String SEARCH_BY_COUNTRY = "Search results by country.\n";
    public static final String SEARCH_BY_MONTHS_AND_COUNTRY = "Search for monthly results for a specific year by country.\n";
    public static final String VIEW_STATISTICS = "View statistics.\n";

    // Database
    public static final String TABLE_STATS = "STATS";
    public static final String COLUMN_LOCATION = "LOCATION";
    public static final String COLUMN_TS = "TS";
    public static final String COLUMN_CONFIRMED = "CONFIRMED";
    public static final String COLUMN_DEATHS = "DEATHS";
    public static final String COLUMN_ACTIVE = "ACTIVE";
    public static final String COLUMN_RECOVERED = "RECOVERED";
    public static final String COLUMN_MONTH = "MONTH";
    public static final String COLUMN_PREV_ID = "PREV_ID";
    public static final String COLUMN_HASH = "HASH";
    public static final String COLUMN_PREV_HASH = "PREV_HASH";
    public static final String COLUMN_BLOCK_TS = "BLOCK_TS";
    public static final String COLUMN_NONCE = "NONCE";

    // URL and PATH
    public static final String URL_COUNTRY_API = "https://covid2019-api.herokuapp.com/v2/country/";
    public static final String PATH_COVID_DATA = "src/main/resources/dataset/covidData.json";
    public static final String DB_URL = "jdbc:sqlite:covid19_stats.db";

    // Err Messages
    public static final String ERR_WRONG = "Something went wrong.\n";
    public static final String ERR_NOT_FOUND_COUNTRY = "Sorry, no country data found. Try again\n";
    public static final String ERR_INTERNET_CONN = "Oh no! No Internet Connection\n";
    public static final String ERR_INVALID_IMPORT = "Invalid import. Try again.\n";
    public static final String ERR_NOT_STATS_FOUND = """
                                                       Sorry, we cannot provide any results.
                                                       Please search for a country [choice 1]
                                                       or monthly stats [choice 2].

                                                       """;

    // User Input Messages
    public static final String MESS_INPUT_MONTHS = """
                            Input time period of months to display results:
                            """+ Color.WHITE + """
                            Examples:
                            - 1-3 or 3-1 is January to March etc.
                            - 1-1 is a single month (January).""" + Color.RESET + """
                            """;

    public static final String MESS_INPUT_CHOICE = """ 
                    """ + LINE + """
                    - Press 1:""" + SEARCH_BY_COUNTRY  + """
                    - Press 2:""" + SEARCH_BY_MONTHS_AND_COUNTRY + """
                    - Press 3:""" + VIEW_STATISTICS + """
                    - Write "exit" for exit:
                    """ + LINE + """
                """;

    // Static List
    final static String[] LIST_MONTHS = {"1","2","3","4","5","6","7","8","9","10","11","12"};
    final static String[] LIST_CHOICES = {"1", "2","3","exit"};
}
