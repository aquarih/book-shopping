package book.pojo;

import java.math.BigDecimal;

public class CartItem {
    private Integer id;
    private Book book;
    private Integer buyCount;
    private User userBean;
    private Double subTotal;

    public CartItem() {}

    public CartItem(Integer id, Integer buyCount) {
        this.id = id;
        this.buyCount = buyCount;
    }

    public CartItem(Book book, Integer buyCount, User user) {
        this.book = book;
        this.buyCount = buyCount;
        this.userBean = user;
    }

    public CartItem(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Integer getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(Integer buyCount) {
        this.buyCount = buyCount;
    }

    public User getUserBean() {
        return userBean;
    }

    public void setUserBean(User user) {
        this.userBean = user;
    }

    public Double getSubTotal() {
        BigDecimal bigDecimalPrice = new BigDecimal("" + getBook().getPrice());
        BigDecimal bigDecimalBuyCount = new BigDecimal("" + buyCount);
        BigDecimal bigDecimalSubTotal = bigDecimalPrice.multiply(bigDecimalBuyCount);
        subTotal = bigDecimalSubTotal.doubleValue();
        return this.subTotal;
    }
}
