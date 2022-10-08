package com.cscb025.logistic.company.config;

import com.cscb025.logistic.company.entity.User;
import com.cscb025.logistic.company.exception.EntityNotFoundException;
import com.cscb025.logistic.company.exception.ErrorMessage;
import com.cscb025.logistic.company.service.JwtUserDetailsService;
import com.cscb025.logistic.company.service.UserService;
import com.cscb025.logistic.company.util.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    public JwtRequestFilter(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        if (isPathPermitted(request)) {
            chain.doFilter(request, response);
        } else {
            String[] cookies = request.getHeader("Cookie").split("; ");
            String requestTokenHeader = null;

            for (String cookie : cookies
            ) {
                if (cookie.trim().startsWith("Bearer")) {
                    requestTokenHeader = cookie.trim();
                }
                break;
            }

            if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
                tryAcquiringAccessToken(request, response, chain, requestTokenHeader);
            } else {
                forbiddenResponse(response, "Token missing");
            }
        }
    }

    private void tryAcquiringAccessToken(HttpServletRequest request, HttpServletResponse response,
                                         FilterChain chain, String requestTokenHeader) throws IOException, ServletException {
        try {
            String jwtToken = requestTokenHeader.substring(7);

            Claims claims = jwtTokenUtil.getAllClaimsFromToken(jwtToken);
            String email = claims.getSubject();

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = userService.findByEmail(email);
                request.setAttribute("userId", user.getUid());

                UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }

            chain.doFilter(request, response);

        } catch (EntityNotFoundException e) {
            log.error("User was deleted!");
            response.setStatus(HttpStatus.GONE.value());
            response.setContentType("application/json");
        } catch (IllegalArgumentException e) {
            forbiddenResponse(response, "Unable to get token");
        } catch (ExpiredJwtException e) {
            forbiddenResponse(response, "Token expired");
        }
    }

    void forbiddenResponse(HttpServletResponse response, String message) throws IOException {
        log.error(message);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(new ErrorMessage(message)));
    }


    private boolean isPathPermitted(HttpServletRequest request) {
        return isUserPathPermitted(request);
    }

    //TODO remove or change
    private boolean isImagePathPermitted(HttpServletRequest request) {
        return (request.getMethod().equalsIgnoreCase("get")
                && request.getServletPath().startsWith("/image"));
    }

    private boolean isUserPathPermitted(HttpServletRequest request) {
        return ((request.getServletPath().startsWith("/user") || request.getServletPath().startsWith("/chooseRole"))
                && !(request.getMethod().equalsIgnoreCase("put")))
                || (request.getServletPath().matches("/signin"))
                || (request.getServletPath().endsWith(".js") || request.getServletPath().endsWith(".css"));
    }
}