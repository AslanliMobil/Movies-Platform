package org.example.moviesplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync    // Video transkodlaşdırılmasının arxa planda işləməsi üçün vacibdir
@EnableCaching  // Redis və ya daxili cache mexanizminin aktiv edilməsi üçün
public class MoviesPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoviesPlatformApplication.class, args);
    }

}