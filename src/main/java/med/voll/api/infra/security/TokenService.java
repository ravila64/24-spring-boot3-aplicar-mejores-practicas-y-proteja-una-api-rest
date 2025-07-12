package med.voll.api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import med.voll.api.domain.usuario.Usuario;
//import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
//import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

   @Value("${api.security.token.secret}")
   private String secret;  // properties

   public String generarToken(Usuario usuario){
      // este algoritmo se busca en jwt.io / java / github / buscar create token
      try {
         //Algorithm algorithm = Algorithm.RSA256(rsaPublicKey, rsaPrivateKey);
         var algoritmo = Algorithm.HMAC256(secret);
         System.out.println("algoritmo "+algoritmo);
         var token = JWT.create()  // se quito la asignacion al token y se dejo return
               .withIssuer("API Voll.med")  // auth0, empresa firma el token
               .withSubject(usuario.getLogin()) // trae el user or email, segun solicitud
               .withExpiresAt(fechaExpiracion())  // fecha u hora que expira el token, tiene que logearse otra vez
               //.withClaim("id", usuario.getId()) // usuarios llave valor
               // se pueden colocar varios
               .sign(algoritmo);
         System.out.println("token en generarToken "+token);
         return token;
      } catch (JWTCreationException exception) {
         throw new RuntimeException("error al generar el token JWT", exception);
      }
   }

   // example:expira 2 horas despues desde que se reciba el token
   private Instant fechaExpiracion() {
      return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-05:00"));  // utc:-05:00 colombia, bog, lima, quito
   }

   // validar el token enviado en el subject
   public String getSubject (String tokenJWT) {
      try {
         var algoritmo = Algorithm.HMAC256(secret);
         //Algorithm algorithm = Algorithm.RSA256(rsaPublicKey, rsaPrivateKey);
         return JWT.require(algoritmo)
               // specify any specific claim validations
               .withIssuer("API Voll.med")  // estaba autho, servidor, quien desarrolla API
               // reusable verifier instance
               .build()
               .verify(tokenJWT)
               .getSubject();
         //decodedJWT = verifier.verify(token);
      } catch (JWTVerificationException exception){
         // Invalid signature/claims
         throw new RuntimeException("Token JWT invalido o expirado.");
      }
   }

}
