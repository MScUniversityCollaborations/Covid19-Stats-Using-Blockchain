package com.unipi.torpiles.Utils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Block {
    public String hash;
    public String previousHash;
    private String data;
    private long timeStamp;
    private int nonce;


    public Block(String data,String previousHash) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.nonce = Integer.MIN_VALUE;
        this.hash = calculateHash();
    }

    public String calculateHash() {
        return StringUtil.applySha256(
                previousHash +
                        timeStamp +
                        nonce +
                        data
        );
    }


    public void mineBlock(int difficulty) {
        long startTime = System.nanoTime();
        String target = new String(new char[difficulty]).replace('\0', '0');
        while(!hash.substring( 0, difficulty).equals(target)) {
            nonce ++;
            hash = calculateHash();
        }
        long endTime = System.nanoTime();
        long convert = TimeUnit.SECONDS.convert(endTime-startTime, TimeUnit.NANOSECONDS);
        System.out.println("Latest Block Mined Successfully with hash : " + hash);
        System.out.println("Current block's mining took: "+convert+" seconds");
    }

    public String getData(){return  data;}
    public long getTimeStamp(){return  timeStamp;}
    public int getNonce(){return  nonce;}
    public void setNonce(int x) {nonce=x;}
    public void setTimeStamp(long x){timeStamp=x;}
    public void setData(String x){data=x;}
}
