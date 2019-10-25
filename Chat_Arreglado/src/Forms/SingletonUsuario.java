package Forms;

import java.io.*;
import java.util.ArrayList;

public class SingletonUsuario {

    public static ArrayList<String> usuarios = new ArrayList<>();
    private File file = new File("src/usuarios/usuarios.txt");

    public  SingletonUsuario() {
        creaUsuariosDefecto();
    }

    private void creaUsuariosDefecto(){
        try {
            String cadena;
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            while ((cadena = br.readLine()) != null ){
                usuarios.add(cadena);
            }
            br.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean addUser(String name){
        if (exist(name) == false){
            try{
                usuarios.add(name);
                FileWriter fw = new FileWriter(file);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter wr = new PrintWriter(bw);
                for (int i = 0; i < usuarios.size(); i++){
                    wr.println(usuarios.get(i));
                }
                wr.close();
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    public ArrayList<String> returnList(){
        return usuarios;
    }

    public boolean exist(String name){
        if(usuarios.contains(name)) return true;
        return false;
    }


}
