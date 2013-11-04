package com.dm.estore.server.servlet;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: denis
 * Date: 11/3/13
 * Time: 4:11 PM
 */
public class ApplicationFilter implements Filter {

    private static final String HTML_EXT = ".html";
    private static final String SETUP_PAGE = "setup" + HTML_EXT;
    private static final String DEFAULT_PAGE = "index" + HTML_EXT;
    private static final String DEFAULT_CONTEX = "/";

    public ApplicationFilter() {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = request instanceof HttpServletRequest ? (HttpServletRequest) request : null;
        HttpServletResponse httpResponse = (HttpServletResponse)response;

        String resourceRelativePath = httpRequest != null ? httpRequest.getRequestURI().toString() : "";

        if (DEFAULT_CONTEX.equals(resourceRelativePath)) {
            httpResponse.sendRedirect(DEFAULT_PAGE);
        } else if (systemInitialized()
                || resourceRelativePath.endsWith(SETUP_PAGE)
                || !resourceRelativePath.endsWith(HTML_EXT)) {
            filterChain.doFilter(request, response);
        } else {
            if (response instanceof HttpServletResponse) {
                ((HttpServletResponse)response).sendRedirect(SETUP_PAGE);
            }
        }
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

    private boolean systemInitialized() {
        // Not implemented
        return true;
    }
}
