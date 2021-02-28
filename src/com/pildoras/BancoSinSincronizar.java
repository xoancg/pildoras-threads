package com.pildoras;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BancoSinSincronizar {

    public static void main(String[] args) {
        Banco b = new Banco();

        for(int i = 0; i < 100; i++){
            EjecucionTransferencias r = new EjecucionTransferencias(b,i,2000);
            Thread t = new Thread(r);
            t.start();
        }
    }
} // Fin BancoSinSincronizar

class Banco {

    public Banco() {
        cuentas = new double[100];   // Creamos as contas: 0,1,2...99

        for (int i = 0; i < cuentas.length; i++) {
            cuentas[i] = 2000; // Cargamos o saldo inicial
        }

        // saldoSuficiente = cierreBanco.newCondition(); // Establecemos a condición. Cada vez que creemos un banco, levará implícita a condición (por iso a incluimos no construtor)

    }

    public synchronized void transferencia(int cuentaOrigen, int cuentaDestino, double cantidad) throws InterruptedException {

        // cierreBanco.lock(); // Cando un fío chama a este método, o mesmo queda bloqueado mentres ese fío estea facendo uso do método. O seguinte fío quedará ao acougo no entanto.

        // try {
/*            if (cuentas[cuentaOrigen] < cantidad) { // Comprobamos que hai saldo dabondo na conta de orixe
                System.out.println("Saldo insuficiente na conta " + cuentaOrigen + ". Saldo: " + cuentas[cuentaOrigen] + " Importe transferencia: " + cantidad);
                return;
            } else {
                System.out.println("OK!");
            }*/

            while (cuentas[cuentaOrigen] < cantidad){
                // saldoSuficiente.await(); // Mentres que a condición do while sea true, poñemos o fío de execución ao acougoawait() and signalAll() methods (java.util.concurrent.locks).. More than one condition available

                wait(); // synchronized Only one condition available
            }

            System.out.println("* " + Thread.currentThread()); // Amosamos o fío que fai a transferencia

            cuentas[cuentaOrigen] -= cantidad; // Descontamos a cantidade da conta de orixe

            System.out.printf("%10.2f de %d para %d", cantidad, cuentaOrigen, cuentaDestino);

            cuentas[cuentaDestino] += cantidad; // Incrementamos a cantidade na conta de destino

            System.out.printf(" > Saldo total: %10.2f%n", getSaldoTotal());

            // saldoSuficiente.signalAll(); // Cando un fío remata a transferencia, avisa a todos os fíos que están ao acougo de saldo suficiente para que se executen no caso de que xa teñen saldo abondo.

            notifyAll(); // synchronized

        /*} finally {
            cierreBanco.unlock(); // Desbloqueamos o código cando un fío deixa de executalo
        }*/
    }

    public double getSaldoTotal(){
        double suma_cuentas = 0;

        for(double a: cuentas){ // Bucle for-each. Almacenamos en 'a' o saldo de todas as contas
            suma_cuentas += a;
        }
        return suma_cuentas;
    }

    private final double[] cuentas; // Array para gardar as contas
    private Lock cierreBanco = new ReentrantLock();
    private Condition saldoSuficiente; // Condición: que haxa saldo dabondo na conta para poder executar unha transferencia sen quedar con saldo negativo.
} // Fin Banco

class EjecucionTransferencias implements Runnable{

    public EjecucionTransferencias(Banco b, int de, double max){
        banco = b;
        deLaCuenta = de;
        cantidadMax = max;
    }

    @Override
    public void run() { // Recordar: este programa fará transferencias de forma infinita ata que deteñamos o programa
        try {
            while (true) {
                int paraLaCuenta = (int) (100 * Math.random()); // casting porque random() precisa números enteiros e ignoramos a parte decimal multiplicando por 100
                double cantidad = cantidadMax * Math.random(); // Almacenamos a cantidade a transferir
                banco.transferencia(deLaCuenta, paraLaCuenta, cantidad); // Os obxectos de tipo banco son os que establecen o bloqueo do método transferencia();
                Thread.sleep((int) (Math.random() * 10));
            }
        } catch (InterruptedException e) {

        }
    }

    private Banco banco;
    private int deLaCuenta; // Conta de orixe
    private double cantidadMax; // Cantidade máxima a transferir
} // Fin EjecucionTransferencias