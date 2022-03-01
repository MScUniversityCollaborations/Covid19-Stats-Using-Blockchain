package com.unipi.torpiles.utils;
import java.util.ArrayList;
import java.util.UUID;

public class BlockChain {

    public static ArrayList<Block> blocklist = new ArrayList<>();

    public static int difficulty = 6;

    public static void addblock(String input)
    {
        if (blocklist.size() > 0)
        {
            Block currentBlock = new Block(input, blocklist.get(blocklist.size()-1).hash);
            currentBlock.mineBlock(difficulty);
            blocklist.add(currentBlock);
        }
        else
        {
            Block currentBlock = new Block(input, UUID.randomUUID().toString());
            currentBlock.mineBlock(difficulty);
            blocklist.add(currentBlock);
        }
    }

}
