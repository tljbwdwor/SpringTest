package iths.tlj.lab2i;

import iths.tlj.lab2i.dtos.GuitaristDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class Lab2iApplicationTests {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
        //This test is checking the whole app loads properly and therefore will need to be connected to the real DB,
        // not the test one.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept","application.xml");
        var result = restTemplate.getForEntity("http://localhost:" + port + "/guitarists", GuitaristDto[].class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertTrue(result.hasBody());
        assertThat(result.getBody().length).isGreaterThan(0);
    }

    @Test
    void testingGetAllWithDb() {
        HttpHeaders header = new HttpHeaders();
        header.add("Accept","application.xml");
        var result = restTemplate.getForEntity("http://localhost:" + port + "/guitarist/1", GuitaristDto.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertTrue(result.hasBody());
        assertThat(result.getBody().getLastName()).isEqualTo("Hendrix");
    }

    @Test
    void testingPostWorks() {
        HttpHeaders header = new HttpHeaders();
        header.add("Accept","application.xml");
        GuitaristDto guitaristDto = new GuitaristDto(0,"T","T","T");
        var result = restTemplate.postForEntity("http://localhost:" + port + "/guitarist",
                guitaristDto, GuitaristDto.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody().getLastName()).isEqualTo("T");
    }

    @Test
    void testingPutWorks() {
        HttpHeaders header = new HttpHeaders();
    }
}
