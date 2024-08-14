package com.blog.app.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        //get token
        String requestToken=request.getHeader("Authorization");

        //print token on console
        System.out.println(requestToken);

        String username=null;
        String token=null;

        if(request!=null && requestToken.startsWith("Bearer")){
            token=requestToken.substring(7);
            try {
                username = jwtTokenHelper.getUsernameFromToken(token);
            }catch(IllegalArgumentException ex){
                System.out.println("Unable to fetch Jwt Token");
            }catch (ExpiredJwtException ex){
                System.out.println("Jwt Token has expired.");
            }catch (MalformedJwtException ex){
                System.out.println("Invalid Jwt Token");
            }
        }
        else
            System.out.println("Jwt token does not begin with Bearer.");

        //validating the token after extracting it
        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){

            UserDetails userDetails =userDetailsService.loadUserByUsername(username);

            if(jwtTokenHelper.validateToken(token,userDetails)){

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
                        new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //now authenticate
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
            else
                System.out.println("Invalid Jwt Token");
        }
        else
            System.out.println("Either username is null or context is null.");

        filterChain.doFilter(request,response);
    }
}
