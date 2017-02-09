package ru.web.servlets;


import ru.web.db.DataHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

@WebServlet(name = "PdfContent", urlPatterns = {"/PdfContent"})
public class PdfContent extends HttpServlet {

    private void processRequest(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/pdf");
        try(OutputStream out = response.getOutputStream()) {
            long id = Long.valueOf(request.getParameter("id"));
            boolean save = Boolean.valueOf(request.getParameter("save"));
            String filename = request.getParameter("filename");
            byte[] content = DataHelper.getInstance().getContent(id);
            response.setContentLength(content.length);
            if(save) {
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8") + ".pdf");
            }
            out.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
