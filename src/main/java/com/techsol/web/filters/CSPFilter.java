package com.techsol.web.filters;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

public class CSPFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;
        res.setHeader("Content-Security-Policy",
                "default-src 'self'; style-src 'self' https://stackpath.bootstrapcdn.com; script-src 'self' https://stackpath.bootstrapcdn.com https://code.jquery.com;");
        chain.doFilter(request, response);
    }

}
