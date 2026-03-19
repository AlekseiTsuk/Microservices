package Application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


@RestClientTest(ApiService.class)
@Import(RestClientConfig.class)
public class ApiServiceTest {

    @Autowired
    private ApiService apiService;

    @Autowired
    private MockRestServiceServer server;

    private final String URL = "http://94.198.50.185:7081/api/users";

    @Test
    public void testFullTaskWorkFlow() {
        server.reset();

        server.expect(requestTo(URL))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.SET_COOKIE,
                                "JSESSIONID=test-session; Path=/"));

        server.expect(requestTo(URL))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Cookie", "JSESSIONID=test-session"))
                .andExpect(jsonPath("$.name").value("James"))
                .andRespond(withSuccess("part1", MediaType.TEXT_PLAIN));

        server.expect(requestTo(URL))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(jsonPath("$.name").value("Thomas"))
                .andRespond(withSuccess("part2", MediaType.TEXT_PLAIN));


        server.expect(requestTo(URL + "/3"))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withSuccess("part3", MediaType.TEXT_PLAIN));


        apiService.getAllUsers();
        String result = apiService.task();

        assertEquals("part1part2part3", result);
        server.verify();
    }
}
