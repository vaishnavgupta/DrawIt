package org.vaishnav.drawit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DrawitApplication {

    public static void main(String[] args) {
        SpringApplication.run(DrawitApplication.class, args);
        System.out.println("Drawit Application Started");
    }

}
