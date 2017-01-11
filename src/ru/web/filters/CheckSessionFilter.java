package ru.web.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * фильтр, который выбрасывает на главную страницу по
 * истечении времени сессии
 */
@WebFilter(filterName = "CheckSessionFilter", urlPatterns = {"/pages/*"})
public class CheckSessionFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

        RequestWrapper wrappedRequest = new RequestWrapper((HttpServletRequest) req);
        ResponseWrapper wrappedResponse = new ResponseWrapper((HttpServletResponse) resp);

        HttpSession session = wrappedRequest.getSession(false);
        if(session == null || session.isNew()) {
            wrappedResponse.sendRedirect(wrappedRequest.getContextPath() + "/index.jsp");
        } else {
            chain.doFilter(req, resp);
        }
    }

    public void init(FilterConfig config) throws ServletException {

    }

    class RequestWrapper extends HttpServletRequestWrapper {

        public RequestWrapper(HttpServletRequest request) {
            super(request);
        }
    }

    class ResponseWrapper extends HttpServletResponseWrapper {

        public ResponseWrapper(HttpServletResponse response) {
            super(response);
        }
    }

}
