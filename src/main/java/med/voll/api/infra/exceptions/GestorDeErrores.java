package med.voll.api.infra.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice  // tiene que ver con gestion de errores
public class GestorDeErrores {
   @ExceptionHandler(EntityNotFoundException.class) // clase
   public ResponseEntity gestionarError404(){
      return ResponseEntity.notFound().build();
   }
   // de debemos pasar la exception como parametro para que se envie un body con le explicacion del error
   @ExceptionHandler(MethodArgumentNotValidException.class) // clase
   public ResponseEntity gestionarError400(MethodArgumentNotValidException ex){
      var errors= ex.getFieldErrors();
      return ResponseEntity.badRequest().body(errors.stream().map(DatosErrorValidacion::new).toList());
   }
   public record DatosErrorValidacion(String field, String msg){
      public DatosErrorValidacion(FieldError error) {
         this(error.getField(), error.getDefaultMessage());
      }
   }
   // otra gestion de errores dados en la teoria de alura
   @ExceptionHandler(HttpMessageNotReadableException.class)
   public ResponseEntity gestionarError400(HttpMessageNotReadableException ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
   }

   @ExceptionHandler(BadCredentialsException.class)
   public ResponseEntity gestionarErrorBadCredentials() {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
   }

   @ExceptionHandler(AuthenticationException.class)
   public ResponseEntity gestionarErrorAuthentication() {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falla en la autenticación");
   }

   @ExceptionHandler(AccessDeniedException.class)
   public ResponseEntity gestionarErrorAccesoDenegado() {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado");
   }

   @ExceptionHandler(Exception.class)
   public ResponseEntity gestionarError500(Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " +ex.getLocalizedMessage());
   }
}
