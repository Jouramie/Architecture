package ca.ulaval.glo4003.ws.http;

import ca.ulaval.glo4003.ws.infrastructure.injection.FilterRegistration;
import java.io.IOException;
import javax.servlet.*;

@FilterRegistration
public class AuthenticationFilter implements javax.servlet.Filter {


  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void doFilter(ServletRequest servletRequest,
                       ServletResponse servletResponse,
                       FilterChain filterChain) throws IOException, ServletException {
    System.out.println("hello from filter");
    filterChain.doFilter(servletRequest, servletResponse);
  }

  @Override
  public void destroy() {

  }
}
