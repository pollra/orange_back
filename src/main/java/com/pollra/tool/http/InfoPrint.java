package com.pollra.tool.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

public class InfoPrint {
    public InfoPrint() {
    }

    public static void headersPrint(HttpServletRequest request){
        Enumeration headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()){
            String name = (String)headerNames.nextElement();
            String value = request.getHeader(name);
            System.out.println("name["+name + "] : value[" + value+"]");
        }
        System.out.println("");
        Enumeration params = request.getParameterNames();
        while(params.hasMoreElements()){
            String name2 = (String)params.nextElement();
            String value2 = request.getParameter(name2);
            System.out.println("name["+name2 + "] : value[" + value2+"]");
        }

        /*Enumeration attribute = request.getAttributeNames();
        while(attribute.hasMoreElements()){
            String name3 = (String)attribute.nextElement();
            String value3 = (String) request.getAttribute(name3);
            System.out.println(name3 + " : " + value3);
        }
        System.out.println("");*/
    }

    public static void headersPrint(HttpServletResponse response){
        Enumeration headerNames = (Enumeration) response.getHeaderNames();
        while(headerNames.hasMoreElements()){
            String name = (String)headerNames.nextElement();
            String value = response.getHeader(name);
            System.out.println("name["+name + "] : value[" + value+"]");
        }
        System.out.println("");
        /*Enumeration attribute = request.getAttributeNames();
        while(attribute.hasMoreElements()){
            String name3 = (String)attribute.nextElement();
            String value3 = (String) request.getAttribute(name3);
            System.out.println(name3 + " : " + value3);
        }
        System.out.println("");*/
    }
}
