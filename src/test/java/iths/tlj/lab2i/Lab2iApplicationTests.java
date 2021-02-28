package iths.tlj.lab2i;

import iths.tlj.lab2i.dtos.GuitaristDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
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
    void testingGetOneByIdWithDb() {
        HttpHeaders header = new HttpHeaders();
        header.add("Accept","application.xml");
        var result = restTemplate.getForEntity("http://localhost:" + port + "/guitarists/1", GuitaristDto.class);
        System.out.println("YO CHECK THIS OUT: " + result.getBody().getFirstName());
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertTrue(result.hasBody());
        assertThat(result.getBody().getLastName()).isEqualTo("Hendrix");
    }


    //TO DO- Get these methods to return correct Json format. Fix Update Replace & delete test methods.
    //check out https://medium.com/swlh/https-medium-com-jet-cabral-testing-spring-boot-restful-apis-b84ea031973d

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

    @Test
    void testingPostWorks() {
        HttpHeaders header = new HttpHeaders();
        header.add("Accept","application.xml");
        GuitaristDto guitaristDto = new GuitaristDto(4,"T","T","T");
        var result = restTemplate.postForEntity("http://localhost:" + port + "/guitarists",
                guitaristDto, GuitaristDto.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody().getLastName()).isEqualTo("T");
    }

    /*@Test
    void testingPutWorks() {
        HttpHeaders header = new HttpHeaders();
        header.add("Accept","application.xml");
        GuitaristDto oldG = new GuitaristDto(0,"Z","Z","Z");
        GuitaristDto newG = new GuitaristDto(oldG.getId(), "X","X","X");
        restTemplate.postForEntity("http:localhost:" + port + "/guitarist", oldG, GuitaristDto.class);
        restTemplate.put("http://localhost" + port + "/guitarists" + oldG.getId(), newG, GuitaristDto.class);
        assertThat(newG.getId()).isEqualTo(oldG.getId());

    }*/

    /*@Test
    void testingDeleteWorks() {
        HttpHeaders header = new HttpHeaders();
        header.add("Accept","application.xml");
        GuitaristDto deleteMe = new GuitaristDto(5000,"D","D","D");
        var result = restTemplate.postForEntity("http://localhost:" + port + "/guitarist", deleteMe, GuitaristDto.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    }*/

    @Test
    void addTestData() {

        TestRestTemplate restTemplate = new TestRestTemplate();
        GuitaristDto test1 = new GuitaristDto(1,"Jimi","Hendrix","American");
        GuitaristDto test2 = new GuitaristDto(2,"Shawn","Lane","American");
        var result = restTemplate.postForEntity("http://localhost:" + port + "/guitarist", test1, GuitaristDto.class);
        var result2 = restTemplate.postForEntity("http://localhost:" + port + "/guitarist", test2, GuitaristDto.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result2.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    }
}
