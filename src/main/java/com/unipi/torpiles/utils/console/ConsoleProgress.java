package com.unipi.torpiles.utils.console;

public class ConsoleProgress extends Thread {

    private int sleep;

    public synchronized void setTimeSleep(int sleep) {
        this.sleep = sleep;
    }

    @Override
    public void run() {
        char[] animationChars = new char[]{'|', '/', '-', '\\'};

        for (int i = 0; i <= 100; i++) {
            System.out.print(
                    "Take a sip of coffee while you wait: " + i + "% " + animationChars[i % 4] + "\r" );

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        super.run();
    }

}
