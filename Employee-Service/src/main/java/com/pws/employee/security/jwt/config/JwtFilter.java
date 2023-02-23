package com.pws.employee.security.jwt.config;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pws.employee.utility.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
@Component
public class JwtFilter extends OncePerRequestFilter {


    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = httpServletRequest.getHeader("Authorization");

        String token = null;
        String userName = null;

        try {
        	
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            userName = jwtUtil.extractUsername(token);
        }

        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(userName);

            if (jwtUtil.validateToken(token, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
            
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
    
    catch (SignatureException ex){
    	httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        ((HttpServletResponse) httpServletResponse).sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT signature");

    }
    catch (MalformedJwtException ex){
    	httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        ((HttpServletResponse) httpServletResponse).sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT token");

    }
    catch (ExpiredJwtException ex){
    	httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        ((HttpServletResponse) httpServletResponse).sendError(HttpServletResponse.SC_BAD_REQUEST, "Expired JWT token");

    }
    catch (UnsupportedJwtException ex){
    	httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        ((HttpServletResponse) httpServletResponse).sendError(HttpServletResponse.SC_BAD_REQUEST, "Unsupported JWT token");

    }
    catch (IllegalArgumentException ex){
    	httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        ((HttpServletResponse) httpServletResponse).sendError(HttpServletResponse.SC_BAD_REQUEST, "JWT claims string is empty");
        
    }
}
}