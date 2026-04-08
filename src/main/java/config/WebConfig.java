package config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir}")
    private String uploadDir;

    /*
     * @Override
     * public void addResourceHandlers(ResourceHandlerRegistry registry) {
     * registry.addResourceHandler("/uploads/**")
     * .addResourceLocations("file:" + System.getProperty("user.dir") + "/" +
     * uploadDir + "/");
     * }
     */

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String ruta = "file:" + System.getProperty("user.dir") + "/uploads/";
        System.out.println(">>> LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL RESOURCE HANDLER RUTA: " + ruta);
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(ruta);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}