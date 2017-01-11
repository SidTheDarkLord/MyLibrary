package ru.web.controllers;


import ru.web.beans.Book;
import ru.web.db.Database;
import ru.web.enums.SearchType;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

@ManagedBean(eager = true)
@SessionScoped
public class SearchController implements Serializable {

    private int booksOnPage = 2;  //кол-во книг на странице
    private int selectedGenreId;
    private char selectedLetter;
    private long selectedPageNumber = 1;
    private boolean requestFromPager;
    private long totalBooksCount; //общее кол-во книг, полученных по sql запросу
    private ArrayList<Integer> pageNumbers = new ArrayList<>();  //список из номеров страниц
    private SearchType searchType;  //хранит выбранный тип поиска
    private String searchString;  //хранит строку поиска
    private static Map<String, SearchType> searchList = new HashMap<>();
    private ArrayList<Book> currentBookList;  //текущий список книг для отображения
    private String currentSql;  //последний выполненный sql запрос без добавления limit

    public SearchController() {
        fillBooksAll();

        ResourceBundle bundle = ResourceBundle.getBundle("ru.web.nls.messages",
                FacesContext.getCurrentInstance().getViewRoot().getLocale());
        searchList.put(bundle.getString("author_name"), SearchType.AUTHOR);
        searchList.put(bundle.getString("book_name"), SearchType.TYTLE);
    }

