package com.yy.system.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.MultipartConfigElement;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class InterceptorConfiguration {

	@Value("${spring.servlet.multipart.max-request-size}")
	private String requestSizeMax;

	@Value("${spring.servlet.multipart.max-file-size}")
	private String fileSizeMax;

	/**
	 * 实例化WebMvcConfigurer接口
	 *
	 * @return
	 */
	@Bean
	public WebMvcConfigurer webMvcConfigurer() {
		return new WebMvcConfigurer() {
			/**
			 * 添加拦截器
			 * 
			 * @param registry
			 */
			@Override
			public void addInterceptors(InterceptorRegistry registry) {
				// 注册默认的拦截器
			}
			// 注册默认的拦截器
			@Override
			public void addResourceHandlers(ResourceHandlerRegistry registry) {
				/*registry.addResourceHandler("/index.html").addResourceLocations(
						"classpath:/static/index.html");*/
				registry.addResourceHandler("/favicon.ico").addResourceLocations(
						"classpath:/static/favicon.ico");
				registry.addResourceHandler("/static/css/static/img/**").addResourceLocations(
						"classpath:/static/img/");
				registry.addResourceHandler("/static/css/static/fonts/**").addResourceLocations(
						"classpath:/static/fonts/");
				registry.addResourceHandler("/static/**").addResourceLocations(
						"classpath:/static/");
				registry.addResourceHandler("swagger-ui.html").addResourceLocations(
						"classpath:/META-INF/resources/");
				registry.addResourceHandler("/webjars/**").addResourceLocations(
						"classpath:/META-INF/resources/webjars/");
			}

			/**
			 * Long 转换String
			 * 
			 * @param converters
			 */
			@Override
			public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
				/* 排除掉原来的MappingJackson2HttpMessageConverter */
				List<MappingJackson2HttpMessageConverter> originalConverters = new ArrayList<>();
				for (HttpMessageConverter<?> converter : converters) {
					if (converter instanceof MappingJackson2HttpMessageConverter) {
						originalConverters.add((MappingJackson2HttpMessageConverter) converter);
					}
				}

				ObjectMapper objectMapper = new ObjectMapper();
				SimpleModule simpleModule = new SimpleModule();
				simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
				simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
				simpleModule.addSerializer(long.class, ToStringSerializer.instance);
				objectMapper.registerModule(simpleModule);
				if (!CollectionUtils.isEmpty(originalConverters)) {
					originalConverters.forEach((converter) -> converter.setObjectMapper(objectMapper));
				} else {
					MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
					jackson2HttpMessageConverter.setObjectMapper(objectMapper);
					converters.add(jackson2HttpMessageConverter);
				}
			}

			/**
			 * 解决跨域问题
			 * 
			 * @param registry
			 */
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*").allowCredentials(true)
						.allowedMethods("GET", "POST", "DELETE", "PUT").maxAge(3600);
			}
		};
	}
	//@Bean(name = "multipartResolver")
	/*public MultipartResolver multipartResolver(){
		//CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		StandardServletMultipartResolver resolver = new StandardServletMultipartResolver();
		//resolver.setDefaultEncoding("UTF-8");
		resolver.setResolveLazily(true);//resolveLazily属性启用是为了推迟文件解析，以在在UploadAction中捕获文件大小异常
		//resolver.setMaxInMemorySize(40960);
		//resolver.setMaxUploadSize(100*1024*1024);//上传文件大小 100M 100*1024*1024
		return resolver;
	}*/
	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();

		factory.setMaxFileSize(StringUtils.isBlank(fileSizeMax)?"100MB":fileSizeMax);
		factory.setMaxRequestSize(StringUtils.isBlank(requestSizeMax)?"100MB":requestSizeMax);
		String location = System.getProperty("user.dir") + "/data/tmp";
		File tmpFile = new File(location);
		if (!tmpFile.exists()) {
			tmpFile.mkdirs();
		}
		factory.setLocation(location);
		return factory.createMultipartConfig();
	}
}
