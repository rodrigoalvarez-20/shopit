package business;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

public class Utils {
    private final String _TK = "YzJodmNHbDBYM05sY25acFkyVmZaVzVq";

    public String generateToken(int id, String name, String email) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(_TK);
            String token = JWT.create()
                .withClaim("id", id)
                .withClaim("name", name)
                .withClaim("email", email)
                .withClaim("rd", new Random().nextInt(100000))
                .withIssuer("shopit_service")
                .sign(algorithm);
            return token;
        }catch(JWTCreationException exception){
            System.out.println(exception.getMessage());
            return null;
        }
    }

    public Map<String, Object> validateToken(String tk){
        Map<String, Object> res = new HashMap<>();
        if(tk.equals(null) || tk.isBlank()){
            res.put(("error"), "Encabezado no encontrado");
            return res;
        }

        try {
            Algorithm algorithm = Algorithm.HMAC256(_TK);
            JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("shopit_service")
                .build();
            DecodedJWT jwt = verifier.verify(token);
            
            res.put("data", jwt.getPayload());
            return res;

        }catch(JWTVerificationException verifError){
            System.out.println(verifError.getMessage());
            res.put("error", "Token invalida");
            return res;
        }

    }


}
