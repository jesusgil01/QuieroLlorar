/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mediadorchat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public abstract class Mediator {
    public ServerSocket server;
    public int puerto = 9500;
    //Conexion[] cliente = new Conexion[11];
    public List<Conexion> conexiones = new ArrayList<Conexion>();

    public void init() {
        Socket socket;
        try {
            server = new ServerSocket(puerto);
            System.out.println("Esperando peticiones por el puerto " + puerto);
            while (true) {
                socket = server.accept();
                DataInputStream buffEntrada = new DataInputStream(socket.getInputStream());
                DataOutputStream buffSalida = new DataOutputStream(socket.getOutputStream());
                String nombre = buffEntrada.readUTF();
                if ( valida(conexiones,nombre) == false ) {
                    buffSalida.writeUTF("El usuario ya existe");
                    //break;
                } else {
                    buffSalida.writeUTF("Bienvenido " + nombre);
                    buffSalida.flush();
                    Conexion conexion = new Conexion(socket, buffEntrada, buffSalida);
                    conexion.setName(nombre);
                    conexion.start();
                    conexiones.add(conexion);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean valida (List<Conexion> conexiones , String  buffEntrada){
        for (Conexion con : conexiones) {
            if (con.getName().compareTo(buffEntrada.toString()) == 0){
                return false;
            }
        }
        return true;
    }


}
