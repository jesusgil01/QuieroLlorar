/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mediadorchat;

import Forms.ChatVisual;
import com.sun.deploy.trace.Trace;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;



public abstract class Colega {

    public Socket cliente;
    public DataOutputStream buffSalida = null;
    public DataInputStream buffEntrada;
    public DataInputStream teclado;
    public String nombre;
    public String ip;
    public int puerto;
    public static String msg = "";


    public String returnMsg(){
        return this.msg;
    }

    public void RecibirDatos() {

        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        msg = buffEntrada.readUTF();
                        if ( msg.contains("Bienvenido")){
                            buffSalida.writeUTF(".");
                        }
                        buffSalida.flush();
                    }
                } catch (Exception e) {
                }
            }
        });
        hilo.start();
    }

    public void EnviarDatos(String msg) throws IOException {

        try {
            buffSalida.writeUTF("<" + nombre + "> " + msg);
            buffSalida.flush();
        } catch (Exception e) {
        }
        buffSalida.flush();
    }

    public void EscribirDatos() {
        try {
            String line = "";
            while (!line.equals(".bye")) {

                //buffSalida.writeUTF("Bug inicio");
                buffSalida.flush();

                System.out.print("->    ");
                line = teclado.readLine();

                buffSalida.writeUTF(line);
                buffSalida.flush();

            }
        } catch (Exception e) {
            System.out.println("Error -> ");
            System.out.println(e.getMessage());
        }

    }


}
