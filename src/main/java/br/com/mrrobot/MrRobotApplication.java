package br.com.mrrobot;

import br.com.mrrobot.domain.Category;
import br.com.mrrobot.repository.CategoryRepository;
import br.com.mrrobot.service.TextClassificationService;
import com.aliasi.util.Files;
import java.io.File;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MrRobotApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MrRobotApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(MrRobotApplication.class, args);
    }

    @Bean
    public CommandLineRunner bootstrap(CategoryRepository repository, TextClassificationService service) {
        return (args) -> {
            repository.save(new Category("Science"));
            repository.save(new Category("Technology"));
            
            File knowledge = new File(Config.KNOWLEDGE_PATH);
            File trainFile = new File(Config.TRAIN_PATH);
            if (!knowledge.exists()) {
                knowledge.createNewFile();
            }
            if (!trainFile.exists()) {
                trainFile.createNewFile();
            }
            Files.writeBytesToFile("".getBytes(), knowledge);
            Files.writeBytesToFile("".getBytes(), trainFile);
            service.init();
        };
    }

}
