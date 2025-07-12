package med.voll.api.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import med.voll.api.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

   @Autowired
   private UsuarioRepository repository;
   @Autowired
   private TokenService tokenService;

   @Override
   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
      var tokenJWT = recuperarToken(request);
      System.out.println("tokenJWT-> "+tokenJWT);
      if(tokenJWT!=null){
         // este subject es el email del usuario
         var subject = tokenService.getSubject(tokenJWT);
         System.out.println("token subject " + subject);
         // recuperar usuario desde repository, trae usuario desde la base de datos
         var usuario = repository.findByLogin(subject);
         // averiguar si user esta autenticado o esta logeado
         var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
         SecurityContextHolder.getContext().setAuthentication(authentication);
         System.out.println("Usuario logueado...");
      }else{
         // spring se encarga de ese null
         System.out.println("token es null, pero sigue, ejemplo en un login");
      }
      filterChain.doFilter(request, response);
   }

   private String recuperarToken(HttpServletRequest request) {
      var authorizationHeader = request.getHeader("Authorization");

      if(authorizationHeader!=null) {
         return authorizationHeader.replace("Bearer ", "").trim();
      }
      //throw new RuntimeException("Token JWT no enviado en el encabezado de Authorization");
      return null;
   }
}
