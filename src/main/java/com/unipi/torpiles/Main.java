package com.unipi.torpiles;

import com.unipi.torpiles.Utils.UserInput;

import static com.unipi.torpiles.Utils.Constants.*;

public class Main {

    public static void main(String[] args) {

        System.out.println(TITLE);

        UserInput userInput = new UserInput();
        resultStats(userInput.choices());

    }


    private static void resultStats(int choice){

        switch (choice) {
            case 1 -> new UserInput().country();
            case 2 -> {
                new UserInput().months();
                new UserInput().country();
            }

            case 3 -> System.out.println("test 3");
            default -> System.out.println("Invalid choice.");
        }

    }

}
