/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mediadorchat;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.apache.commons.cli.*;
import org.apache.commons.cli.Option.Builder;

import java.io.Serializable;


/**
 * @author pedro
 */
public class Comandos {

    //public  CommandLine command;
    public Options options;

    public Comandos() {

        Option option_A = Option.builder("m")
                .desc("The command sends a text message to a topic")
                .argName("mBody")
                .required(false)
                .hasArg()
                .build();
        Option option_r = Option.builder("t")
                .desc("The t refers to the topic name")
                .argName("tName")
                .hasArg()
                .build();
        Option option_l = Option.builder("l")
                .desc("The l refers to in listing the topics")
                .argName("lList")
                .hasArg()
                .build();

        options = new Options();
        options.addOption(option_A);
        options.addOption(option_r);
        options.addOption(option_l);

    }

    public CommandLine parse(String args[]) throws ParseException {

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = null;
        //parser.p
        try {
            //System.out.println("entro a parser");
            // parser.
            //System.out.println(args);
            commandLine = parser.parse(options, args);
            //System.out.println("Salido del parser 0");
        } catch (ParseException exception) {
            System.out.print("Parse error: ");
            System.out.println(exception.getMessage());
        }

        //System.out.println("return del commandLine");
        return commandLine;

    }




}
