package business;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.QueryParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Response;

import java.sql.*;
import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.*;

//URL del sistema: http://localhost:8080/shopit/api


@Path("/")
public class Service {
    private static DataSource pool = null;
    private static Gson g = new Gson();
    /* static Gson j = new GsonBuilder()
            .registerTypeAdapter(byte[].class, new AdaptadorGsonBase64())
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
            .create(); */

    static {
        try {
            Context ctx = new InitialContext();
            pool = (DataSource) ctx.lookup("java:comp/env/jdbc/datasource_shopit");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GET
    @Path("/test")
    @Produces(MediaType.APPLICATION_JSON)
    public Response testApi() throws Exception {
        String message = "{\"message\": \"Api correcta\"}";
        return Response
            .status(Response.Status.OK)
            .entity(message)
            .type(MediaType.APPLICATION_JSON)
            .build();
    }

    @POST
    @Path("/users/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(String usrStr) throws Exception{
        Usuario u = g.fromJson(usrStr, Usuario.class);
        Map<String, Object> res = new HashMap<>();

        if ( u.getName().equals("") || u.getEmail().equals("") || u.getPassword().equals("")){    
            res.put("error", "Por favor llene todos los campos");
            JsonObject jsonRes = g.toJsonTree(res).getAsJsonObject();
            return Response.status(Response.Status.BAD_REQUEST).entity(jsonRes.toString()).build();
        }

        // Verificar que el email no haya sido registrado
        Connection dbConn = pool.getConnection();
        PreparedStatement stmtUser = null;
        try {
            stmtUser = dbConn.prepareStatement("SELECT * FROM users WHERE email = ?");
            try {
                stmtUser.setString(1, u.getEmail());
                ResultSet rs = stmtUser.executeQuery();
                try {
                    if (rs.next()){
                        res.put("error", "El email ya ha sido registrado");
                        JsonObject jsonRes = g.toJsonTree(res).getAsJsonObject();
                        return Response.status(Response.Status.BAD_REQUEST).entity(jsonRes.toString()).build();
                    }

                    String pwdHsh = BCrypt.hashpw(u.getPassword(), BCrypt.gensalt(12));
                    // Registrar el usuario
                    stmtUser = dbConn.prepareStatement("INSERT INTO users VALUES (0,?,?,?,?,?,?");

                    stmtUser.setString(1, u.getName());
                    stmtUser.setString(2, u.getLastName());
                    stmtUser.setString(3, u.getEmail());
                    stmtUser.setString(4, u.getPassword());
                    stmtUser.setString(5, u.getPhone());
                    stmtUser.setString(6, u.getGender());

                    stmtUser.executeUpdate();

                    res.put("message", "Se ha registrado el usuario correctamente");
                    JsonObject jsonRes = g.toJsonTree(res).getAsJsonObject();
                    return Response.status(Response.Status.OK).entity(jsonRes.toString()).build();

                } finally {
                    rs.close();
                }
            }finally {
                stmtUser.close();
            }
        }catch(Exception ex){
            res.put(("error"), ex.getMessage());
            JsonObject jsonRes = g.toJsonTree(res).getAsJsonObject();
            return Response.status(Response.Status.BAD_REQUEST).entity(jsonRes.toString()).build();
        }finally {
            stmtUser.close();
            dbConn.close();
        }
    }

}