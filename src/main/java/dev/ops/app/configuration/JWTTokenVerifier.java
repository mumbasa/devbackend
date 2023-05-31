package dev.ops.app.configuration;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.var;
@Component
public class JWTTokenVerifier extends OncePerRequestFilter {
	//@Value("{${jwt.secret}}")
	String jwtToken ="oeRaYY7Wo24sDqKSX3IM9ASGmdGPmkTd9jo1QTy4b7P9Ze5_9hKolVX8xNrQDcNRfVEdTZNOuOyqEGhXEbdJI-ZQ19k_o9MI0y3eZN2lp9jow55FfXMiINEdt1XR85VipRLSOkT6kSpzs2x-jbLDiz9iFVzkd81YKxMgPA7VfZeQUm4n-mOmnWMaVX30zGFU4L3oPBctYKkl4dYfqYWqRNfrgPJVi5DGFjywgxx0ASEiJHtV72paI3fDR2XwlSkyhhmY-ICjCRmsJN4fX1pdoL8a18-aQrvyu4j0Os6dVPYIoPvvY0SAZtWYKHfM15g7A3HD4cVREf9cUsprCRK93w";;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String headerToken = request.getHeader("Authorization");
		Optional<String> headerData = Optional.ofNullable(headerToken);
		if(!headerData.isPresent() || !headerData.get().startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		try {
			String token = headerData.get().replace("Bearer ", "");
			Jws<Claims> claims =			
			Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(jwtToken.getBytes())).build().parseClaimsJws(token);
			String username = claims.getBody().getSubject();
			@SuppressWarnings("unchecked")
			var authorities = (List<Map<String, String>>) claims.getBody().get("authorities");
			/*
			 * Set<GrantedAuthority> permisions = authorities.stream().map(e ->new
			 * SimpleGrantedAuthority(e.get("authority"))). collect(Collectors.toSet());
			 */
			Authentication authentication = new UsernamePasswordAuthenticationToken(username,null, null);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}catch (JwtException e) {
			// TODO: handle exception
			throw new JwtException("Expired");
		}
		// TODO Auto-generated method stub
		filterChain.doFilter(request, response);

	}

}
