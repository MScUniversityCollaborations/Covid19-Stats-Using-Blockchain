package com.unipi.torpiles.database;

import com.unipi.torpiles.models.Statistic;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.unipi.torpiles.utils.Constants.DB_URL;

public class GetFromDatabase {

    public void connection() throws SQLException {

        String sql = "SELECT LOCATION, CONFIRMED, DEATHS, RECOVERED, ACTIVE, MONTH FROM STATS";

        Statistic data;
        ArrayList<Statistic> listData = new ArrayList<>();
        List<Statistic> asd;
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next())
            {
                if (!rs.getString(1).isEmpty()) {

                     data = new Statistic(
                            rs.getString(1),
                            rs.getInt(2),
                            rs.getInt(3),
                            rs.getInt(6));

                    listData.add(data);

                    //System.out.println(rs.getString(1));
                    //System.out.println(rs.getInt(2));
                    //System.out.println(rs.getInt(3));
                    //System.out.println(rs.getInt(4));
                    //System.out.println(rs.getInt(5));
                    //System.out.println(rs.getInt(6));
                }
            }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        listData.stream().distinct().forEach(System.out::println);

        }
}
