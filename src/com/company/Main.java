package com.company;


import sun.security.krb5.internal.PAData;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


/*
    Omar Khaled Abdelrahim 20150172
    Amr Alaa Aldin 20150182
    Mahmoud Farouk
 */

public class Main {


    public static void main(String[] args) throws IOException {
        Scanner scanner=new Scanner(System.in);
        File file=new File("/home/omar");
        while(true)
        {
            if(file.getPath()=="")
                System.out.print("Windows:~$ ");
            else
                System.out.print("Windows:~"+file.getPath().toString()+"$ ");
            String s;
            s=scanner.nextLine();
            CLI command=new CLI();
            if(s.length()!=0) {
                if (s.trim().equals("exit"))
                {
                    break;
                }
                if(s.contains("|"))
                {
                    command.isPipe=true;
                    ArrayList<String>parts=new ArrayList<String>(Arrays.asList(s.split("\\|")));
                    for(int x=0;x<parts.size();x++)
                    {
                        Parser parser=new Parser(parts.get(x));
                        command.parser=parser;
                        command.chooseCommand();
                    }
                }
                else
                {
                    Parser parser = new Parser(s);
                    command.parser=parser;
                    command.chooseCommand();
                }

            }
            file=command.file;
            command.isPipe=false;
        }
    }

}
