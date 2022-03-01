package com.unipi.torpiles.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Block {
    public String hash;
    public String previousHash;
    private String data;
    private long timeStamp;
    private int nonce;
    private boolean miningFinish; //Needed to find out whether a thread has finished its work

    public Block(String data,String previousHash) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.nonce = Integer.MIN_VALUE;
        this.hash = calculateHash(this.nonce);
    }

    public String calculateHash(int x) {
        return StringUtil.applySha256(
                previousHash +
                        timeStamp +
                        x +
                        data
        );
    }

    synchronized boolean isMiningFinish(){
        return miningFinish;
    }

    public void mineBlock(int difficulty) {
        miningFinish = false;
        long startTime = System.nanoTime();
        List<Thread> threads = new ArrayList<>();
        Runnable runnableTask = () -> {
            String target = new String(new char[difficulty]).replace('\0', '0');
            int tnonce = this.nonce; //each thread will save a copy of the initial nonce and hash in order not to mess the original ones up
            String thash = this.hash;
            while(!thash.substring( 0, difficulty).equals(target)&&!miningFinish) {
                tnonce ++;
                thash = calculateHash(tnonce);
            }
            miningFinish = true;

            synchronized (this) {
                if (miningFinish&&thash.substring( 0, difficulty).equals(target)) {
                    this.nonce = tnonce;
                    this.hash = thash;
                    long endTime = System.nanoTime();
                    long convert = TimeUnit.SECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS);
                    System.out.println("Latest Block Mined Successfully with hash : " + hash);
                    System.out.println("Current block's mining took: " + convert + " seconds");
                    for (Thread thread : threads)
                    {
                        thread.interrupt();
                    }
                }
            }
        };
        for (int i=0;i<5;i++)  //Using 5 simple threads.
        {
            Thread thread = new Thread(runnableTask);
            thread.start();
            threads.add(thread);
        }
        // Waiting for the mining to get done, otherwise the program will continue with the wrong execution order.
        while (!isMiningFinish()) {}
    }

    public String getData(){return  data;}
    public long getTimeStamp(){return  timeStamp;}
    public int getNonce(){return  nonce;}
    public void setNonce(int x) {nonce=x;}
    public void setTimeStamp(long x){timeStamp=x;}
    public void setData(String x){data=x;}
}
