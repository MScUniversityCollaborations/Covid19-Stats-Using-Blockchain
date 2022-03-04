package com.unipi.torpiles.utils;

class ConsoleProgress extends Thread {

        @Override
        public void run() {
            char[] animationChars = new char[]{'|', '/', '-', '\\'};

            for (int i = 0; i <= 100; i++) {
                System.out.print(
                        "Take a sip of coffee while you wait: " + i + "% " + animationChars[i % 4] + "\r" );

                try {
                    Thread.sleep(70);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            super.run();
        }
}
