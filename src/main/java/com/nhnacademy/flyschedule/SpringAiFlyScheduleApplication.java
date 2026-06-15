package com.nhnacademy.flyschedule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SpringAiFlyScheduleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAiFlyScheduleApplication.class, args);
    }

}
