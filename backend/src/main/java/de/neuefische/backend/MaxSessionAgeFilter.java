package de.neuefische.backend;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MaxSessionAgeFilter extends OncePerRequestFilter {

    private static final Duration MAX_AGE = Duration.ofMinutes(10);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null &&
                System.currentTimeMillis() - session.getCreationTime() > MAX_AGE.toMillis()
        ) {
            session.invalidate();
        }
        filterChain.doFilter(request, response);
    }
}
