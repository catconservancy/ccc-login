package org.rmcc.ccc;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;

@SpringBootApplication
@Controller
public class CccApplication extends WebMvcConfigurerAdapter {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/register").setViewName("register");
		registry.addViewController("/").setViewName("home");
	}

	public static void main(String[] args) throws Exception {
		new SpringApplicationBuilder(CccApplication.class).run(args);
	}
	
	@RequestMapping({
		"/dashboard",
		"/importPhotos",
		"/viewPhotos",
		"/idPhotos",
		"/folderLocation",
		"/cameraMonitoringLog",
		"/studyAreas",
		"/deployments",
		"/species",
        "/detectionDetails",
        "/userProfile"
	})
	public String index() { return "home"; }

}