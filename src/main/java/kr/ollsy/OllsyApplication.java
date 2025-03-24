package kr.ollsy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class OllsyApplication {

    public static void main(String[] args) {
        SpringApplication.run(OllsyApplication.class, args);
    }
}
