package com.unipi.torpiles.database;

import com.unipi.torpiles.Utils.Block;
import com.unipi.torpiles.Utils.BlockChain;
import com.unipi.torpiles.models.Statistic;

import java.sql.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBManager extends BlockChain {

    public DBManager() {
        String url = "jdbc:derby:covid19_stats;create=true";

        /* Legend:
        *  DT: Date Time
        *  TS: TimeStamp in Long
        *
        * Creating the Statistics table if it doesn't exist. */
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
            DatabaseMetaData meta = conn.getMetaData();
            System.out.println("Connection to the database established.");
            stmt.execute(table);

            String sql = "SELECT * FROM Stats WHERE id = (SELECT MAX(id) FROM Stats ORDER BY id ASC)";
            ResultSet rs = stmt.executeQuery(sql);

            // If DB is empty
            if (!rs.next()) {
                //Initializing the empty DB with 1 entry of a fake statistic.
                Statistic statistic = new Statistic ("",0, 0,0,0, LocalDate.now(), new Date().getTime());
                Block genesisBlock = new Block(statistic.jsonMaker(), UUID.randomUUID().toString());
                genesisBlock.mineBlock(difficulty);
                blocklist.add(genesisBlock);
                int previdinit = 0;
                String sql2 = "INSERT INTO Stats (LOCATION, DT, TS, CONFIRMED, DEATHS, RECOVERED, ACTIVE, PREV_ID, HASH, PREV_HASH, BLOCK_TS, NONCE) " +
                        "VALUES ('" +statistic.location+"','" +statistic.dt+"','" +statistic.ts+"'," +
                        "'" +statistic.confirmed+"', '" +statistic.deaths+"', '" +previdinit+"', '" +genesisBlock.hash+"', '"+genesisBlock.previousHash+"', '"+genesisBlock.getTimeStamp()+"', '"+genesisBlock.getNonce()+"')";
                stmt.executeUpdate(sql2);
                rs.close();
            }
            // If DB is not empty, we just read the latest DB entry and add it in our arraylist, so we can continue our work
            else
            {
                String hash = rs.getString("hash");
                String prevHash = rs.getString("prevHash");
                long blockTimestamp = Long.decode(rs.getString("blockTimestamp"));
                int nonce = rs.getInt("nonce");

                // Data
                String location = rs.getString("location");
                Integer confirmed = rs.getInt("confirmed");
                Integer deaths = rs.getInt("deaths");
                Integer recovered = rs.getInt("recovered");
                Integer active = rs.getInt("active");

                String data = new Statistic(location, confirmed, deaths, recovered, active).jsonMaker();

                Block currentBlock = new Block ("",prevHash);
                currentBlock.setTimeStamp(blockTimestamp);
                currentBlock.setNonce(nonce);
                currentBlock.setData(data);
                currentBlock.hash=hash;
                blocklist.add(currentBlock);
                rs.close();
            }

        } catch (SQLException e) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
