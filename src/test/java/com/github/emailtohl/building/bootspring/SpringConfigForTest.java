package com.github.emailtohl.building.bootspring;

import static com.github.emailtohl.building.config.RootContextConfiguration.PROFILE_DEVELPMENT;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;

import com.github.emailtohl.building.config.RootContextConfiguration;
import com.github.emailtohl.building.site.service.UserService;
import com.github.emailtohl.building.stub.SecurityContextManager;
import com.github.emailtohl.building.stub.ServiceStub;

@Profile(PROFILE_DEVELPMENT)
@Configuration
@Import({ RootContextConfiguration.class })
public class SpringConfigForTest {
	@Inject AuthenticationManager authenticationManager;
	
	@Bean
	public SecurityContextManager securityContextManager() throws Exception {
		return new SecurityContextManager(authenticationManager);
	}
	
	@Bean
	public ServiceStub serviceStub() {
		return new ServiceStub();
	}
	
	@Bean
	public UserService userServiceMock() {
		return serviceStub().getUserService();
	}
}