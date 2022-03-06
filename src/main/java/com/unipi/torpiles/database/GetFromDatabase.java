package com.unipi.torpiles.database;

import com.unipi.torpiles.models.Statistic;
import com.unipi.torpiles.utils.Constants;
import com.unipi.torpiles.utils.console.Color;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.unipi.torpiles.utils.Constants.DB_URL;
import static com.unipi.torpiles.utils.Constants.ERR_NOT_STATS_FOUND;

public class GetFromDatabase {


    public void resultStats(){

        ArrayList<String> data = DBManager.getInstance().dataForStats();

        if(!data.isEmpty()){
            data.forEach(System.out::println);

            List<String> country = data.stream().filter(s -> s.equals("country")).toList();;
            country.forEach(System.out::println);

        }else {
            System.out.println(Color.RED + ERR_NOT_STATS_FOUND + Color.RESET);
        }





    }

}
