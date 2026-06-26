package hr.algebra.fishingstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class FishingStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(FishingStoreApplication.class, args);
    }

}
