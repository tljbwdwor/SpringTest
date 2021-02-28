package iths.tlj.lab2i;

import iths.tlj.lab2i.dtos.GuitaristDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class Lab2iApplicationTests {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    //GET ALL
    @Test
    void contextLoads() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept","application.xml");
        var result = restTemplate.getForEntity("http://localhost:" + port + "/guitarists", GuitaristDto[].class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertTrue(result.hasBody());
        assertThat(result.getBody().length).isGreaterThan(0);
        assertThat(result.getBody().length).isEqualTo(3);
        System.out.println("1: FIRST: " + Arrays.stream(result.getBody()).findFirst().get().getFirstName());
    }

    //GET ONE BY ID
    @Test
    void getByIdReturnsOkAndJson() {
        HttpHeaders header = new HttpHeaders();
        header.add("Accept","application.xml");
        header.setContentType(MediaType.APPLICATION_JSON);

        var result = restTemplate.getForEntity("http://localhost:" + port + "/guitarists/1", GuitaristDto.class);
        System.out.println("YO CHECK THIS OUT: " + result.getBody().getFirstName());
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertTrue(result.hasBody());
        assertThat(result.getBody().getLastName()).isEqualTo("Hendrix");
    }

    //INVALID GET BY ID
    @Test
    void getByInvalidIdReturnsNotFound() {
        HttpHeaders header = new HttpHeaders();
        header.add("Accept","application.xml");
        header.setContentType(MediaType.APPLICATION_JSON);

        var result = restTemplate.getForEntity("http://localhost:" + port + "/guitarists/4", GuitaristDto.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    //CREATE
    @Test
    void testingPostReturns201AndAddsJson() {
        GuitaristDto guitaristDto = new GuitaristDto(4,"T","T","T");

        HttpHeaders header = new HttpHeaders();
        header.add("Accept","application.xml");
        header.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GuitaristDto> request = new HttpEntity<GuitaristDto>(guitaristDto);
        ResponseEntity<GuitaristDto> response = restTemplate
                .postForEntity("http://localhost:" + port + "/guitarists",
                guitaristDto, GuitaristDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getLastName()).isEqualTo("T");
        assertThat(response.getBody().getFirstName()).isEqualTo(guitaristDto.getFirstName());
    }

    //INVALID CREATE
    @Test
    void invalidPostReturns400() {
        GuitaristDto invalidDto = new GuitaristDto(0,null,null,null);

        HttpHeaders header = new HttpHeaders();
        header.add("Accept","application.xml");
        header.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<GuitaristDto> response = restTemplate
                .postForEntity("http://localhost:" + port + "/guitarists",
                        invalidDto, GuitaristDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    //REPLACE
    @Test
    void replaceReturns200AndJson() {
        GuitaristDto newG = new GuitaristDto(1,"new","new","new");

        HttpHeaders header = new HttpHeaders();
        header.add("Accept","application.xml");
        header.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GuitaristDto> request = new HttpEntity<GuitaristDto>(newG);
        ResponseEntity<GuitaristDto> response = this.restTemplate.exchange("http://localhost:" + port
                + "/guitarists/1", HttpMethod.PUT, request, GuitaristDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody().getFirstName()).isEqualTo(newG.getFirstName());
    }

    //INVALID REPLACE
    @Test
    void replaceInvalidIdReturnsNotFound() {
        GuitaristDto newG = new GuitaristDto(1,"new","new","new");

        HttpHeaders header = new HttpHeaders();
        header.add("Accept","application.xml");
        header.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GuitaristDto> request = new HttpEntity<GuitaristDto>(newG);
        ResponseEntity<GuitaristDto> response = this.restTemplate.exchange("http://localhost:" + port
                + "/guitarists/4", HttpMethod.PUT, request, GuitaristDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    //TO DO- fix parsing error in search methods. Fix Replace & delete test methods.
    //check out https://medium.com/swlh/https-medium-com-jet-cabral-testing-spring-boot-restful-apis-b84ea031973d

    //SEARCH BY FIRST
    @Test
    void getByFirstReturnsOkAndJson() {
        HttpHeaders header = new HttpHeaders();
        header.add("Accept","application.xml");
        header.setContentType(MediaType.APPLICATION_JSON);

        var result = restTemplate.getForEntity("http://localhost:" + port + "/guitarists/firstname/jimi",
                GuitaristDto[].class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertTrue(result.hasBody());
        assertThat(Arrays.stream(result.getBody()).findFirst().get().getLastName()).isEqualTo("Hendrix");
    }

    @Test
    void testingGetOneByFirstWithDb() {
        HttpHeaders header = new HttpHeaders();
        header.add("Accept","application.xml");
        var result = restTemplate.getForEntity("http://localhost:" + port + "/guitarists/firstname/jimi",
                GuitaristDto[].class);


        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertTrue(result.hasBody());
        assertThat(Arrays.stream(result.getBody()).findFirst().get().getLastName()).isEqualTo("Hendrix");
        assertThat(result.getBody().length).isEqualTo(1);
    }

    @Test
    void testingGetOneByLastWithDb() {
        HttpHeaders header = new HttpHeaders();
        header.add("Accept","application.xml");
        var result = restTemplate.getForEntity("http://localhost:" + port + "/guitarists/lastname/hendrix",
                GuitaristDto[].class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertTrue(result.hasBody());
        assertThat(Arrays.stream(result.getBody()).findFirst().get().getFirstName()).isEqualTo("Jimi");
        assertThat(result.getBody().length).isEqualTo(1);
    }




    /*@Test
    void validIdDeleteReturns200() {
        ResponseEntity<String> response = this.restTemplate
                .delete("http://localhost:" + port + "/guitarists/delete/1", HttpMethod.DELETE);



    }*/
}
