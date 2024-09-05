package ro.mpp2024.start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@ComponentScan({"ro.mpp2024.service","ro.mpp2024.repository"})
@SpringBootApplication
public class StartRestServer {
    public static void main(String[] args) {

        SpringApplication.run(StartRestServer.class, args);
    }

    @Bean(name="props")
    public Properties getBdProperties(){
        Properties props = new Properties();
        try {
            System.out.println("Searching bd.config in directory "+((new File(".")).getAbsolutePath()));
            props.load(new FileReader("C:\\Users\\Andrea\\IdeaProjects\\mpp-proiect-java-andrea17tc\\Rest\\bd.config"));
        } catch (IOException e) {
            System.err.println("Configuration file bd.cong not found" + e);

        }
        return props;
    }

    //TODO: clientjava, JsonObjectMapper, HTTPCLient
}
