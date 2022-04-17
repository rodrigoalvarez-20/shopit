package business;

import java.util.Date;

public class Product {
    private int id, stock;
    private String image, sku, name, category;
    private double price;
    private Date createdAt;

    public Product(int id, String image, String sku, String name, double price, String cat, int stock, Date createdAt){
        this.id = id;
        this.image = image;
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.category = cat;
        this.stock = stock;
        this.createdAt = createdAt;
    }

    public int getId(){
        return this.id;
    }

    public String getImage(){
        return this.image;
    }

    public String getSku(){
        return this.sku;
    }

    public String getName(){
        return this.name;
    }

    public double getPrice(){
        return this.price;
    }

    public String getCategory(){
        return this.category;
    }

    public int getStock(){
        return this.stock;
    }

    public Date getCreatedAt(){
        return this.createdAt;
    }

}
