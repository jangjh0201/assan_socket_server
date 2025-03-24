package org.asan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@EnableMongoRepositories
@SpringBootApplication
public class AsanSocketServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsanSocketServerApplication.class, args);
    }

}
