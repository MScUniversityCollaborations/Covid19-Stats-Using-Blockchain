package com.unipi.torpiles.database;

import com.unipi.torpiles.models.Statistic;
import com.unipi.torpiles.utils.Constants;
import com.unipi.torpiles.utils.blockchain.Block;
import com.unipi.torpiles.utils.blockchain.BlockChain;

import java.sql.*;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

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
             *  DT: Date Time
             *  TS: TimeStamp in Long
             *
             * Creating the Statistics table if it doesn't exist. */
            String table = "CREATE TABLE IF NOT EXISTS STATS"
                            + "(ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
                            + "LOCATION VARCHAR(255) NOT NULL,"
                            + "TS TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                            + "CONFIRMED BIGINT NOT NULL,"
                            + "DEATHS BIGINT NOT NULL,"
                            + "RECOVERED BIGINT NOT NULL,"
                            + "ACTIVE BIGINT NOT NULL,"
                            + "PREV_ID INTEGER,"
                            + "HASH TEXT,"
                            + "PREV_HASH TEXT,"
                            + "BLOCK_TS TEXT,"
                            + "NONCE TEXT)";

            Statement statement = connection.createStatement();
            statement.executeUpdate(table);
            // Closing the statement.
            statement.close();

            System.out.println("Table '"+Constants.TABLE_STATS+"' has been created successfully!");

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
                String hash = rs2.getString("hash");
                String prevHash = rs2.getString("prevHash");
                long blockTimestamp = Long.decode(rs2.getString
                        ("blockTimestamp"));
                int nonce = rs2.getInt("nonce");

                // Data
                String location = rs2.getString("location");
                Integer confirmed = rs2.getInt("confirmed");
                Integer deaths = rs2.getInt("deaths");
                Integer recovered = rs2.getInt("recovered");
                Integer active = rs2.getInt("active");

                String data = new Statistic(location, confirmed, deaths,
                        recovered, active).jsonMaker();

                Block currentBlock = new Block("",prevHash);
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

    public void addNewEntry(String location, int confirmed, int deaths, int recovered, int active) {
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
                    preparedStatement.setInt(7, prevId);
                    preparedStatement.setString(8, BlockChain.blocklist.get(BlockChain.blocklist.size()-1).hash);
                    preparedStatement.setString(9, BlockChain.blocklist.get(BlockChain.blocklist.size()-1).previousHash);
                    preparedStatement.setTimestamp(10, new Timestamp(BlockChain.blocklist.get(BlockChain.blocklist.size()-1).getTimeStamp()));
                    preparedStatement.setInt(11, BlockChain.blocklist.get(BlockChain.blocklist.size()-1).getNonce());

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
                    System.out.println("Insert to database completed!");
                } catch (SQLException e) {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, e);
                }
            }

            // Closing the connections.
            rs.close();
            statement.close();
            connection.close();
            System.out.println("Insertion to database completed!");
        } catch (SQLException e) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
