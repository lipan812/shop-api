package com.fh.common;

import com.fh.util.WebContextRequestUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class WebContextFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        WebContextRequestUtil.setRequest((HttpServletRequest) request);
        try {
            chain.doFilter(request,response);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }finally {
            WebContextRequestUtil.remove();
        }
    }

    @Override
    public void destroy() {

    }
}
