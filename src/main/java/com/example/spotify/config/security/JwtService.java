package com.example.spotify.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secretKey}")
    private String secret_key;

    public String extractUsername(String jwtToken) {

        return extractClaim(jwtToken, Claims::getSubject); //el subject sera el Username/Email de mi usuario
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignIngKey()) //la necesitamos para decodificar el token.
                .build() //ya con el objeto del token, obtenemos los claims con el parse
                .parseClaimsJwt(token)
                .getBody();  //del claim que es la info en json del token, obtenemos el body(payload)

    }

    private Key getSignIngKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret_key);
        return Keys.hmacShaKeyFor(keyBytes);

    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails); //extra claims vacio.
    }

    private String generateToken( //debe ser priado el método.
            Map<String, Object> extractClaims, //claims extras. puedo solo hacerlo con valores del UserDetails.
            UserDetails userDetails
    ) {
        return Jwts
                .builder()
                .setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24 ))
                .signWith(getSignIngKey(), SignatureAlgorithm.HS256)
                .compact(); //genera y retorna el token.

    }

    //es suficiente?
    public boolean isTokenValid(String token, UserDetails userDetails) {
        //necesitamos el userDetail para ver si la info del token corresponde a la del usuario
        final String username = extractUsername(token);  //username==email
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    } //cuando llamo a extract se valida la key?? no. Mejorar la verificación con la firma.

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date()); //fecha antes que ahora
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
