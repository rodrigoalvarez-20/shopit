package business;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.auth0.jwt.*;

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

    public Map<String, Object> validateToken(String userToken){
        Map<String, Object> res = new HashMap<>();
        System.out.println(userToken);
        if(userToken == null || userToken.isBlank()){
            res.put(("error"), "Encabezado no encontrado");
            return res;
        }

        try {
            Algorithm algorithm = Algorithm.HMAC256(_TK);
            JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("shopit_service")
                .build();
            DecodedJWT jwt = verifier.verify(userToken);
            
            res.put("data", jwt.getPayload());
            return res;

        }catch(JWTVerificationException verifError){
            System.out.println(verifError.getMessage());
            res.put("error", "Token invalida");
            return res;
        }

    }


}
