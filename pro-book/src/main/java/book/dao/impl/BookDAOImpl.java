package book.dao.impl;

import book.dao.BookDAO;
import book.pojo.Book;
import myssm.basedao.BaseDAO;

import java.util.List;

public class BookDAOImpl extends BaseDAO<Book> implements BookDAO {
    @Override
    public List<Book> getBookList() {
        String sql = "select * from t_book where bookStatus = 0";
        return executeQuery(sql);
    }

    @Override
    public Book getBook(Integer id) {
        String sql = "select * from t_book where id = ?";
        return load(sql, id);
    }
}
