package com.kb.fm.config;
//package com.kb.fm.web.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//@Configuration
//@EnableSwagger2
//public class SwaggerConfig {
//	@Bean
//	public Docket api() {
//		return new Docket(DocumentationType.SWAGGER_2).select()
////				net.guides.springboot2.springboot2swagger2.controller
//				.apis(RequestHandlerSelectors.basePackage("com.kb.us"))
//				.paths(PathSelectors.regex("/.*")).build().apiInfo(apiEndPointsInfo());
//	}
//
//	private ApiInfo apiEndPointsInfo() {
//		return new ApiInfoBuilder().title("Finance Manaer application").description("Finance Management REST API")
//				.contact(new Contact("Balaji Krishnan", "www.google.com", "balajikrishnan14@gmail.com"))
//				.license("Apache 2.0").licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html").version("1.0.0")
//				.build();
//	}
//}
