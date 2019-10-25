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

public class MediadorChat extends Mediator {


    public MediadorChat(int puerto) {

        this.puerto = puerto;
    }

    public static void main(String[] args) {
        MediadorChat servidor = new MediadorChat(1414);
        servidor.init();
    }

}
