package com.unipi.torpiles.database;

import com.unipi.torpiles.models.Statistic;
import com.unipi.torpiles.utils.BlockChain;
import com.unipi.torpiles.utils.Constants;

import java.sql.*;
import java.util.Date;
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
            connection = DriverManager.getConnection(Constants.DERBY_URL);
        } catch (SQLException e) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return connection;
    }

    public void initializeTable() {
        try {
            Connection connection = connect();

            DatabaseMetaData dbMetaData = connection.getMetaData();

            ResultSet rs = dbMetaData.getTables(null, null, Constants.TABLE_STATS,null);
            if (rs.next()) {
                System.out.println("Table " + rs.getString("TABLE_NAME") + " already exists, skipping creating it!");
            }
            else {
                /* Legend:
                 *  DT: Date Time
                 *  TS: TimeStamp in Long
                 *
                 * Creating the Statistics table if it doesn't exist. */
                String table = "CREATE TABLE STATS"
                        + "(ID INTEGER NOT NULL PRIMARY KEY,"
                        + "LOCATION VARCHAR(255) NOT NULL,"
                        + "TS TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                        + "CONFIRMED INTEGER NOT NULL,"
                        + "DEATHS INTEGER NOT NULL,"
                        + "RECOVERED INTEGER NOT NULL,"
                        + "ACTIVE INTEGER NOT NULL,"
                        + "PREV_ID INTEGER,"
                        + "HASH CLOB,"
                        + "PREV_HASH CLOB,"
                        + "BLOCK_TS VARCHAR(255),"
                        + "NONCE CLOB)";

                Statement statement = connection.createStatement();
                statement.executeUpdate(table);

                System.out.println("Table '"+Constants.TABLE_STATS+"' has been created successfully!");

                // Closing the statement.
                statement.close();
            }

            /*String sql = "SELECT * FROM STATS WHERE id = (SELECT MAX(id) FROM STATS ORDER BY id ASC)";
            Statement statement = connection.createStatement();

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
                        "VALUES ('" +statistic.location+"','" +statistic.ts+"'," +
                        "'" +statistic.confirmed+"', '" +statistic.deaths+"', '" +prevIdInit+"', '" +genesisBlock.hash+"', '"+genesisBlock.previousHash+"', '"+genesisBlock.getTimeStamp()+"', '"+genesisBlock.getNonce()+"')";
                statement.executeUpdate(sqlInsert);
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
                currentBlock.hash=hash;
                blocklist.add(currentBlock);
            }
            rs2.close();*/

            // Closing the connections.
            // statement.close();
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

            String sql2 = "SELECT MAX(id) FROM Stats WHERE title = '"+statistic.location+"' ORDER BY id ASC";

            ResultSet rs = statement.executeQuery(sql2);
            if (rs.next()) {
                int prevId = rs.getInt(1);
                String sql3 = "INSERT INTO '"+Constants.TABLE_STATS+"' (" +
                        "LOCATION, TS, CONFIRMED, DEATHS, RECOVERED, ACTIVE, PREV_ID, HASH, PREV_HASH, BLOCK_TS, NONCE)" +
                        "VALUES ('" +statistic.location+"','" +statistic.ts+"','" +statistic.confirmed+"'," +
                        "'" +statistic.deaths+"', '" +statistic.recovered+"', '" +statistic.active+"', " +
                        "'" +prevId+"', '" +BlockChain.blocklist.get(BlockChain.blocklist.size()-1).hash+"'," +
                        " '"+BlockChain.blocklist.get(BlockChain.blocklist.size()-1).previousHash+"', " +
                        "'"+BlockChain.blocklist.get(BlockChain.blocklist.size()-1).getTimeStamp()+"', " +
                        "'"+BlockChain.blocklist.get(BlockChain.blocklist.size()-1).getNonce()+"') ";
                statement.executeUpdate(sql3);
                rs.close();
            }
            else {
                String sql = "INSERT INTO '"+Constants.TABLE_STATS+"' (" +
                        "LOCATION, TS, CONFIRMED, DEATHS, RECOVERED, ACTIVE, HASH, PREV_HASH, BLOCK_TS, NONCE)" +
                        "VALUES ('" +statistic.location+"','" +statistic.ts+"','" +statistic.confirmed+"'," +
                        "'" +statistic.deaths+"', '" +statistic.recovered+"', '" +statistic.active+"', " +
                        "'" + BlockChain.blocklist.get(BlockChain.blocklist.size() - 1).hash + "'," +
                        " '" + BlockChain.blocklist.get(BlockChain.blocklist.size() - 1).previousHash + "', " +
                        "'" + BlockChain.blocklist.get(BlockChain.blocklist.size() - 1).getTimeStamp() + "', " +
                        "'" + BlockChain.blocklist.get(BlockChain.blocklist.size() - 1).getNonce() + "') ";

                statement.executeUpdate(sql);
                rs.close();
            }

            statement.close();
            connection.close();
            System.out.println("Done!");
        } catch (SQLException e) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /*public DBManager() {
        String url = "jdbc:derby:covid19_stats;create=true";

        *//* Legend:
        *  DT: Date Time
        *  TS: TimeStamp in Long
        *
        * Creating the Statistics table if it doesn't exist. *//*
        String table = "CREATE TABLE IF NOT EXISTS STATS"
                + "(ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
                + "LOCATION TEXT NOT NULL,"
                + "DT DATE NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                + "TS BIGINT NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                + "CONFIRMED INTEGER NOT NULL,"
                + "DEATHS INTEGER NOT NULL,"
                + "RECOVERED INTEGER NOT NULL,"
                + "ACTIVE INTEGER NOT NULL,"
                + "PREV_ID INTEGER,"
                + "HASH TEXT,\n"
                + "PREV_HASH TEXT,"
                + "BLOCK_TS TEXT,"
                + "NONCE TEXT"
                + ")";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement())
        {
            System.out.println("Connection to the database established.");
            stmt.execute(table);

            String sql = "SELECT * FROM Stats WHERE id = (SELECT MAX(id) FROM Stats ORDER BY id ASC)";
            ResultSet rs = stmt.executeQuery(sql);

            // If DB is empty
            if (!rs.next()) {
                //Initializing the empty DB with 1 entry of a fake statistic.
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
                String sql2 = "INSERT INTO Stats (" +
                        "LOCATION, TS, CONFIRMED, DEATHS, RECOVERED, ACTIVE, PREV_ID, HASH, PREV_HASH, BLOCK_TS, NONCE) " +
                        "VALUES ('" +statistic.location+"','" +statistic.ts+"'," +
                        "'" +statistic.confirmed+"', '" +statistic.deaths+"', '" +prevIdInit+"', '" +genesisBlock.hash+"', '"+genesisBlock.previousHash+"', '"+genesisBlock.getTimeStamp()+"', '"+genesisBlock.getNonce()+"')";
                stmt.executeUpdate(sql2);
            }
            // If DB is not empty, we just read the latest DB entry and add it in our arraylist, so we can continue our work
            else
            {
                String hash = rs.getString("hash");
                String prevHash = rs.getString("prevHash");
                long blockTimestamp = Long.decode(rs.getString
                        ("blockTimestamp"));
                int nonce = rs.getInt("nonce");

                // Data
                String location = rs.getString("location");
                Integer confirmed = rs.getInt("confirmed");
                Integer deaths = rs.getInt("deaths");
                Integer recovered = rs.getInt("recovered");
                Integer active = rs.getInt("active");

                String data = new Statistic(location, confirmed, deaths,
                        recovered, active).jsonMaker();

                Block currentBlock = new Block("",prevHash);
                currentBlock.setTimeStamp(blockTimestamp);
                currentBlock.setNonce(nonce);
                currentBlock.setData(data);
                currentBlock.hash=hash;
                blocklist.add(currentBlock);
            }
            rs.close();

        } catch (SQLException e) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, e);
        }
    }*/
}
