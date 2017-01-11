package ru.web.servlets;

import ru.web.controllers.SearchController;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet(name = "PdfContent", urlPatterns = {"/PdfContent"})
public class PdfContent extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/pdf");
        try(OutputStream out = response.getOutputStream()) {
            int id = Integer.valueOf(request.getParameter("id"));
            SearchController searchController = (SearchController) request.getSession(false).getAttribute("searchController");
            byte[] content = searchController.getContent(id);
            response.setContentLength(content.length);
            out.write(content);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
