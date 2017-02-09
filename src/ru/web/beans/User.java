package ru.web.beans;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ResourceBundle;

@ManagedBean
@SessionScoped
public class User implements Serializable {

    private String username;
    private String password;

    public User() {
    }

    public String login() {
        try {
            ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).login(username, password);
            return "books";
        } catch (ServletException e) {
            ResourceBundle bundle = ResourceBundle.getBundle("ru.web.nls.messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());
            e.printStackTrace();
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage message = new FacesMessage(bundle.getString("login_error"));
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            context.addMessage("login_form", message);
        }
        return "index";
    }

    public String logout() {
        String result = "/index.xhtml?faces-redirect=true";
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            request.logout();
        } catch (ServletException e) {
            e.printStackTrace();
        }
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

        return result;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
