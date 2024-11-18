package com.daym.oauth2.config;

import com.daym.oauth2.response.ApiResponse;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.parsers.ReturnTypeParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.lang.reflect.Type;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: Date: 2024/11/18
 * Time: 15:44
 * To change this template use File | Settings | File Templates.
 * Description:
 */
@Configuration

@EnableWebMvc
public class Knife4jConfig {
    @Bean
    public GroupedOpenApi adminApi() { // 创建了一个api接口的分组
        return GroupedOpenApi.builder()
                .group("admin-api") // 分组名称
                .pathsToMatch("/**") // 接口请求路径规则
                .build();
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info() // 基本信息配置
                        .title("Knife4j整合Swagger3 Api接口文档") // 标题
                        .description("Knife4j后端接口服务...") // 描述Api接口文档的基本信息
                        .version("v1.0.0") // 版本
                        // 设置OpenAPI文档的联系信息，包括联系人姓名为"patrick"，邮箱为"patrick@gmail.com"。
                        .contact(new Contact().name("ink100").email("ink00@gmail.com"))
                        // 设置OpenAPI文档的许可证信息，包括许可证名称为"Apache 2.0"，许可证URL为"http://springdoc.org"。
                        .license(new License().name("Apache 2.0").url("http://springdoc.org"))
                );
    }

    /**
     * 文档解析
     * @return
     */
    @Bean
    public ReturnTypeParser returnTypeParser() {
        return new ReturnTypeParser() {
            @Override
            public Type getReturnType(MethodParameter methodParameter) {
                Type returnType = ReturnTypeParser.super.getReturnType(methodParameter);
                Class<?> parameterType = methodParameter.getParameterType();
                // 资源文件或者已经被包装了, 直接返回
                if (parameterType.isAssignableFrom(ApiResponse.class) || parameterType.isAssignableFrom(Resource.class)) {
                    return returnType;
                }
                // 分页特殊处理, 转为PagingVO类
               /* if (parameterType.isAssignableFrom(Page.class) && returnType instanceof ParameterizedType) {
                    Optional<Type> t = TypeUtils.getTypeArguments((ParameterizedType) returnType)
                            .values().stream().findFirst();
                    Type type = t.orElse(Object.class);
                    return TypeUtils.parameterize(Res.class, TypeUtils.parameterize(PagingVO.class, type));
                }*/
                // void转为Res<Object>
                if (parameterType.isAssignableFrom(void.class)) {
                    return TypeUtils.parameterize(Resource.class, Object.class);
                }
                // 包装Res
                return TypeUtils.parameterize(ApiResponse.class, returnType);
            }
        };
    }
}