    private void fillBooksBySQL (String sql) {
        StringBuilder sqlBuilder = new StringBuilder(sql);
        currentSql = sql;

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Database database = new Database();
            conn = database.getConnection();
            stmt = conn.createStatement();

            if(!requestFromPager) {
                rs = stmt.executeQuery(sqlBuilder.toString());
                rs.last();
                totalBooksCount = rs.getRow();
                fillPageNumbers(totalBooksCount, booksOnPage);
            }

            if(totalBooksCount > booksOnPage) {
                sqlBuilder.append(" LIMIT ").append(selectedPageNumber * booksOnPage - booksOnPage).append(", ").append(booksOnPage);
            }

            rs = stmt.executeQuery(sqlBuilder.toString());
            currentBookList = new ArrayList<>();

            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getLong("id"));
                book.setName(rs.getString("name"));
                book.setPageCount(rs.getInt("page_count"));
                book.setIsbn(rs.getString("isbn"));
                book.setGenre(rs.getString("genre"));
                book.setAuthor(rs.getString("author"));
                book.setPublishDate(rs.getInt("publish_year"));
                book.setPublisher(rs.getString("publisher"));
                //book.setImage(rs.getBytes("image"));
                //book.setContent(rs.getBytes("content"));
                book.setDescr(rs.getString("descr"));
                currentBookList.add(book);
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
    }

    private void fillBooksAll() {
        fillBooksBySQL("SELECT b.id, b.name, b.page_count, b.isbn, g.name as genre, a.fio as author, b.publish_year, " +
                "p.name as publisher, b.image, b.descr " +
                "FROM book b " +
                "INNER JOIN genre g ON b.genre_id = g.id " +
                "INNER JOIN author a ON b.author_id = a.id " +
                "INNER JOIN publisher p ON b.publisher_id = p.id " +
                "ORDER BY b.name");
    }

    private void submitValues(char selectedLetter, int selectedGenreId, long selectedPageNumber, boolean requestFromPager) {
        this.selectedLetter = selectedLetter;
        this.selectedGenreId = selectedGenreId;
        this.selectedPageNumber = selectedPageNumber;
        this.requestFromPager = requestFromPager;
    }

    public String fillBooksByGenre() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        submitValues(' ', Integer.valueOf(params.get("genre_id")), 1, false);

        fillBooksBySQL("SELECT b.id, b.name, b.page_count, b.isbn, g.name as genre, a.fio as author, b.publish_year, " +
                "p.name as publisher, b.image, b.descr " +
                "FROM book b " +
                "INNER JOIN genre g ON b.genre_id = g.id " +
                "INNER JOIN author a ON b.author_id = a.id " +
                "INNER JOIN publisher p ON b.publisher_id = p.id " +
                "where g.id = " + selectedGenreId + " " +
                "ORDER BY b.name");
        return "book";
    }

    public void fillBooksBySearch() {

        submitValues(' ', -1, 1, false);

        if(searchString.trim().length() == 0) {
            fillBooksAll();
            return;
        }

        StringBuilder sql = new StringBuilder("SELECT b.id, b.name, b.page_count, b.isbn, g.name as genre, a.fio as author, b.publish_year, " +
                "p.name as publisher, b.image, b.descr " +
                "FROM book b " +
                "INNER JOIN genre g ON b.genre_id = g.id " +
                "INNER JOIN author a ON b.author_id = a.id " +
                "INNER JOIN publisher p ON b.publisher_id = p.id ");

        if(searchType == SearchType.AUTHOR) {
            sql.append("WHERE LOWER(a.fio) LIKE '%" + searchString.toLowerCase() + "%' ORDER BY b.name");
        } else if (searchType == SearchType.TYTLE) {
            sql.append("WHERE LOWER(b.name) LIKE '%" + searchString.toLowerCase() + "%' ORDER BY b.name");
        }

        fillBooksBySQL(sql.toString());
    }

    public void fillBooksByLetter() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        submitValues(params.get("letter").charAt(0), -1, 1, false);

        fillBooksBySQL("SELECT b.id, b.name, b.page_count, b.isbn, g.name as genre, a.fio as author, b.publish_year, " +
                "p.name as publisher, b.image, b.descr " +
                "FROM book b " +
                "INNER JOIN genre g ON b.genre_id = g.id " +
                "INNER JOIN author a ON b.author_id = a.id " +
                "INNER JOIN publisher p ON b.publisher_id = p.id " +
                "WHERE substr(b.name,1,1) = '" + selectedLetter + "' " +
                "ORDER BY b.name");
        selectedGenreId = -1;
        selectedPageNumber = 1;
    }

    public byte[] getImage(int id) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        byte[] image = null;

        try {
            Database database = new Database();
            conn = database.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT image FROM book WHERE id = " + id);
            while (rs.next()) {
                image = rs.getBytes("image");
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
        return image;
    }

    public Character[] getRussianLetters() {
        Character[] letters = {'А', 'Б','В','Г','Д','Е','Ж','З','И','К','Л','М','Н','О','П','Р','С',
                'Т','У','Ф','Х','Ц','Ч','Ш','Щ','Э','Ю','Я'};
        return letters;
    }

    private void fillPageNumbers(long totalBooksCount, int booksOnPage) {
        int pageCount = totalBooksCount > 0 ? (int) Math.ceil((double) totalBooksCount/booksOnPage) : 0;
        pageNumbers.clear();
        for (int i = 1; i <= pageCount; i++) {
            pageNumbers.add(i);
        }
    }

    public String selectPage() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        selectedPageNumber = Integer.valueOf(params.get("page_number"));
        requestFromPager = true;
        fillBooksBySQL(currentSql);
        return "books";
    }

    public byte[] getContent(int id) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        byte[] content = null;

        try {
            Database database = new Database();
            conn = database.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT content FROM book WHERE id=" + id);

            while (rs.next()) {
                content = rs.getBytes("content");
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
        return content;
    }

    public ArrayList<Integer> getPageNumbers() {
        return pageNumbers;
    }

    public int getSelectedGenreId() {
        return selectedGenreId;
    }

    public char getSelectedLetter() {
        return selectedLetter;
    }

    public long getSelectedPageNumber() {
        return selectedPageNumber;
    }

    public int getBooksOnPage() {
        return booksOnPage;
    }

    public long getTotalBooksCount() {
        return totalBooksCount;
    }

    public SearchType getSearchType() {
        return searchType;
    }

    public void setSearchType(SearchType searchType) {
        this.searchType = searchType;
    }

    public Map<String, SearchType> getSearchList() {
        return searchList;
    }

    public ArrayList<Book> getCurrentBookList() {
        return currentBookList;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
}
