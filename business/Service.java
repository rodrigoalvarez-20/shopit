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
import javax.ws.rs.HeaderParam;
//import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;


import java.sql.*;
import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.google.gson.*;

//URL del sistema: http://localhost:8080/shopit/api

@Path("/")
public class Service {
    private static DataSource pool = null;
    private static Gson g = new Gson();
    private static String _TK = "YzJodmNHbDBYM05sY25acFkyVmZaVzVq";

    static {
        try {
            Context ctx = new InitialContext();
            pool = (DataSource) ctx.lookup("java:comp/env/jdbc/datasource_shopit");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Object> validateToken(String userToken) {
        Map<String, Object> res = new HashMap<>();
        if (userToken == null || userToken.equals("")) {
            res.put(("error"), "Encabezado no encontrado");
            return res;
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(_TK);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("shopit_service")
                    .build();
            DecodedJWT jwt = verifier.verify(userToken);

            res.put("message", "Ok");
            res.put("id", jwt.getClaim("id"));
            res.put("email", jwt.getClaim("email"));
            return res;

        } catch (JWTVerificationException verifError) {
            System.out.println(verifError.getMessage());
            res.put("error", "Token invalida");
            return res;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            res.put("error", "Ha ocurrido un error al verificar la token");
            return res;
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
    public Response registerUser(String usrStr) throws Exception {
        Usuario u = g.fromJson(usrStr, Usuario.class);
        Map<String, Object> res = new HashMap<>();

        if (u.getName().equals("") || u.getEmail().equals("") || u.getPassword().equals("")) {
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
                    if (rs.next()) {
                        res.put("error", "El email ya ha sido registrado");
                        JsonObject jsonRes = g.toJsonTree(res).getAsJsonObject();
                        return Response.status(Response.Status.BAD_REQUEST).entity(jsonRes.toString()).build();
                    }

                    String pwdHsh = BCrypt.hashpw(u.getPassword(), BCrypt.gensalt(12));
                    u.setPassword(pwdHsh);
                    // Registrar el usuario
                    stmtUser = dbConn.prepareStatement("INSERT INTO users VALUES (0,?,?,?,?,?,?);");

                    stmtUser.setString(1, u.getName());
                    stmtUser.setString(2, u.getLastName());
                    stmtUser.setString(3, u.getEmail());
                    stmtUser.setString(4, u.getPassword());
                    stmtUser.setString(5, u.getPhone());
                    stmtUser.setString(6, u.getGender());

                    if (stmtUser.executeUpdate() != 0) {
                        res.put("message", "Se ha registrado el usuario correctamente");
                        JsonObject jsonRes = g.toJsonTree(res).getAsJsonObject();
                        return Response.status(Response.Status.OK).entity(jsonRes.toString()).build();
                    } else {
                        res.put("error", "No se ha podido registrar el usuario");
                        JsonObject jsonRes = g.toJsonTree(res).getAsJsonObject();
                        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonRes.toString())
                                .build();
                    }
                } finally {
                    rs.close();
                }
            } finally {
                stmtUser.close();
            }
        } catch (Exception ex) {
            res.put(("error"), ex.getMessage());
            JsonObject jsonRes = g.toJsonTree(res).getAsJsonObject();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonRes.toString()).build();
        } finally {
            stmtUser.close();
            dbConn.close();
        }
    }

    @POST
    @Path("/users/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUser(String usrStr) throws Exception {
        Usuario u = g.fromJson(usrStr, Usuario.class);
        Map<String, Object> res = new HashMap<>();

        if (u.getEmail().equals("") || u.getPassword().equals("")) {
            res.put("error", "No puede dejar los campos en blanco");
            JsonObject jsonRes = g.toJsonTree(res).getAsJsonObject();
            return Response.status(Response.Status.BAD_REQUEST).entity(jsonRes.toString()).build();
        }

        // Verificar que el email si exista
        Connection dbConn = pool.getConnection();
        PreparedStatement stmtUser = null;

        try {
            stmtUser = dbConn.prepareStatement("SELECT * FROM users WHERE email = ?");
            try {
                stmtUser.setString(1, u.getEmail());
                ResultSet rs = stmtUser.executeQuery();
                try {
                    if (!rs.next()) {
                        res.put("error", "Las credenciales son incorrectas");
                        JsonObject jsonRes = g.toJsonTree(res).getAsJsonObject();
                        return Response.status(Response.Status.NOT_FOUND).entity(jsonRes.toString()).build();
                    }
                    try {

                        // Validar la contraseña 
                        if (!BCrypt.checkpw(u.getPassword(), rs.getString(5))){
                            res.put("error", "Las credenciales son incorrectas");
                            JsonObject jsonRes = g.toJsonTree(res).getAsJsonObject();
                            return Response.status(Response.Status.BAD_REQUEST).entity(jsonRes.toString()).build();
                        }
                        
                        Algorithm algorithm = Algorithm.HMAC256(_TK);
                        String token = JWT.create()
                                .withClaim("id", rs.getString(1))
                                .withClaim("name", rs.getString(2))
                                .withClaim("email", u.getEmail())
                                .withClaim("rd", new Random().nextInt(100000))
                                .withIssuer("shopit_service")
                                .sign(algorithm);
                        res.put("message", "Inicio de sesión correcto");
                        res.put("token", token);
                        JsonObject jsonRes = g.toJsonTree(res).getAsJsonObject();
                        return Response.status(Response.Status.OK).entity(jsonRes.toString()).build();

                    } catch (JWTCreationException exception) {
                        // Invalid Signing configuration / Couldn't convert Claims.
                        System.out.println(exception.getMessage());
                        res.put(("error"), "Ha ocurrido un error al crear la token");
                        JsonObject jsonRes = g.toJsonTree(res).getAsJsonObject();
                        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonRes.toString()).build();
                    }
                } finally {
                    rs.close();
                }
            } finally {
                stmtUser.close();
            }
        } catch (Exception ex) {
            res.put(("error"), ex.getMessage());
            JsonObject jsonRes = g.toJsonTree(res).getAsJsonObject();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonRes.toString()).build();
        } finally {
            stmtUser.close();
            dbConn.close();
        }

    }

    @GET
    @Path("/users/me")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserProfile(@HeaderParam("Authorization") String auth){
        Map<String, Object> res = new HashMap<>();

        res = validateToken(auth);

        if(res.containsKey("error")){
            JsonObject jsonRes = g.toJsonTree(res).getAsJsonObject();
            return Response.status(Response.Status.BAD_REQUEST).entity(jsonRes.toString()).build();    
        }

        //res.put("message", "Ok");
        //res.put("data", res.get("id"));
        //res.put("data", res.get("id"));
        JsonObject jsonRes = g.toJsonTree(res).getAsJsonObject();
        return Response.status(Response.Status.OK).entity(jsonRes.toString()).build();
    }

}