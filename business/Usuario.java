package business;

import com.google.gson.*;

public class Usuario {
    String name;
    String last_name;
    String email;
    String password;
    String phone;
    String gender;
    String createdAt;    

    public static Usuario valueOf(String s) throws Exception {
        Gson j = new GsonBuilder().registerTypeAdapter(byte[].class, new AdaptadorGsonBase64()).create();
        return (Usuario) j.fromJson(s, Usuario.class);
    }
}
