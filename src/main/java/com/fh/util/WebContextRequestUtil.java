package com.fh.util;

import javax.servlet.http.HttpServletRequest;

public class WebContextRequestUtil {
   private static ThreadLocal<HttpServletRequest> threadLocal =new ThreadLocal<>();

    public static void setRequest(HttpServletRequest request){
        threadLocal.set(request);
    }

    public static HttpServletRequest getRequest(){
        return  threadLocal.get();
    }

    public static void remove(){
        threadLocal.remove();
    }

}
