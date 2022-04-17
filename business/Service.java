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

import java.io.Console;
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
    static DataSource pool = null;
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
    public Response registerUser(String usrStr){
        
        Gson g = new Gson();
        Usuario u = g.fromJson(usrStr, Usuario.class);
        
        if (u.getEmail().equals("") || u.getPassword().equals("")){
            Map<String,Object> res = new HashMap<>();
            res.put("error", "No puede dejar el email y/o el password en blanco");
            JsonObject jsonRes = g.toJsonTree(res).getAsJsonObject();
            return Response.status(Response.Status.BAD_REQUEST).entity(jsonRes.toString()).build();
        }

        String message = "{\"message\": \"Ok\"}";
        return Response.status(Response.Status.OK)
        .entity(message).build();
    }

}