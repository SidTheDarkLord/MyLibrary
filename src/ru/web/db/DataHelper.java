package ru.web.db;


import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import ru.web.hibernate.entity.Author;
import ru.web.hibernate.entity.Book;
import ru.web.hibernate.entity.Genre;
import ru.web.hibernate.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class DataHelper {

    private SessionFactory sessionFactory = null;
    private static DataHelper dataHelper;

    private DataHelper() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    public static DataHelper getInstance() {
        return dataHelper == null ? new DataHelper() : dataHelper;
    }


    public List<Book> getAllBooks() throws HibernateException {
        EntityManager entityManager = sessionFactory.createEntityManager();
        CriteriaBuilder builder = HibernateUtil.getSession().getCriteriaBuilder();
        CriteriaQuery<Book> criteria = builder.createQuery(Book.class);
        Root<Book> root = criteria.from(Book.class);
        criteria.select(root);
        List<Book> list = entityManager.createQuery(criteria).getResultList();
        return list;
        /*List<Book> list = HibernateUtil.getSession().createCriteria(Book.class).list();
        for (Book book : list) {
            System.out.println(book.getName());
        }
        return list;*/
    }

    public List<Genre> getAllGenres() {
        EntityManager entityManager = sessionFactory.createEntityManager();
        CriteriaBuilder builder = HibernateUtil.getSession().getCriteriaBuilder();
        CriteriaQuery<Genre> criteria = builder.createQuery(Genre.class);
        Root<Genre> root = criteria.from(Genre.class);
        criteria.select(root);
        List<Genre> list = entityManager.createQuery(criteria).getResultList();
        return list;
        //return HibernateUtil.getSession().createCriteria(Genre.class).list();
    }

    public List<Author> getAllAuthors() {
        EntityManager entityManager = sessionFactory.createEntityManager();
        CriteriaBuilder builder = HibernateUtil.getSession().getCriteriaBuilder();
        CriteriaQuery<Author> criteria = builder.createQuery(Author.class);
        Root<Author> root = criteria.from(Author.class);
        criteria.select(root);
        List<Author> list = entityManager.createQuery(criteria).getResultList();
        return list;
        //return HibernateUtil.getSession().createCriteria(Author.class).list();
    }

    public List<Book> getBooksByGenre(Long genreId) {
        /*
        EntityManager entityManager = sessionFactory.createEntityManager();
        CriteriaBuilder builder = HibernateUtil.getSession().getCriteriaBuilder();
        CriteriaQuery<Book> criteria = builder.createQuery(Book.class);
        Root<Book> root = criteria.from(Book.class);
        criteria.select(root);
        criteria.where(builder.equal(root.get(Book_.genreId), genreId));
        List<Book> list = entityManager.createQuery(criteria).getResultList();
        return list;*/
        return HibernateUtil.getSession().createCriteria(Book.class).add(Restrictions.eq("genre.id", genreId)).list();
    }

    public List<Book> getBooksByLetter(Character letter) {
        return getBookList("name", letter.toString(), MatchMode.START);
    }

    public List<Book> getBooksByAuthor(String authorName) {
        return getBookList("author", authorName, MatchMode.ANYWHERE);
    }

    public List<Book> getBooksByName(String bookName) {
        return getBookList("name", bookName, MatchMode.ANYWHERE);
    }

    public byte[] getContent(Long id) {
        return (byte[]) getFieldValue("content", id);
    }

    public byte[] getImage(Long id) {
        return (byte[]) getFieldValue("image", id);
    }

    public Author getAuthor(Long id) {
        return (Author) HibernateUtil.getSession().get(Author.class, id);
    }

    private List<Book> getBookList(String field, String value, MatchMode matchMode) {
        return HibernateUtil.getSession().createCriteria(Book.class).add(Restrictions.ilike(field, value, matchMode)).list();
    }

    private Object getFieldValue(String field, Long id) {
        return HibernateUtil.getSession().createCriteria(Book.class).setProjection(Projections.property(field)).add(Restrictions.eq("id", id)).uniqueResult();
    }

}
