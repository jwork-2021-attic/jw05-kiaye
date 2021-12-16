public class Freshmain implements Runnable {
    ApplicationMain main;

    public Freshmain(ApplicationMain main){
        this.main = main;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(20);
                main.repaint();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        }
    }
}