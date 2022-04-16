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
import com.google.gson.*;

//URL del sistema: http://localhost:8080/shopit/api


@Path("/")
public class Service {
    static DataSource pool = null;
    static {
        try {
            Context ctx = new InitialContext();
            pool = (DataSource) ctx.lookup("java:comp/env/jdbc/datasource_shopit");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static Gson j = new GsonBuilder()
		.registerTypeAdapter(byte[].class,new AdaptadorGsonBase64())
		.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
		.create();

    @GET
    @Path("/test")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response testApi() throws Exception {
        String message = "{\"hello\": \"This is a JSON response\"}";
        return Response
            .status(Response.Status.OK)
            .entity(message)
            .type(MediaType.APPLICATION_JSON)
            .build();
    }
}