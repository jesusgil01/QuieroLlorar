/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mediadorchat;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import sun.security.krb5.internal.crypto.Des;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.Vector;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Conexion extends Thread {

    Socket cliente1;
    DataInputStream buffEntrada;
    DataOutputStream buffSalida;
    DataInputStream teclado;
    public static Vector<Conexion> clientesConectados = new Vector();
    public static List<Topic> topics = new ArrayList<>();
    Comandos comandos = new Comandos();
    Topic topic_a; //= new Topic();
    CommandLineParser parser = new DefaultParser();


    public Conexion(Socket cliente, DataInputStream buffEntrada, DataOutputStream buffSalida) {
        cliente1 = cliente;
        this.buffEntrada = buffEntrada;
        this.buffSalida = buffSalida;
        clientesConectados.add(this);
//        Topic topic = topics.stream().
//                filter(current -> "BroadCasts".equals(current.getTopicTitle()))
//                .findAny()
//                .orElse(null);
//        topic.getUserList().add(this);

    }

    public void run() {
        try {
            CrearTopicosPorDefecto();
            DisplayUsers(clientesConectados);
            SuscribeBroadCast();

            while (true) {

                ArrayList<String> args = new ArrayList<>();
                String mensaje = buffEntrada.readUTF();
                System.out.println(this.getName() + " dice: " + mensaje);
                if (mensaje.compareTo("") == 0) mensaje = ".";

                Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
                Matcher regexMatcher = regex.matcher(mensaje);

                int index = 0;

                while (regexMatcher.find()) {
                    if (regexMatcher.group(1) != null) args.add(regexMatcher.group(1));
                    else if (regexMatcher.group(2) != null) args.add(regexMatcher.group(2));
                    else args.add(regexMatcher.group());
                }

                if (args.get(0).equals(".")){

                }
                if (args.get(0).equals("enviar")) {
                    Enviar(args);
                } else if (args.get(0).equals("crear")) {
                    Crear(args);
                } else if (args.get(0).equals("remove")) {
                    Remover(args);
                } else if (args.get(0).equals("suscribe")) {
                    Suscribe(args);
                } else if (args.get(0).equals("unsuscribe")) {
                    Unsuscribe(args);
                } else if (args.get(0).equals("topics")) {
                    Topics(args);
                } else {
                    System.out.println("Comando no reconocido");
                }
                System.out.println("*   --- --- --- --- ---    *");
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

    }

    private void Enviar(ArrayList<String> args) {
        try {
            CommandLine commandLine = comandos.parse(args.toArray(new String[args.size()]));
            if (commandLine != null) {
                if (commandLine.hasOption("m")) {
                    String messageBody = commandLine.getOptionValue("m").trim();
                    if (commandLine.hasOption("t")) {
                        String topicName = commandLine.getOptionValue("t").trim();
                        for (Topic i : topics) {
                            if (i.getTopicTitle().compareTo(topicName) == 0) {
                                i.Publish(this.getName()+" a "+ topicName +": " + messageBody);
                            }
                        }
                    }
                }
            }
        } catch (ParseException e) {

        }
    }

    private void Crear(ArrayList<String> args) {
        try {
            CommandLine commandLine = comandos.parse(args.toArray(new String[args.size()]));

            if (commandLine != null) {

                if (commandLine.hasOption("t")) {
                    String topicName = commandLine.getOptionValue("t").trim();

                    if (FindTopic(topicName) != false) {
                        topic_a.setTopicTitle(topicName);
                        topic_a.addUser(this);
                        topics.add(topic_a);
                        Display(topics);

                        EnviarMensaje("Se creo el topico " + topicName);

//                        for (Topic topic : topics) {
//                            if (topic.getTopicTitle().compareTo(topicName) == 0) {
//                                topic.setUserList(this);
//                            }
//                        }

                    } else {
                        EnviarMensaje("El topico ya existe");
                    }

                }

            }
        } catch (ParseException e) {
            System.out.println("Error en crear:");
            System.out.println(e.getMessage());
        }
    }

    private void Remover(ArrayList<String> args) {
        try {
            CommandLine commandLine = comandos.parse(args.toArray(new String[args.size()]));
            if (commandLine != null) {
                if (commandLine.hasOption("t")) {
                    String topicName = commandLine.getOptionValue("t").trim();
                    topic_a = GetTopic(topicName);
                    if (topic_a != null) {

                        topics.remove(topic_a);
                        Display(topics);
                        System.out.printf("Hola desde crear");
                        EnviarMensaje("El topico " + topicName + " fue removido");
                    } else if (topic_a == null) {
                        EnviarMensaje("El topico " + topicName + " no existe");
                    }
                }
            }
        } catch (ParseException e) {

        }
    }

    private void Suscribe(ArrayList<String> args) {
        try {
            CommandLine commandLine = comandos.parse(args.toArray(new String[args.size()]));
            if (commandLine != null) {
                if (commandLine.hasOption("t")) {
                    String topicName = commandLine.getOptionValue("t");
                    if (FindTopic(topicName) == false) {
                        for (Topic topic : topics) {
                            if (topic.getTopicTitle().compareTo(topicName) == 0) {
                                if (!topic.getUserList().contains(this)) {
                                    topic.addUser(this);
                                    EnviarMensaje("Suscrito al topico:  " + topicName);
                                }
                                EnviarMensaje("Ya esta suscrito a este topico:  " + topicName);
                            }
                        }
                    } else {
                        EnviarMensaje("EL topico: " + topicName + " no existe.");
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void Unsuscribe(ArrayList<String> args) {
        try {
            CommandLine commandLine = comandos.parse(args.toArray(new String[args.size()]));
            if (commandLine != null) {
                if (commandLine.hasOption("t")) {
                    String topicName = commandLine.getOptionValue("t");
                    if (FindTopic(topicName) == false) {
                        for (Topic topic : topics) {
                            if (topic.getTopicTitle().compareTo(topicName) == 0) {
                                topic.RemoveUserList(this);
                                EnviarMensaje("Dadjo de baja del topico: " + topicName);
                            }
                        }
                    } else {
                        EnviarMensaje("EL topico: " + topicName + " no existe.");
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void Topics(ArrayList<String> args) {
        try {
            buffSalida.writeUTF(Display(topics));
        } catch (IOException e) {
        }
    }



    //  Metodos y funciones

    private void SuscribeBroadCast() {
        for (Topic topic : topics) {
            if (topic.getTopicTitle().compareTo("BroadCast") == 0) {
                topic.addUser(this);
                EnviarMensaje("Suscrito al topico por defecto BroadCast");
            }
        }
    }

    private void CrearTopicosPorDefecto() {
        Topic Desarrollo4 = new Topic("Desarrollo4");
        //Desarrollo4.setTopicTitle("Desarrollo4");
        topics.add(Desarrollo4);

        Topic Pruebas = new Topic("Pruebas");
        //Pruebas.setTopicTitle("Pruebas");
        topics.add(Pruebas);

        Topic BroadCasts = new Topic("BroadCast");
        //BroadCasts.setTopicTitle("BroadCast");
        topics.add(BroadCasts);
    }

    public void EnviarMensaje(String mensaje) {
        try {
            buffSalida.writeUTF(mensaje);
        } catch (Exception e) {
        }

    }

    private String Display(List<Topic> topics) {
        String ou = "";
        for (Topic i : topics) {
            ou = ou + "Topic -> " + i.getTopicTitle() + "\n";

        }
        return ou;
    }

    private String DisplayClient(List<Topic> topics) {
        String line = "";
        for (Topic i : topics) {
            line = line + i.getTopicTitle() + "\n";
        }
        return line;
    }

    private Topic GetTopic(String topicName) {
        for (Topic topic : topics) {
            if (topic.getTopicTitle().equals(topicName)) {
                return topic;
            }
        }
        return null;
    }

    private void DisplayUsers(Vector<Conexion> usuarios) {
        System.out.println("Usuarios conectados");
        for (Conexion e : usuarios) {
            System.out.println(e.getName());
        }
        System.out.println("-");
        //System.out.println("*   --- --- --- --- ---    *");
    }

    private boolean FindTopic(String name) {
        for (Topic i : topics) {
            if (i.getTopicTitle().compareTo(name) == 0) {
                return false;
            }
        }
        return true;
    }


}
