package com.unipi.torpiles.utils.blockchain;
import java.util.ArrayList;
import java.util.UUID;

public class BlockChain {

    public static ArrayList<Block> blocklist = new ArrayList<>();

    public static int difficulty = 6;

    public static void addBlock(String input)
    {
        Block currentBlock;

        if (blocklist.size() > 0) {
            currentBlock = new Block(input, blocklist.get(blocklist.size() - 1).hash);
        }
        else {
            currentBlock = new Block(input, UUID.randomUUID().toString());
        }

        currentBlock.mineBlock(difficulty);
        blocklist.add(currentBlock);
    }
}
