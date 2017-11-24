package com.company;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import javax.annotation.processing.SupportedSourceVersion;
import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;


public class CLI
{
    public static File file=new File("/home/omar");
    public static StringBuilder passed=new StringBuilder();
    public static boolean isPipe=false;
    public static Parser parser;

    public static void clear()
    {

        for (int i=0 ; i<60; i++)
        {
            System.out.println('\n');
        }
    }

    public static File cd()
    {
        if(parser.size>2)
        {
            System.out.println("ERROR cd: [arg]");
        }
        else
        {
            try
            {
                if(new File(parser.dir1).isDirectory())
                    return new File(parser.dir1);
                else
                    System.out.println("cd:"+parser.dir1 +" : No such file or directory");
            }
            catch (Exception e)
            {
                System.out.println("cd:"+parser.dir1 +" : No such file or directory");
            }

        }
        return new File("/home/omar");

    }

    public static void ls()
    {
        if((!parser.dir1.isEmpty()||!parser.dir2.isEmpty())&&parser.redirect.isEmpty())
        {
            System.out.println("ERROR! ls: [no arg]");
            return;
        }
        File[] sortedFiles=file.listFiles();
        Arrays.sort(sortedFiles);
        if(isPipe==true)
        {
            passed.delete(0,passed.length());
            for(File f:sortedFiles)
            {
                passed.append(f.getName()+"\n");
            }
            return;
        }
        else if(!parser.redirect.isEmpty())
        {
            passed.delete(0,passed.length());
            for(File f:sortedFiles)
                passed.append(f.getName()+"\n");
            redirect();
        }
        else
        {
            for(File f:sortedFiles)
                System.out.println(f.getName());
        }
    }

