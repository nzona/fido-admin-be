package it.be.fido.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.Locale;

@SpringBootApplication
@ComponentScan("it.be.fido.admin")
public class FidoAdminApplication {

    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        SpringApplication.run(FidoAdminApplication.class, args);
    }

}
