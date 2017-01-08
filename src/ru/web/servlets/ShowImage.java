package ru.web.servlets;

import ru.web.controllers.SearchController;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;


@WebServlet(name = "ShowImage")
public class ShowImage extends HttpServlet {

    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");
        try(OutputStream out = response.getOutputStream()) {
            int id = Integer.valueOf(request.getParameter("id"));
            SearchController searchController = (SearchController) request.getSession(false).getAttribute("searchController");
            byte[] image = searchController.getImage(id);
            response.setContentLength(image.length);
            out.write(image);
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
