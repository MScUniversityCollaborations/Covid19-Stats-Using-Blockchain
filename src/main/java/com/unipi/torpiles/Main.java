package com.unipi.torpiles;

import com.unipi.torpiles.utils.Color;
import com.unipi.torpiles.utils.GetFromAPI;
import com.unipi.torpiles.utils.GetFromDataset;
import com.unipi.torpiles.utils.UserInput;

import java.io.IOException;

import static com.unipi.torpiles.utils.Constants.ERR_WRONG;
import static com.unipi.torpiles.utils.Constants.TITLE;

public class Main {

    public static void main(String[] args) throws IOException {

        System.out.println(Color.CYAN + TITLE + Color.RESET);

        resultStats(new UserInput().choices());

        //For Test
        //new UserInput().months();
        //new GetFromDataset().searchByCountryAndMonths();
    }


    private static void resultStats(String choice) throws IOException {

        switch (choice) {
            case "1" -> new GetFromAPI().searchByCountry(new UserInput().country());

            case "2" -> new GetFromDataset().searchByCountryAndMonths(
                        new UserInput().months(),
                        new UserInput().country());

            case "3" -> System.out.println("test 3");

            case "exit" -> {
                System.out.println(Color.RED + "Exit...." + Color.RESET);
                System.exit(0);
            }

            default -> System.out.println(Color.RED + ERR_WRONG + Color.RESET);
        }

        main(new String[] {"Call main again"});
    }
}
