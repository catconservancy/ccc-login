package org.rmcc.ccc;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.util.WebUtils;

@SpringBootApplication
@Controller
public class CccApplication extends WebMvcConfigurerAdapter {

	@RequestMapping("/")
	public String home(Map<String, Object> model) {
		model.put("message", "Hello World");
		model.put("title", "Hello Home");
		model.put("date", new Date());
		return "home";
//		return "dashboard";
	}

	@RequestMapping("/foo")
	public String foo() {
		throw new RuntimeException("Expected exception in controller");
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("login");
	}

	public static void main(String[] args) throws Exception {
		new SpringApplicationBuilder(CccApplication.class).run(args);
	}

	@Configuration
	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	protected static class ApplicationSecurity extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
				.authorizeRequests()
					.antMatchers("/img/**").permitAll()
					.anyRequest().fullyAuthenticated()
					.and()
				.formLogin()
					.loginPage("/login")
					.failureUrl("/login?error").permitAll()
					.and()
				.logout().permitAll()
                	.and()
                .csrf()
                	.csrfTokenRepository(csrfTokenRepository())
                	.and()
                .addFilterAfter(csrfHeaderFilter(), CsrfFilter.class);
            
		}

		@Override
		public void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.inMemoryAuthentication().withUser("admin").password("admin")
					.roles("ADMIN", "USER").and().withUser("user").password("user")
					.roles("USER");
		}

	    private Filter csrfHeaderFilter() {
	        return new OncePerRequestFilter() {
	            @Override
	            protected void doFilterInternal(HttpServletRequest request,
	                                            HttpServletResponse response, FilterChain filterChain)
	                    throws ServletException, IOException {
	                CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class
	                        .getName());
	                if (csrf != null) {
	                    Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
	                    String token = csrf.getToken();
	                    if (cookie == null || token != null && !token.equals(cookie.getValue())) {

	                        cookie = new Cookie("XSRF-TOKEN", token);
	                        cookie.setPath("/ccc/");

	                        response.addCookie(cookie);
	                    }
	                }
	                filterChain.doFilter(request, response);
	            }
	        };
	    }

	    private CsrfTokenRepository csrfTokenRepository() {
	        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
	        repository.setHeaderName("X-XSRF-TOKEN");
	        return repository;
	    }

	}

}