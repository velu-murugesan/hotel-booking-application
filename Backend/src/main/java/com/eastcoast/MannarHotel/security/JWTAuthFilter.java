package com.eastcoast.MannarHotel.security;
import com.eastcoast.MannarHotel.utils.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

       final String authHeader = request.getHeader("Authorization");
       final String jwtToken;
       final String userEmail;

        System.out.println("Auth Header: " + authHeader);
       if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
       }

       jwtToken = authHeader.substring(7);
        System.out.println("JWT Token: " + jwtToken);
       userEmail =  jwtUtils.extractUsername(jwtToken);
        System.out.println("Extracted Email: " + userEmail);
       if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){

           UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
           System.out.println("Current Authentication: " +
                   SecurityContextHolder.getContext().getAuthentication());

           System.out.println("Authorities from DB: " + userDetails.getAuthorities());

           boolean valid = jwtUtils.isTokenValid(jwtToken,userDetails);
           System.out.println("Is Token Valid: " + valid);
           if(valid){

               SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
               UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
               token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
               securityContext.setAuthentication(token);
               SecurityContextHolder.setContext(securityContext);
           }


       }
        filterChain.doFilter(request,response);

    }
}
