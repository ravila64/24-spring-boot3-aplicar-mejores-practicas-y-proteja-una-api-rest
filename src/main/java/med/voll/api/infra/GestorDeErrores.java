package med.voll.api.infra;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
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
}
