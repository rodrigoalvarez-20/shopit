import java.util.Date;

public class Products {
    private int id, stock;
    private String image, sku, name, category;
    private double price;
    private Date createdAt;

    public Products(int id, String image, String sku, String name, double price, String cat, int stock, Date createdAt){
        this.id = id;
        this.image = image;
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.category = cat;
        this.stock = stock;
        this.createdAt = createdAt;
    }

}
