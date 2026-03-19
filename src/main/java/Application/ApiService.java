package Application;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService {

    private final RestTemplate restTemplate;

    public ApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private final String URL = "http://94.198.50.185:7081/api/users";
    private String sessionId;

    public void getAllUsers() {
        ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.GET,
                null, String.class);
         String cookie = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        this.sessionId = (cookie != null) ? cookie.split(";", 2) [0] : null;
    }

    public String task() {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set(HttpHeaders.SET_COOKIE, sessionId);
        if(sessionId != null){
            httpHeaders.add("Cookie", sessionId);
        }

        StringBuilder sb = new StringBuilder();

        User user = new User(3L, "James", "Brown", (byte) 37);

        HttpEntity<User> entitySave = new HttpEntity<>(user, httpHeaders);
        sb.append(restTemplate.exchange(URL, HttpMethod.POST, entitySave, String.class)
                .getBody());

        user.setName("Thomas");
        user.setLastName("Shelby");
        HttpEntity<User> entityUpdate = new HttpEntity<>(user, httpHeaders);
        sb.append(restTemplate.exchange(URL, HttpMethod.PUT, entityUpdate, String.class)
                .getBody());

        HttpEntity<String> entityDelete = new HttpEntity<>(httpHeaders);
        sb.append(restTemplate.exchange(URL + "/3", HttpMethod.DELETE, entityDelete, String.class)
                .getBody());

        return sb.toString();
    }
}
