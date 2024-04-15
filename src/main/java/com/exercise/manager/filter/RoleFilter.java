package com.exercise.manager.filter;

import com.exercise.manager.model.UserInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

/**
 * @author wby
 */
public class RoleFilter  extends OncePerRequestFilter {

    public static final String REQUEST_HEADER = "Role-Info";


    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader(REQUEST_HEADER);
        if (header != null) {
            String decodedHeader = new String(Base64.getDecoder().decode(header));
            UserInfo userInfo = objectMapper.readValue(decodedHeader, UserInfo.class);

            request.setAttribute("role", userInfo.getRole());
            request.setAttribute("userId", userInfo.getUserId());
        }
        filterChain.doFilter(request, response);
    }
}
