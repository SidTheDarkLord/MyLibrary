package ru.web.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.Locale;

@ManagedBean(eager = true)
@SessionScoped
public class LocaleChanger implements Serializable {

    private Locale currentLocale = new Locale("ru");

    public LocaleChanger() {
    }

    public Locale getCurrentLocale() {
        return currentLocale;
    }

    public void changeLocale(String localeCode) {
        currentLocale = new Locale(localeCode);
    }
}
