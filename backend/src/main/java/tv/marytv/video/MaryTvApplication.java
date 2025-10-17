package tv.marytv.video;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class MaryTvApplication {
    public static void main(String[] args) {
        SpringApplication.run(MaryTvApplication.class, args);
    }
}