package ru.web.controllers;


import org.hibernate.HibernateException;
import ru.web.db.DataHelper;
import ru.web.enums.SearchType;
import ru.web.hibernate.entity.Book;
import ru.web.hibernate.util.HibernateUtil;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ManagedBean(eager = true)
@SessionScoped
public class BookListController implements Serializable {

    private ArrayList<Integer> pageNumbers = new ArrayList<>();  //список из номеров страниц
    private List<Book> currentBookList;  //текущий список книг для отображения
    //критерии поиска---------
    private long selectedGenreId;  //выбранный жанр
    private char selectedLetter;  //выбранная буква алфавита, по умолчанию ни одна не выбрана
    private Long selectedAuthorId;// текущий автор книги из списка при редактировании книги
    private SearchType selectedSearchType = SearchType.TITLE;  //хранит выбранный тип поиска
    private String searchString;  //хранит строку поиска
    //для постраничности------
    private long totalBooksCount; //общее кол-во книг, полученных по sql запросу
    private int booksOnPage = 5;  //кол-во книг на странице
    private long selectedPageNumber = 1;  //выбранный номер страницы
    private boolean pageSelected;
    //------
    private boolean editMode;  //режим отображения

    public BookListController() {
        fillBooksAll();
    }

    private void submitValues(char selectedLetter, long selectedGenreId, long selectedPageNumber, boolean requestFromPager) {
        this.selectedLetter = selectedLetter;
        this.selectedGenreId = selectedGenreId;
        this.selectedPageNumber = selectedPageNumber;
        this.pageSelected = requestFromPager;
    }

    private void fillBooksAll() {
        try {
          currentBookList = DataHelper.getInstance().getAllBooks();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    public String fillBooksByGenre() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        selectedGenreId = Long.valueOf(params.get("genre_id"));
        submitValues(' ', selectedGenreId, 1, false);

        currentBookList = DataHelper.getInstance().getBooksByGenre(selectedGenreId);
        return "books";
    }

    public String fillBooksBySearch() {

        submitValues(' ', -1, 1, false);

        if(searchString.trim().length() == 0) {
            fillBooksAll();
            return "books";
        }

        if(selectedSearchType == SearchType.AUTHOR) {
            currentBookList = DataHelper.getInstance().getBooksByAuthor(searchString);
        } else if (selectedSearchType == SearchType.TITLE) {
            currentBookList = DataHelper.getInstance().getBooksByName(searchString);
        }
        return "books";
    }

    public String  fillBooksByLetter() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        selectedLetter = params.get("letter").charAt(0);
        submitValues(selectedLetter, -1, 1, false);

        currentBookList = DataHelper.getInstance().getBooksByLetter(selectedLetter);
        return "books";
    }


    public void updateBooks() {
        cancelEdit();
    }

    public void cancelEdit() {
        editMode = false;
        for (Book book : currentBookList) {
            book.setEdit(false);
        }
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void switchEditMode() {
        editMode = !editMode;
    }


    public Character[] getRussianLetters() {
        return new Character[]{'А', 'Б','В','Г','Д','Е','Ж','З','И','К','Л','М','Н','О','П','Р','С',
                'Т','У','Ф','Х','Ц','Ч','Ш','Щ','Э','Ю','Я'};
    }

    public void searchTypeChanged(ValueChangeEvent e) {
        selectedSearchType = (SearchType) e.getNewValue();
    }

    public void searchStringChanged(ValueChangeEvent e) {
        searchString = e.getNewValue().toString();
    }


    //постраничность
    private void fillPageNumbers(long totalBooksCount, int booksOnPage) {
        int pageCount = totalBooksCount > 0 ? (int) Math.ceil((double) totalBooksCount/booksOnPage) : 0;
        pageNumbers.clear();
        for (int i = 1; i <= pageCount; i++) {
            pageNumbers.add(i);
        }
    }

    public void booksOnPageChanged(ValueChangeEvent e) {
        /*cancelEdit();
        pageSelected = false;
        booksOnPage = Integer.valueOf(e.getNewValue().toString());
        selectedPageNumber = 1;
        fillBooksBySQL(currentSql);*/
    }
    public void selectPage() {
        /*cancelEdit();
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        selectedPageNumber = Integer.valueOf(params.get("page_number"));
        pageSelected = true;
        fillBooksBySQL(currentSql);
        return "books";*/
    }


    //геттеры и сеттеры
    public ArrayList<Integer> getPageNumbers() {
        return pageNumbers;
    }

    public long getSelectedGenreId() {
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

    public void setBooksOnPage(int booksOnPage) {
        this.booksOnPage = booksOnPage;
    }

    public long getTotalBooksCount() {
        return totalBooksCount;
    }

    public SearchType getSelectedSearchType() {
        return selectedSearchType;
    }

    public void setSelectedSearchType(SearchType selectedSearchType) {
        this.selectedSearchType = selectedSearchType;
    }

    public List<Book> getCurrentBookList() {
        return currentBookList;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public Long getSelectedAuthorId() {
        return selectedAuthorId;
    }

    public void setSelectedAuthorId(Long selectedAuthorId) {
        this.selectedAuthorId = selectedAuthorId;
    }
}
