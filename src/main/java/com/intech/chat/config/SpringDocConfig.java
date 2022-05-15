package com.intech.chat.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Сваггер будет доступен на  http://localhost:8080/swagger-ui.html
 * JSON при этом находится на http://localhost:8080/v3/api-docs
 */
@Configuration
@OpenAPIDefinition(info = @Info(title = "Простой чат", version = "v1"))
@SecurityScheme(type = SecuritySchemeType.HTTP, name = "jwt", paramName = "Authorization", in = SecuritySchemeIn.HEADER, scheme = "bearer")
public class SpringDocConfig {
}
