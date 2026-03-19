package Application;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        ApiService apiService = new ApiService(new RestTemplate());

        apiService.getAllUsers();

        String finalCode = apiService.task();

        System.out.println("Final code: " + finalCode);
        System.out.println("Code length: " + finalCode.length());
    }
}
