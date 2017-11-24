package com.company;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser
{
    String command=null;
    String option=null;
    String redirect=null;
    String dir1=null;
    String dir2=null;
    int size;

    public List<String> split(String line)    //handle empty elements in list
    {
        List<String> result=new ArrayList<String>();
        String[] parts=line.split(" ");
        for(String s:parts)
        {
            if(s!=null&&s.length()>0)
                result.add(s);
        }
        return result;
    }

    Parser()
    {

    }
    Parser(String line)
    {
        List<String>parts=split(line);
        String[] commands=new String[]{"cd","cp","mv","rm","mkdir","rmdir","ls","cat","more","pwd","clear","?","args","date","help"};
        String[] op=new String[]{"-a","-f","-r","-i"};
        String[] redr=new String[]{">",">>"};
        size=parts.size();
        for(int i=0;i<parts.size();i++)
        {
            if(Arrays.asList(commands).contains(parts.get(i))&&command==null)
                command=parts.get(i);
            else if(Arrays.asList(op).contains(parts.get(i))&&option==null)
                option=parts.get(i);
            else if(Arrays.asList(redr).contains(parts.get(i))&&redirect==null)
                redirect=parts.get(i);
            else if(dir1==null)
                dir1=parts.get(i);
            else if(dir2==null&&dir1!=null)
                dir2=parts.get(i);
        }
        if(command==null)command="";
        if(dir1!=null)dir1=dir1.replaceAll("\\*"," ");
        if(dir2!=null)dir2=dir2.replaceAll("\\*"," ");
        if(dir1==null)dir1="";
        if(dir2==null)dir2="";
        if(option==null)option="";
        if(redirect==null)redirect="";

    }
}
