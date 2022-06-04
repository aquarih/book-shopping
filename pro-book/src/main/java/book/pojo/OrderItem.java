package book.pojo;

public class OrderItem {
    private Integer id;
    private Book book;
    private Integer buyCount;
    private Order order; //N:1

    public OrderItem() {}

    public OrderItem(Integer id) {
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

    public Order getOrderBean() {
        return order;
    }

    public void setOrderBean(Order order) {
        this.order = order;
    }
}
