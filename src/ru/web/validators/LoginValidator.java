package ru.web.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.ArrayList;
import java.util.ResourceBundle;

@FacesValidator("ru.web.validators.LoginValidator")
public class LoginValidator implements Validator {
    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {

        ResourceBundle bundle = ResourceBundle.getBundle("ru.web.nls.messages",
                FacesContext.getCurrentInstance().getViewRoot().getLocale());
        try {
            String newValue = o.toString();
            if(newValue.length() < 3) {
                throw new IllegalArgumentException(bundle.getString("login_length_error"));
            }
            if(Character.isLetter(newValue.charAt(0))) {
                throw new IllegalArgumentException(bundle.getString("first_letter_error"));
            }
            if(getUsedNames().contains(newValue)) {
                throw new IllegalArgumentException(bundle.getString("used_name"));
            }

        } catch (IllegalArgumentException e) {
            FacesMessage message = new FacesMessage(e.getMessage());
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
    }

    private ArrayList<String> getUsedNames() {
        //заглушка, нужно подгружать список имен из базы данных
        ArrayList<String> list = new ArrayList<>();
        list.add("username");
        list.add("login");
        return list;
    }
}
