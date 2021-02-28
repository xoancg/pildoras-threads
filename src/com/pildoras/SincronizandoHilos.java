package com.pildoras;

public class SincronizandoHilos {
    public static void main(String[] args) {

        HilosVarios hilo1 = new HilosVarios();
        hilosVarios2 hilo2 = new hilosVarios2(hilo1);

        hilo2.start();
        hilo1.start();

        System.out.println("Fin do programa");
    }
}
class HilosVarios extends Thread {

    public void run() {
        for (int i = 0; i < 15; i++) {
            System.out.println("Execuntando fío " + getName());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}class hilosVarios2 extends Thread {

    public hilosVarios2(Thread hilo){
        this.hilo = hilo;
    }

    public void run() {

        try {
            hilo.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 15; i++) {
            System.out.println("Execuntando fío " + getName());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Thread hilo;
}
