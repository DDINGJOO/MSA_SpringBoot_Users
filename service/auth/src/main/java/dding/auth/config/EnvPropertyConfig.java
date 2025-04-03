package dding.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("file:/app/.env.properties")
public class EnvPropertyConfig {
}
