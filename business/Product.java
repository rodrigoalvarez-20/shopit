package business;

import java.util.Date;

public class Product {
    private int id, stock, quantity;
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

    public Product(int id, String image, String sku, String name, double price, String cat, int stock, Date createdAt, int quantity) {
        this.id = id;
        this.image = image;
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.category = cat;
        this.stock = stock;
        this.createdAt = createdAt;
        this.quantity = quantity;
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

    public int getQuantity(){
        return this.quantity;
    }

    public void setImage(String v){
        this.image = v;
    }

    public void setSku(String v){
        this.sku = v;
    }

    public void setName(String v){
        this.name = v;
    }

    public void setPrice(double v){
        this.price = v;
    }

    public void setCategory(String v){
        this.category = v;
    }

    public void setStock(int v){
        this.stock = v;
    }

    public void setQuantity(int v){
        this.quantity = v;
    }

}
