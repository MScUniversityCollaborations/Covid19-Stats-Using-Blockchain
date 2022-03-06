package com.unipi.torpiles.database;

import com.unipi.torpiles.models.Statistic;
import com.unipi.torpiles.utils.Constants;
import com.unipi.torpiles.utils.blockchain.Block;
import com.unipi.torpiles.utils.blockchain.BlockChain;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.unipi.torpiles.utils.Constants.DB_URL;

public class DBManager extends BlockChain {

    private DBManager() {}

    private static final class InstanceHolder {
        // DbManager Instance
        private static final DBManager dbManager = new DBManager();
    }

    public static DBManager getInstance(){
        return InstanceHolder.dbManager;
    }

    public Connection connect() {
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(Constants.DB_URL);
        } catch (Exception e) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return connection;
    }

    public void initializeTable() {
        try {
            Connection connection = connect();

            /* Legend:
             *  Location: 56 characters because that's what the longest country name has.
             *  TS: TimeStamp in Long
             *  Month: Month number, so 2 characters.
             *
             * Creating the Statistics table if it doesn't exist. */
            String table = "CREATE TABLE IF NOT EXISTS '"+Constants.TABLE_STATS+"'"
                            + "(ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
                            + "LOCATION VARCHAR(56) NOT NULL,"
                            + "TS TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                            + "CONFIRMED BIGINT NOT NULL,"
                            + "DEATHS BIGINT NOT NULL,"
                            + "RECOVERED BIGINT,"
                            + "ACTIVE BIGINT,"
                            + "PREV_ID INTEGER,"
                            + "HASH TEXT,"
                            + "PREV_HASH TEXT,"
                            + "BLOCK_TS TEXT,"
                            + "NONCE TEXT,"
                            + "MONTH INTEGER)";

            Statement statement = connection.createStatement();
            statement.executeUpdate(table);
            // Closing the statement.
            statement.close();

            String sql = "SELECT * FROM STATS WHERE id = (SELECT MAX(id) FROM STATS ORDER BY id ASC)";
            statement = connection.createStatement();

            ResultSet rs2 = statement.executeQuery(sql);

            // If DB is empty
            if (!rs2.next()) {
                // Initializing the empty DB with 1 entry of a fake statistic.
                Statistic statistic = new Statistic (
                        "",
                        0,
                        0,
                        0,
                        0,
                        new Date().getTime()
                );
                Block genesisBlock = new Block(statistic.jsonMaker(),
                        UUID.randomUUID().toString());
                genesisBlock.mineBlock(difficulty);
                blocklist.add(genesisBlock);

                int prevIdInit = 0;
                String sqlInsert = "INSERT INTO '"+Constants.TABLE_STATS+"' (" +
                        "LOCATION, TS, CONFIRMED, DEATHS, RECOVERED, ACTIVE, PREV_ID, HASH, PREV_HASH, BLOCK_TS, NONCE) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);

                    preparedStatement.setString(1, statistic.location);
                    preparedStatement.setTimestamp(2, new Timestamp(statistic.ts));
                    preparedStatement.setInt(3, statistic.confirmed);
                    preparedStatement.setInt(4, statistic.deaths);
                    preparedStatement.setInt(5, statistic.recovered);
                    preparedStatement.setInt(6, statistic.active);
                    preparedStatement.setInt(7, prevIdInit);
                    preparedStatement.setString(8, genesisBlock.hash);
                    preparedStatement.setString(9, genesisBlock.previousHash);
                    preparedStatement.setTimestamp(10, new Timestamp(genesisBlock.getTimeStamp()));
                    preparedStatement.setInt(11, genesisBlock.getNonce());

                    int count = preparedStatement.executeUpdate();
                    if (count > 0) {
                        System.out.println(count+" record updated");
                    }

                    // Closing the statement.
                    preparedStatement.close();
                    System.out.println("Insertion to database completed!");
                } catch (SQLException e) {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, e);
                }
            }
            // If DB is not empty, we just read the latest DB entry and add it in our arraylist, so we can continue our work
            else
            {
                String hash = rs2.getString(Constants.COLUMN_HASH);
                String prevHash = rs2.getString(Constants.COLUMN_PREV_HASH);
                long blockTimestamp = Long.decode(rs2.getString
                        (Constants.COLUMN_BLOCK_TS));
                int nonce = rs2.getInt(Constants.COLUMN_NONCE);

                // Data
                String location = rs2.getString(Constants.COLUMN_LOCATION);
                Integer confirmed = rs2.getInt(Constants.COLUMN_CONFIRMED);
                Integer deaths = rs2.getInt(Constants.COLUMN_DEATHS);
                Integer recovered = rs2.getInt(Constants.COLUMN_RECOVERED);
                Integer active = rs2.getInt(Constants.COLUMN_ACTIVE);

                String data = new Statistic(location, confirmed, deaths,
                        recovered, active).jsonMaker();

                Block currentBlock = new Block("", prevHash);
                currentBlock.setTimeStamp(blockTimestamp);
                currentBlock.setNonce(nonce);
                currentBlock.setData(data);
                currentBlock.hash = hash;
                blocklist.add(currentBlock);
            }
            rs2.close();

            // Closing the connections.
            statement.close();
            connection.close();

        } catch (SQLException e) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void addNewEntry(String location, int confirmed, int deaths, int recovered, int active, int month) {
        try {
            Statistic statistic = new Statistic (
                    location,
                    confirmed,
                    deaths,
                    recovered,
                    active,
                    new Date().getTime()
            );
            BlockChain.addBlock(statistic.jsonMaker());

            Connection connection = connect();
            Statement statement = connection.createStatement();

            String sql2 = "SELECT MAX(id) FROM '"+Constants.TABLE_STATS+"' WHERE LOCATION = '"+statistic.location+"' ORDER BY id ASC";

            ResultSet rs = statement.executeQuery(sql2);
            if (rs.next()) {
                int prevId = rs.getInt(1);

                String sqlInsert = "INSERT INTO '"+Constants.TABLE_STATS+"' (" +
                        "LOCATION, TS, CONFIRMED, DEATHS, RECOVERED, ACTIVE, PREV_ID, HASH, PREV_HASH, BLOCK_TS, NONCE, MONTH) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);

                    preparedStatement.setString(1, statistic.location);
                    preparedStatement.setTimestamp(2, new Timestamp(statistic.ts));
                    preparedStatement.setInt(3, statistic.confirmed);
                    preparedStatement.setInt(4, statistic.deaths);
                    preparedStatement.setInt(5, statistic.recovered);
                    preparedStatement.setInt(6, statistic.active);
                    preparedStatement.setInt(7, prevId);
                    preparedStatement.setString(8, BlockChain.blocklist.get(BlockChain.blocklist.size()-1).hash);
                    preparedStatement.setString(9, BlockChain.blocklist.get(BlockChain.blocklist.size()-1).previousHash);
                    preparedStatement.setTimestamp(10, new Timestamp(BlockChain.blocklist.get(BlockChain.blocklist.size()-1).getTimeStamp()));
                    preparedStatement.setInt(11, BlockChain.blocklist.get(BlockChain.blocklist.size()-1).getNonce());
                    preparedStatement.setInt(12, month);

                    int count = preparedStatement.executeUpdate();
                    if (count > 0) {
                        System.out.println(count+" record updated");
                    }

                    // Closing the statement.
                    preparedStatement.close();
                    System.out.println("Insert to database completed!");
                } catch (SQLException e) {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, e);
                }
            }
            else {
                String sqlInsert = "INSERT INTO '"+Constants.TABLE_STATS+"' (" +
                        "LOCATION, TS, CONFIRMED, DEATHS, RECOVERED, ACTIVE, HASH, PREV_HASH, BLOCK_TS, NONCE) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);

                    preparedStatement.setString(1, statistic.location);
                    preparedStatement.setTimestamp(2, new Timestamp(statistic.ts));
                    preparedStatement.setInt(3, statistic.confirmed);
                    preparedStatement.setInt(4, statistic.deaths);
                    preparedStatement.setInt(5, statistic.recovered);
                    preparedStatement.setInt(6, statistic.active);
                    preparedStatement.setString(7, BlockChain.blocklist.get(BlockChain.blocklist.size()-1).hash);
                    preparedStatement.setString(8, BlockChain.blocklist.get(BlockChain.blocklist.size()-1).previousHash);
                    preparedStatement.setTimestamp(9, new Timestamp(BlockChain.blocklist.get(BlockChain.blocklist.size()-1).getTimeStamp()));
                    preparedStatement.setInt(10, BlockChain.blocklist.get(BlockChain.blocklist.size()-1).getNonce());

                    int count = preparedStatement.executeUpdate();
                    if (count > 0) {
                        System.out.println(count+" record updated");
                    }

                    // Closing the statement.
                    preparedStatement.close();
                    System.out.println("Insertion to database completed!");
                } catch (SQLException e) {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, e);
                }
            }

            // Closing the connections.
            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public ArrayList<String> dataForStats() {

        String sql = "SELECT LOCATION, CONFIRMED, DEATHS, MONTH FROM STATS";

        ArrayList<String> listData = new ArrayList<>();
        String data;

        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery(sql);

            while (result.next())
            {
                if(!result.isFirst()){

                    String location = result.getString(1);
                    Integer confirmed = result.getInt(2);
                    Integer deaths = result.getInt(3);
                    Integer month = result.getInt(4);

                    data = new Statistic(
                            location,
                            confirmed,
                            deaths,
                            month).jsonMaker();

                    listData.add(data);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return listData;
    }

}
