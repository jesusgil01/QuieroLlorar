/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mediadorchat;

import Forms.ChatVisual;
import Forms.LoginVisul;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Cliente extends Colega {

    static ChatVisual chat;


    public Cliente(String nombre, String ip, int puerto) {
        this.puerto = puerto;
        this.nombre = nombre;
        this.ip = ip;
        this.chat = chat;
    }

    public void abreVentana(){
        this.chat.setVisible(true);
    }

    public void init() {

        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cliente = new Socket(ip, puerto);
                    buffSalida = new DataOutputStream(cliente.getOutputStream());
                    buffEntrada = new DataInputStream(cliente.getInputStream());
                    //String msg = buffSalida.();
                    teclado = new DataInputStream(System.in);
                    buffSalida.writeUTF(nombre);
                    buffSalida.flush();

                    RecibirDatos();
                    //EscribirDatos();
                } catch (Exception e) {
                    System.out.println("no funcino");
                    System.exit(0);
                }
            }
        });
        hilo.start();




    }

    public static void main(String[] args) {

        System.out.println("enviar -m '<Cuerpo del mensaje>' -t '<Nombre del topico>' ");
        System.out.println("crear -t '<Nombre del topico>' ");
        System.out.println("remove -t '<Nombre del topico>' ");
        System.out.println("suscribe -t '<Nombre del topico>' ");
        System.out.println("unsuscribe -t '<Nombre del topico>' ");
        System.out.println("topics -l");
        System.out.println("");


    }


}
