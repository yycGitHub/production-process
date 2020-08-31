package com.surekam.modules.api.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableWebMvc
@ComponentScan(basePackages = "com.surekam.modules")
public class SwaggerConfig {
	@Bean
	public Docket api() {
		ParameterBuilder ticketPar = new ParameterBuilder();
		List<Parameter> pars = new ArrayList<Parameter>();
		ticketPar.name("X-Token").description("user ticket")
				.modelRef(new ModelRef("string")).parameterType("header")
				.required(false).build(); // header中的ticket参数非必填，传空也可以
		pars.add(ticketPar.build());

		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("溯源API接口")
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.surekam.modules.api.web"))
				.paths(PathSelectors.any()).build()
				.globalOperationParameters(pars);
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("API").description("农科院信息所溯源管理系统")
				.version("1.0").build();
	}
}