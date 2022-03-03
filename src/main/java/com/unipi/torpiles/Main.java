package com.unipi.torpiles;

import com.unipi.torpiles.utils.GetFromAPI;
import com.unipi.torpiles.utils.GetFromDataset;
import com.unipi.torpiles.utils.UserInput;
import com.unipi.torpiles.utils.Color;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;


import static com.unipi.torpiles.utils.Constants.*;

public class Main {

    public static void main(String[] args) throws IOException {

        System.out.println(Color.CYAN + TITLE + Color.RESET);

        UserInput userInput = new UserInput();
        resultStats(userInput.choices());


        //For Test
        //new UserInput().months();
        //new GetFromDataset().searchByCountryAndMonths();
    }


    private static void resultStats(String choice) throws IOException {

        switch (choice) {
            case "1" -> new GetFromAPI().searchByCountry(new UserInput().country());

            case "2" -> {
                new GetFromDataset().searchByCountryAndMonths(
                        new UserInput().country(),
                        new UserInput().months());
            }
            case "3" -> System.out.println("test 3");
            case "exit" -> {
                System.out.println(Color.RED + "Exit...." + Color.RESET);
                System.exit(0);
            }
            default -> System.out.println(Color.RED + ERR_WRONG + Color.RESET);
        }
    }
}
