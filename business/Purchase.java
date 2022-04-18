package business;
import java.util.Date;

public class Purchase {
    private int id, id_usuario, total_products;
    private double total;
    private Date createdAt;

    public Purchase(int id, int id_usr, int t_prods, double total, Date created){
        this.id = id;
        this.id_usuario = id_usr;
        this.total_products = t_prods;
        this.total = total;
        this.createdAt = created;
    }

    public int getId(){
        return this.id;
    }

    public int getUserId(){
        return this.id_usuario;
    }

    public int getTotalProducts(){
        return this.total_products;
    }

    public double getTotal(){
        return this.total;
    }

    public Date getCreatedAt(){
        return this.createdAt;
    }




}
