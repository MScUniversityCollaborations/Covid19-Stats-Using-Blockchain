package com.unipi.torpiles;

import com.unipi.torpiles.Utils.UserInput;

public class Main {

    public static void main(String[] args) {
        UserInput userInput = new UserInput();

//        String country = userInput.country();
//        String months = userInput.months();
        int choice = userInput.choices();
        System.out.println(choice);
    }

}
