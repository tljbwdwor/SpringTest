package iths.tlj.lab2i;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import iths.tlj.lab2i.dtos.GuitaristDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//this is instead of @BeforeEach
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
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

        HttpEntity<GuitaristDto> request = new HttpEntity<>(newG);
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

        HttpEntity<GuitaristDto> request = new HttpEntity<>(newG);
        ResponseEntity<GuitaristDto> response = this.restTemplate.exchange("http://localhost:" + port
                + "/guitarists/4", HttpMethod.PUT, request, GuitaristDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    //UPDATE FIRST
    @Test
    void updateFirstReturns200AndJson() {
        GuitaristDto updateFirst = new GuitaristDto(1,"J","H","A");

        //The standard JDK HTTP library does not support HTTP PATCH. These next 4 lines help us to support it.
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(1000);
        requestFactory.setReadTimeout(1000);
        restTemplate.getRestTemplate().setRequestFactory(requestFactory);

        HttpHeaders header = new HttpHeaders();
        header.add("Accept","application.xml");
        header.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GuitaristDto> request = new HttpEntity<>(updateFirst);
        ResponseEntity<GuitaristDto> response = this.restTemplate.exchange("http://localhost:" + port
                + "/guitarists/firstname/1", HttpMethod.PATCH, request, GuitaristDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody().getFirstName()).isEqualTo(updateFirst.getFirstName());
        assertThat(response.getBody().getLastName()).isEqualTo("Hendrix");
        assertThat(response.getBody().getNationality()).isEqualTo("American");
    }

    //INVALID UPDATE FIRST
    @Test
    void invalidIdUpdateFirstReturnsNotFound() {
        GuitaristDto updateFirst = new GuitaristDto(4,"J","H","A");

        //The standard JDK HTTP library does not support HTTP PATCH. These next 4 lines help us to support it.
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(1000);
        requestFactory.setReadTimeout(1000);
        restTemplate.getRestTemplate().setRequestFactory(requestFactory);

        HttpHeaders header = new HttpHeaders();
        header.add("Accept","application.xml");
        header.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GuitaristDto> request = new HttpEntity<>(updateFirst);
        ResponseEntity<GuitaristDto> response = this.restTemplate.exchange("http://localhost:" + port
                + "/guitarists/firstname/4", HttpMethod.PATCH, request, GuitaristDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    //UPDATE LAST
    @Test
    void updateLastReturns200AndJson() {
        GuitaristDto updateLast = new GuitaristDto(1,"J","H","A");

        //The standard JDK HTTP library does not support HTTP PATCH. These next 4 lines help us to support it.
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(1000);
        requestFactory.setReadTimeout(1000);
        restTemplate.getRestTemplate().setRequestFactory(requestFactory);

        HttpHeaders header = new HttpHeaders();
        header.add("Accept","application.xml");
        header.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GuitaristDto> request = new HttpEntity<>(updateLast);
        ResponseEntity<GuitaristDto> response = this.restTemplate.exchange("http://localhost:" + port
                + "/guitarists/lastname/1", HttpMethod.PATCH, request, GuitaristDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody().getLastName()).isEqualTo(updateLast.getLastName());
        assertThat(response.getBody().getFirstName()).isEqualTo("Jimi");
        assertThat(response.getBody().getNationality()).isEqualTo("American");
    }

    //INVALID UPDATE LAST
    @Test
    void invalidIdUpdateLastReturnsNotFound() {
        GuitaristDto updateLast = new GuitaristDto(1,"J","H","A");

        //The standard JDK HTTP library does not support HTTP PATCH. These next 4 lines help us to support it.
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(1000);
        requestFactory.setReadTimeout(1000);
        restTemplate.getRestTemplate().setRequestFactory(requestFactory);

        HttpHeaders header = new HttpHeaders();
        header.add("Accept","application.xml");
        header.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GuitaristDto> request = new HttpEntity<>(updateLast);
        ResponseEntity<GuitaristDto> response = this.restTemplate.exchange("http://localhost:" + port
                + "/guitarists/lastname/4", HttpMethod.PATCH, request, GuitaristDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    //UPDATE NATIONALITY
    @Test
    void updateNationalityReturns200AndJson() {
        GuitaristDto updateNationality = new GuitaristDto(1,"J","H","A");

        //The standard JDK HTTP library does not support HTTP PATCH. These next 4 lines help us to support it.
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(1000);
        requestFactory.setReadTimeout(1000);
        restTemplate.getRestTemplate().setRequestFactory(requestFactory);

        HttpHeaders header = new HttpHeaders();
        header.add("Accept","application.xml");
        header.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GuitaristDto> request = new HttpEntity<>(updateNationality);
        ResponseEntity<GuitaristDto> response = this.restTemplate.exchange("http://localhost:" + port
                + "/guitarists/nationality/1", HttpMethod.PATCH, request, GuitaristDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody().getNationality()).isEqualTo(updateNationality.getNationality());
        assertThat(response.getBody().getFirstName()).isEqualTo("Jimi");
        assertThat(response.getBody().getLastName()).isEqualTo("Hendrix");
    }

    //INVALID UPDATE NATIONALITY
    @Test
    void invalidIdUpdateNationalityReturnsNotFound() {
        GuitaristDto updateNationality = new GuitaristDto(1,"J","H","A");

        //The standard JDK HTTP library does not support HTTP PATCH. These next 4 lines help us to support it.
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(1000);
        requestFactory.setReadTimeout(1000);
        restTemplate.getRestTemplate().setRequestFactory(requestFactory);

        HttpHeaders header = new HttpHeaders();
        header.add("Accept","application.xml");
        header.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GuitaristDto> request = new HttpEntity<>(updateNationality);
        ResponseEntity<GuitaristDto> response = this.restTemplate.exchange("http://localhost:" + port
                + "/guitarists/nationality/4", HttpMethod.PATCH, request, GuitaristDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    //DELETE
    @Test
    public void deleteByIdReturnsNoContent204() {
        HttpHeaders header = new HttpHeaders();
        header.add("Accept","application.xml");
        header.setContentType(MediaType.APPLICATION_JSON);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);


        var result = this.restTemplate
                .exchange("http://localhost:" + port + "/guitarists/delete/1", HttpMethod.DELETE, null,
                        Void.class);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    //SEARCH
    @Test
    void searchNationalityReturnsOkAndJson() {
        HttpHeaders header = new HttpHeaders();
        header.add("Accept","application/json");
        header.setContentType(MediaType.APPLICATION_JSON);

        var result = restTemplate.getForEntity("http://localhost:" + port + "/guitarists/search/3?nationality=a",
                GuitaristDto[].class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertTrue(result.hasBody());
        assertThat(result.getBody().length).isEqualTo(2);
    }

    @Test
    void searchFirstNameReturnsOkAndJson() {
        HttpHeaders header = new HttpHeaders();
        header.add("Accept","application/json");
        header.setContentType(MediaType.APPLICATION_JSON);

        var result = restTemplate.getForEntity("http://localhost:" + port + "/guitarists/search/1?firstname=a",
                GuitaristDto[].class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertTrue(Arrays.stream(result.getBody()).findFirst().get().getFirstName().contains("Shawn"));
        assertThat(result.getBody().length).isEqualTo(1);
    }

    @Test
    void searchLastNameReturnsOkAndJson() {
        HttpHeaders header = new HttpHeaders();
        header.add("Accept","application/json");
        header.setContentType(MediaType.APPLICATION_JSON);

        var result = restTemplate.getForEntity("http://localhost:" + port + "/guitarists/search/2?lastname=a",
                GuitaristDto[].class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Arrays.stream(result.getBody()).findFirst().get().getLastName()).isEqualTo("Lane");
        assertThat(result.getBody().length).isEqualTo(2);
    }

    //INVALID SEARCH
    @Test
    void searchNationalityInvalidReturnsOkAndJson() {
        HttpHeaders header = new HttpHeaders();
        header.add("Accept","application/json");
        header.setContentType(MediaType.APPLICATION_JSON);

        var result = restTemplate.getForEntity("http://localhost:" + port + "/guitarists/search/3?nationality=z",
                GuitaristDto[].class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().length).isEqualTo(0);
    }

    //GET ALL by element
    @Test
    void getByFirstReturnsJson200() {


        HttpHeaders header = new HttpHeaders();
        header.add("Accept","application.xml");
        header.setContentType(MediaType.APPLICATION_JSON);

        var result = restTemplate.exchange("http://localhost:" + port + "/guitarists/firstname/Jimi",HttpMethod.GET,
                null, new ParameterizedTypeReference<List<GuitaristDto>>(){});

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertTrue(result.hasBody());
        assertThat(result.getBody().get(0).getLastName()).isEqualTo("Hendrix");
    }

    @Test
    void testingGetOneByFirstReturnsJson200() {
        HttpHeaders header = new HttpHeaders();
        header.add("Accept","application.xml");
        var result = restTemplate.getForEntity("http://localhost:" + port + "/guitarists/firstname/Jimi",
                GuitaristDto[].class);


        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertTrue(result.hasBody());
        assertThat(Arrays.stream(result.getBody()).findFirst().get().getLastName()).isEqualTo("Hendrix");
        assertThat(result.getBody().length).isEqualTo(1);
    }

    @Test
    void testingGetByLastReturnsJson200() {
        HttpHeaders header = new HttpHeaders();
        header.add("Accept","application.xml");
        var result = restTemplate.getForEntity("http://localhost:" + port + "/guitarists/lastname/Hendrix",
                GuitaristDto[].class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertTrue(result.hasBody());
        assertThat(Arrays.stream(result.getBody()).findFirst().get().getFirstName()).isEqualTo("Jimi");
        assertThat(result.getBody().length).isEqualTo(1);
    }
}

//check out https://medium.com/swlh/https-medium-com-jet-cabral-testing-spring-boot-restful-apis-b84ea031973d
