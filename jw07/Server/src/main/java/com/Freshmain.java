package com;

public class Freshmain implements Runnable {
    ApplicationMain main;

    public Freshmain(ApplicationMain main) {
        this.main = main;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(20);
                try {
                    main.write();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                ;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        }
    }
}