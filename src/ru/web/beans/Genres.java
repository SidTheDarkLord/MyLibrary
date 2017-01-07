package ru.web.beans;

import ru.web.db.Database;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

@ManagedBean
@ApplicationScoped
public class Genres implements Serializable {

    public static boolean mark = false;
    private ArrayList<Genre> genreList = new ArrayList<>();
    private Connection conn;
    private Statement stmt;
    private ResultSet rs;

    /*
    метод, возвращающий список жанров,
    заполненный информацией из базы данных
     */
    private ArrayList<Genre> getGenres() {

        try {
            Database database = new Database();
            conn = database.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from genre order by name");
            while (rs.next()) {
                Genre genre = new Genre();
                genre.setId(rs.getLong("id"));
                genre.setName(rs.getString("name"));
                genreList.add(genre);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(rs != null) {
                    rs.close();
                }
                if(stmt != null) {
                    stmt.close();
                }
                if(conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return genreList;
    }

    public ArrayList<Genre> getGenreList() {
        if(genreList.isEmpty()) {
            return getGenres();
        } else {
            return genreList;
        }
    }

}
