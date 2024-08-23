package com.techsol.web;

import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import org.apache.wicket.protocol.http.WicketFilter;

public class CustomWicketFilter extends WicketFilter {
    @Override
    public void init(boolean isServlet, FilterConfig filterConfig) throws ServletException {
        super.init(isServlet, filterConfig);
        System.out.println("CustomWicketFilter init");
        System.out.println("Filter config : " + getFilterConfig().toString());
    }
}
