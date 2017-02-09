package ru.web.servlets;


import ru.web.db.DataHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;


@WebServlet(name = "ShowImage", urlPatterns = {"/ShowImage"})
public class ShowImage extends HttpServlet {

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");
        try(OutputStream out = response.getOutputStream()) {
            long id = Long.valueOf(request.getParameter("id"));
            byte[] image = DataHelper.getInstance().getImage(id);
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
