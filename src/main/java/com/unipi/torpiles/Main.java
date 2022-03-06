package com.unipi.torpiles;

import com.unipi.torpiles.database.DBManager;
import com.unipi.torpiles.database.GetFromDatabase;
import com.unipi.torpiles.utils.console.Color;
import com.unipi.torpiles.database.GetFromAPI;
import com.unipi.torpiles.database.GetFromDataset;
import com.unipi.torpiles.utils.UserInput;

import java.io.IOException;
import java.sql.SQLException;

import static com.unipi.torpiles.utils.Constants.*;

public class Main {

   static DBManager dbManagerInstance = DBManager.getInstance();

    public static void main(String[] args) throws IOException, InterruptedException, SQLException {

        System.out.println(
                Color.BLUE + LINE + Color.RESET +
                Color.CYAN + TITLE + Color.RESET);

        dbManagerInstance.initializeTable();

        resultStats(new UserInput().choices());

        // For Test
        // new UserInput().months();
        // new GetFromDataset().searchByCountryAndMonths();
        // new GetFromDatabase().resultStats();
    }

    private static void resultStats(String choice) throws IOException, InterruptedException {

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
        Thread.sleep(1666);
        //main(new String[] {"Call main again"});
    }
}
