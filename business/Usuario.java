package business;

import com.google.gson.*;

public class Usuario {
    private String name, last_name, email, password, phone, gender;

    public Usuario(String name, String lastName, String email, String pwd, String phone, String gender){
        this.name = name;
        this.last_name = lastName;
        this.email = email;
        this.password = pwd;
        this.phone = phone;
        this.gender = gender;
    }

    public String getName(){
        return name;
    }

    public String getLastName(){
        return last_name;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    public String getPhone(){
        return phone;
    }

    public String getGender(){
        return gender;
    }

    public void setName(String v){
        this.name = v;
    }

    public void setLastName(String v){
        this.last_name = v;
    }

    public void setEmail(String v){
        this.email = v;
    }

    public void setPassword(String v){
        this.password = v;
    }

    public void setPhone(String v){
        this.phone = v;
    }

    public void setGender(String v){
        this.gender = v;
    }

    public static Usuario valueOf(String s) throws Exception {
        Gson j = new GsonBuilder().registerTypeAdapter(byte[].class, new AdaptadorGsonBase64()).create();
        return (Usuario) j.fromJson(s, Usuario.class);
    }
}
