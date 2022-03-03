package com.unipi.torpiles;

import com.unipi.torpiles.utils.GetFromAPI;
import com.unipi.torpiles.utils.GetFromDataset;
import com.unipi.torpiles.utils.UserInput;

import java.io.IOException;
import java.util.Arrays;

import static com.unipi.torpiles.utils.Constants.*;

public class Main {

    public static void main(String[] args) throws IOException {

        System.out.println(TITLE);

        UserInput userInput = new UserInput();
        //resultStats(userInput.choices());

        //For Test
        //new UserInput().months();
        new GetFromDataset().searchByCountryAndMonths();
    }


    private static void resultStats(String choice) throws IOException {

        switch (choice) {
            case "1" -> new GetFromAPI().searchByCountry(new UserInput().country());

            case "2" -> {
                new UserInput().months();
                new UserInput().country();
            }
            case "3" -> System.out.println("test 3");
            default -> System.out.println(ERR_WRONG);
        }
    }
}
