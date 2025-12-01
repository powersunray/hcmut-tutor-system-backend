package hcmut.edu.vn.tutor_support_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@RestController
@EnableJpaAuditing
public class TutorSupportSystemApplication {

  public static void main(String[] args) {
    SpringApplication.run(TutorSupportSystemApplication.class, args);
  }

  @RequestMapping(value = "/")
  public String helloWorld() {
    return "Hello World!";
  }
}
