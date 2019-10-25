/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mediadorchat;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static mediadorchat.Conexion.clientesConectados;


public class Topic {


    public static Vector<Conexion> usuarios;
    public String topicTitle;

    public Topic(String topicTitle){
        this.topicTitle = topicTitle;
        usuarios = new Vector();
    }

    public static Vector<Conexion> getUserList() {
        return usuarios;
    }

//    public static void setUserList(Conexion connection){
//        usuarios.add(connection);
//    }

    public static void addUser(Conexion conexion){
        usuarios.add(conexion);}

    public static void RemoveUserList(Conexion connection){
        usuarios.remove(connection);
    }

    public String getTopicTitle() {
        return topicTitle;
    }

    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
    }

    public void Publish(String msg) {
        for (int i = 0; i < usuarios.size() ; i++) {
            if (i != usuarios.indexOf(this)) {
                usuarios.get(i).EnviarMensaje(msg);
            }
        }
    }


}
