package com.example.spotify.config.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService; //inyecta el bean que devuelve el UserDetails de la bd.
  //  private final UserRepository userRepository;

    @Override
    protected void doFilterInternal( //vamos a chequear el token..
            @NotNull HttpServletRequest request, //obtener data desde el request(El header)-- el token
            @NotNull HttpServletResponse response, //puedeo agregar un header a la respuesta
            @NotNull  FilterChain filterChain  //Chain(Cadena), cadena de filtros que se deben ejecutar.
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;
        if(authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response); //to next filter.
            return; //terminar ejecución no pase a lo siguiente.
        }

        jwtToken = authHeader.substring(7); //porque tiene 7 caracteres--> "Bearer "
        //necesito un servicio que me permita sacar info del token.
        userEmail = jwtService.extractUsername(jwtToken);
        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //si no esta autenticado, tengo que validar la identidad del usuario con la bbdd
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail); //la imp de la interfaz impl este método
            if(jwtService.isTokenValid(jwtToken, userDetails)) {
                //si es valido, actualizo securityContext, le enviamos dicho objecto Authentication
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, //credentials
                        userDetails.getAuthorities()
                );
                authToken.setDetails( //darle más detalles
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken); //seteo el contexto
            }
        } //ver si el usuario no esta autenticado, para no repetir proceso.
        filterChain.doFilter(request, response); //para que haga el filtro siguiente.
    }

    //NECESITAMOS DECIRLE A SPRING QUE USE ESTA CONFIGURACION(FILTER, USERDETAILS, JWT COMO PROVEEDOR(JWT SERVICE))
}
