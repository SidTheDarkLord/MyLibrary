package ru.web.controllers;

import ru.web.db.DataHelper;
import ru.web.hibernate.entity.Genre;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ManagedBean(eager = false)
@ApplicationScoped
public class GenreController implements Serializable {

    private List<SelectItem> selectItems = new ArrayList<>();
    private Map<Long, Genre> genreMap;
    private List<Genre> genreList;
    /*
        конструктор сразу заполняет список жанров из базы данных
     */
    public GenreController() {
        genreMap = new HashMap<>();
        genreList = DataHelper.getInstance().getAllGenres();

        for (Genre genre : genreList) {
            genreMap.put(genre.getId(), genre);
            selectItems.add(new SelectItem(genre, genre.getName()));
        }
    }

    public List<SelectItem> getSelectItems() {
        return selectItems;
    }

    public List<Genre> getGenreList() {
        return genreList;
    }


    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return genreMap.get(Long.valueOf(value));
    }


    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return ((Genre) value).getId().toString();
    }
}
