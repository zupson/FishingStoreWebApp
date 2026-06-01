package hr.algebra.fishingstore.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "paypal")
@Data
public class PayPalConfig {
    private String clientId;
    private String clientSecret;
    private String baseUrl;
}