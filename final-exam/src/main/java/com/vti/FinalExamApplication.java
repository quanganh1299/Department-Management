package com.vti;

import com.vti.dto.DepartmentDto;
import com.vti.entity.Account;
import com.vti.form.AccountCreateForm;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class FinalExamApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinalExamApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer(){
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("http://127.0.0.1:5500")
						.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
			}
		};
	}

	@Bean
	public ModelMapper modelMapper(){
		ModelMapper mapper = new ModelMapper();

		mapper.typeMap(AccountCreateForm.class, Account.class)
				.addMappings(mapping -> mapping.skip(Account::setId));
		// khi map từ AccountCreateForm sang Account thì skip phần setId cho Account
		// tránh bị nhầm giữa departmentId với id ở trong Account
		return mapper;
	}

}