    public static void cp(){
        if(parser.dir1.isEmpty()||parser.dir2.isEmpty()) {
            System.out.println("ERROR! cp: [arg1] [arg2] copies contents of arg1(file) to arg2(file).");
            return;
        }
        String path;
        if(!new File(parser.dir1).exists())
            path=file.getPath()+"/"+parser.dir1;
        else
            path=parser.dir1;
        File source=new File(path);
        File target=new File(parser.dir2);
        try
        {
            if(parser.option.equals("-f"))
            {
                Files.copy(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
                return;
            }
            else if(parser.option.equals("-r"))
            {
                File dir=null;
                try {
                    dir = new File(parser.dir2 + '/' + source.getName());
                    if (!dir.mkdir())
                    {
                        System.out.println("Failed to copy");
                        return;
                    }
                }
                catch (Exception e)
                {
                    System.out.println("Folder Already Exists");
                }
                File[] files=new File(path).listFiles();
                for(File f:files)
                {
                    Files.copy(f.toPath(),new File(dir.getPath()+"/"+f.getName()).toPath());
                }
                return;
            }
            else if(parser.option.equals("-i")||target.exists())
            {
                System.out.println("Do you want to overwrite it (Y / N) ?");
                char c;
                Scanner scanner=new Scanner(System.in);
                c=scanner.next().charAt(0);
                if(c=='Y'||c=='y')
                {
                    Files.copy(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    return;
                } else
                    return;
            }
            Files.copy(source.toPath(),target.toPath());
        }
        catch (Exception e)
        {
            System.out.println("No such file or directory");
        }
    }

    public static void mv()
    {
        if(parser.dir1.isEmpty()||parser.dir2.isEmpty()) {
            System.out.println("ERROR! mv: [arg1] [arg2] ");
            return;
        }
        String path;
        if(!new File(parser.dir1).exists())
            path=file.getPath()+"/"+parser.dir1;
        else
            path=parser.dir1;


        if(parser.dir1.isEmpty()||parser.dir2.isEmpty()) {
            System.out.println("mv command only have two argus (file directory / destination directory).");
            return;
        }
        try {

            File source = new File(path);
            File target = new File(parser.dir2);
            if(parser.option.equals("-f"))
                Files.move(source.toPath(), target.toPath(),StandardCopyOption.REPLACE_EXISTING);
            else if(parser.option.equals("-r"))
            {
                File dir=null;
                try {
                    dir = new File(parser.dir2 + '/' + source.getName());
                    if (!dir.mkdir())
                    {
                        System.out.println("Failed to move");
                        return;
                    }
                }
                catch (Exception e)
                {
                    System.out.println("Folder Already Exists");
                }
                File[] files=new File(path).listFiles();
                for(File f:files)
                {
                    Files.move(f.toPath(),new File(dir.getPath()+"/"+f.getName()).toPath());
                }
                new File(path).delete();
                return;
            }
            else if(parser.option.equals("-i")||target.exists())
            {
                System.out.print("Do you want to overrite it (Y , N) ? ");
                char c;
                Scanner scanner=new Scanner(System.in);
                c=scanner.next().charAt(0);
                if(c=='Y'||c=='y')
                    Files.move(source.toPath(), target.toPath(),StandardCopyOption.REPLACE_EXISTING);
                else
                    return;
            }
            else
                Files.move(source.toPath(), target.toPath());

        } catch (IOException e) {
            System.out.println("No such file or directory");
        }
    }

    public static void mkdir()
    {
        if(!parser.dir2.isEmpty()) {
            System.out.println("ERROR! mkdir: [arg1]");
            return;
        }
        try {
            File dir = new File(parser.dir1);  // modified the user should enter the full path
            if (dir.mkdir())
                System.out.println("Created Successfully");
            else
                System.out.println("Failed to create");
        }
        catch (Exception e)
        {
            System.out.println("Name Already Exists");
        }
    }

    public static void rmdir()
    {
        if(!parser.dir2.isEmpty()) {
            System.out.println("ERROR! rmdir: [arg1]");
            return;
        }
        try
        {
            if (new File(parser.dir1).isDirectory())
            {
                File specfic = new File(parser.dir1);
                specfic.delete();
            } else
                System.out.println("No such directory");
        }
        catch (Exception e)
        {
            System.out.println("No such directory");
        }
    }

    public static void rm()
    {
        if(parser.dir2.isEmpty()) {
            System.out.println("ERROR! rm: [arg1]");
            return;
        }
        try {
            String path;
            if(!new File(parser.dir1).exists())
                path=file.getPath()+"/"+parser.dir1;
            else
                path=parser.dir1;

            if (new File(path).exists())
            {
                File specfic = new File(path);
                if (parser.option.equals("-f"))
                    specfic.delete();
                else
                {
                    System.out.print("Do you want to delete it (Y , N) ? ");
                    char c;
                    Scanner scanner = new Scanner(System.in);
                    c = scanner.next().charAt(0);
                    if (c == 'Y' || c == 'y')
                        specfic.delete();
                    else
                        return;
                }
            }
            else
            {
                System.out.println("No such file or directory");
                return;
            }
        }
        catch (Exception e)
        {
            System.out.println("No such file or directory");
        }
    }

    public static void redirect()
    {
        try
        {
            String path;
            if(!parser.dir2.isEmpty())
                path=parser.dir2;
            else
                path=parser.dir1;
            FileWriter fileWriter;
            if(parser.redirect.equals(">"))
               fileWriter=new FileWriter(file.getPath()+"/"+path,false);
            else
                fileWriter=new FileWriter(file.getPath()+"/"+path,true);

            fileWriter.write(passed.toString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void cat()
    {
        try {
            String path;
            if (!new File(parser.dir1).exists())
                path = file.getPath() + "/" + parser.dir1;
            else
                path = parser.dir1;
            if (!path.isEmpty() && !parser.dir2.isEmpty() && parser.redirect.isEmpty())
            {
                FileReader fileReader = new FileReader(path);
                FileWriter fileWriter = new FileWriter(parser.dir2);
                int c = fileReader.read();
                while (c != -1)
                {
                    c = fileReader.read();
                    fileWriter.write(c);
                }
            }
            else if (!parser.dir1.isEmpty() && !parser.dir2.isEmpty())
            {
                Scanner scanner = new Scanner(new File(path));
                passed.delete(0,passed.length());
                while (scanner.hasNext())
                {
                    passed.append(scanner.nextLine()+"\n");
                }
                redirect();
            }
            else if (!parser.dir1.isEmpty() && parser.dir2.isEmpty())
            {
                Scanner scanner = new Scanner(new File(path));
                if (isPipe == true)
                {
                    passed.delete(0,passed.length());
                    while (scanner.hasNext())
                    {
                        passed.append(scanner.nextLine()+"\n");
                    }
                }
                else
                {
                    while (scanner.hasNext())
                    {
                        System.out.println(scanner.nextLine());
                    }
                }
            } else
                System.out.println("No such file or directory");
        } catch (FileNotFoundException e) {
            System.out.println("no such file or directory");
        } catch (IOException e) {
            System.out.println("no such file or directory");
        }

    }

    public static void pwd()
    {
        System.out.println(file.getPath().toString());
    }

    public static void more()
    {
        if(isPipe==true)
        {
            try
            {
                Scanner scanner=new Scanner(System.in);
                Scanner scanner1=new Scanner(passed.toString());
                ArrayList<String> str=new ArrayList<String>();
                while (scanner1.hasNext())
                {
                    if(str.size()<10)
                    {
                        str.add(scanner1.nextLine());
                    }
                    else
                    {
                        for(int i=0;i<str.size();i++)
                        {
                            if(i==str.size()-1)
                                System.out.print(str.get(i));
                            else
                                System.out.println(str.get(i));
                        }
                        str.clear();
                        scanner.nextLine();
                    }
                }
                if(str.size()>0)
                {
                    for(int i=0;i<str.size();i++)
                    {
                        if(i==str.size()-1)
                            System.out.print(str.get(i));
                        else
                            System.out.println(str.get(i));
                    }
                }
                return;
            }
            catch (Exception e)
            {
                System.out.println("No such file or directory");
            }
        }
        String path;
        if(!new File(parser.dir1).exists())
            path=file.getPath()+"/"+parser.dir1;
        else
            path=parser.dir1;
        try
        {
            Scanner scanner=new Scanner(System.in);
            Scanner scanner1=new Scanner(new File(path));
            ArrayList<String> str=new ArrayList<String>();
            while (scanner1.hasNext())
            {
                if(str.size()<10)
                {
                    str.add(scanner1.nextLine());
                }
                else
                {
                    for(int i=0;i<str.size();i++)
                    {
                        if(i==str.size()-1)
                            System.out.print(str.get(i));
                        else
                            System.out.println(str.get(i));
                    }
                    str.clear();
                    scanner.nextLine();
                }
            }
            if(str.size()>0)
            {
                for(int i=0;i<str.size();i++)
                {
                    if(i==str.size()-1)
                        System.out.print(str.get(i));
                    else
                        System.out.println(str.get(i));
                }
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("No such file or directory");
        }
    }

    public static void args()
    {
        if(parser.dir1.equals("cd"))System.out.println("cd: [arg] changes working directory to the given arg");
        if(parser.dir1.equals("ls"))System.out.println("ls: [no arg] displays contents of a file");
        if(parser.dir1.equals("cp"))System.out.println("cp: [arg1] [arg2] copies contents of arg1(file) to arg2(file) + directory argN");
        if(parser.dir1.equals("mv"))System.out.println("mv: [arg1] [arg2] copies contents of arg1(file) to arg2(file) and deletes arg1");
        if(parser.dir1.equals("mkdir"))System.out.println("mkdir: [arg] creates a directory with whose name is the given argument");
        if(parser.dir1.equals("rmdir"))System.out.println("rmdir: [arg] deletes a directory whose name is given argument");
        if(parser.dir1.equals("rm"))System.out.println("rm: [arg] deletes a file whose name is the given argument");
        if(parser.dir1.equals("cat"))System.out.println("cat: [arg1] displays contents of arg1(file)");
        if(parser.dir1.equals("cat"))System.out.println("cat: [arg1] [arg2] concatenates contents of arg1 to contents of arg2 and displays the result");
        if(parser.dir1.equals("date"))System.out.println("date : [noargs] displays system date and time");
    }

    public static void help()
    {
        System.out.println("cd: [arg] Changes current working directory. The default directory is the value of the HOME\n");
        System.out.println("pwd : [no arg] Display current working directory\n");
        System.out.println("ls:[no arg] List all names of files of current directory sorted alphabetically\n");
        System.out.println("mv: [arg1] [arg2] Moves files from directory to another director\n");
        System.out.println("cp: [arg1] [arg2] Copies files to another directory\n");
        System.out.println("mkdir:[arg]  Creates a new directory\n");
        System.out.println("rmdir: [arg] Deletes a directory\n");
        System.out.println("rm: [arg] Deletes a file\n");
        System.out.println("cat: [arg1] displays contents of arg1(file)\n");
        System.out.println("cat: [arg1] [arg2] concatenates contents of arg1 to contents of arg2 and displays the result\n");
        System.out.println("args: [arg] List all commands arguments\n");
        System.out.println("date:[no arg] Displays system date and time\n");
    }

    public static void helpOfCommand()
    {
        if(parser.dir1.equals("cd"))System.out.println("cd: Changes current working directory");
        if(parser.dir1.equals("pwd"))System.out.println("pwd : Display current directory");
        if(parser.dir1.equals("ls"))System.out.println("ls: List all contents of current directory");
        if(parser.dir1.equals("mv"))System.out.println("mv: Moves files from directory to another directory");
        if(parser.dir1.equals("cp"))System.out.println("cp: Copies files");
        if(parser.dir1.equals("mkdir"))System.out.println("mkdir: Creates a new directory");
        if(parser.dir1.equals("rmdir"))System.out.println("rmdir: Deletes a directory");
        if(parser.dir1.equals("rm"))System.out.println("rm: Deletes a file");
        if(parser.dir1.equals("cat"))System.out.println("cat: Displays contents of a file and concatenates files and display output");
        if(parser.dir1.equals("args"))System.out.println("args: List all commands arguments");
        if(parser.dir1.equals("date"))System.out.println("date: Displays system date and time");
    }

    public static void date()
    {
        if(parser.size>1)
        {
            System.out.println("ERROR date: [no args] don't have any args");
        }
        Date date = new Date();
        System.out.println(date.toString());
    }

    public static void chooseCommand() throws IOException {

        if(parser.command.equals("clear"))
            clear();
        else if(parser.command.equals("cd"))
            file=cd();
        else if(parser.command.equals("ls"))
            ls();
        else if(parser.command.equals("cp"))
            cp();
        else if(parser.command.equals("mv"))
            mv();
        else if(parser.command.equals("rm"))
            rm();
        else if(parser.command.equals("mkdir"))
            mkdir();
        else if(parser.command.equals("rmdir"))
            rmdir();
        else if(parser.command.equals("cat"))
            cat();
        else if(parser.command.equals("more"))
            more();
        else if(parser.command.equals("pwd"))
            pwd();
        else if(parser.command.equals("args"))
            args();
        else if(parser.command.equals("?"))
            helpOfCommand();
        else if(parser.command.equals("date"))
            date();
        else if(parser.command.equals("help"))
            help();
        else
            System.out.println(parser.command+ " command not found");

    }

}
