
package iths.tlj.lab2i.controllers;

import iths.tlj.lab2i.dtos.GuitaristDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GuitaristControllerTest {

    //Isolated unit tests.
    //Initialising outside of methods which can all call
    GuitaristController guitaristController = new GuitaristController(new TestService());

    //CREATE TESTS
    @Test
    void createReturnsValidAndCorrectGuitarist() {
        var newG = guitaristController.createGuitarist(new GuitaristDto());

        assertThat(newG.getId()).isEqualTo(4);
        assertThat(newG.getFirstName()).isEqualTo("Zakk");
        assertThat(newG.getLastName()).isEqualTo("Wylde");
        assertThat(newG.getNationality()).isEqualTo("American");
    }

    //READ TESTS
    @Test
    void findOneByValidIdReturnsGuitarist(){
        var guitarist = guitaristController.findGuitaristById(1);

        assertThat(guitarist.getId()).isEqualTo(1);
        assertThat(guitarist.getFirstName()).isEqualTo("Tom");
        assertThat(guitarist.getLastName()).isEqualTo("Lloyd-Jones");
        assertThat(guitarist.getNationality()).isEqualTo("British");
    }

    @Test
    void findOneByInvalidIdThrows404Exception(){
        var exception = assertThrows(ResponseStatusException.class, () -> guitaristController.findGuitaristById(56));
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void findAllReturnsCorrectList() {
        List <GuitaristDto> list = guitaristController.listAllGuitarists();

        assertFalse(list.isEmpty());
        assertEquals(list.size(), 3);

        assertThat(list.get(0).getId()).isEqualTo(1);
        assertThat(list.get(0).getFirstName()).isEqualTo("Tom");
        assertThat(list.get(0).getLastName()).isEqualTo("Lloyd-Jones");
        assertThat(list.get(0).getNationality()).isEqualTo("British");

        assertThat(list.get(1).getId()).isEqualTo(2);
        assertThat(list.get(1).getFirstName()).isEqualTo("T2");
        assertThat(list.get(1).getLastName()).isEqualTo("T2");
        assertThat(list.get(1).getNationality()).isEqualTo("T2");

        assertThat(list.get(2).getId()).isEqualTo(3);
        assertThat(list.get(2).getFirstName()).isEqualTo("T3");
        assertThat(list.get(2).getLastName()).isEqualTo("T3");
        assertThat(list.get(2).getNationality()).isEqualTo("T3");

    }

    @Test
    void findByFirstReturnsCorrectList() {
        List <GuitaristDto> list = guitaristController.findAllByFirstName("Tom");

        assertFalse(list.isEmpty());
        assertEquals(list.size(), 1);
        assertThat(list.get(0).getId()).isEqualTo(1);
        assertThat(list.get(0).getFirstName()).isEqualTo("Tom");
        assertThat(list.get(0).getLastName()).isEqualTo("Lloyd-Jones");
        assertThat(list.get(0).getNationality()).isEqualTo("British");
    }

    @Test
    void findByLastReturnsCorrectList() {
        List <GuitaristDto> list = guitaristController.findAllByLastName("Lloyd-Jones");

        assertFalse(list.isEmpty());
        assertEquals(list.size(), 1);
        assertThat(list.get(0).getId()).isEqualTo(1);
        assertThat(list.get(0).getFirstName()).isEqualTo("Tom");
        assertThat(list.get(0).getLastName()).isEqualTo("Lloyd-Jones");
        assertThat(list.get(0).getNationality()).isEqualTo("British");
    }

    @Test
    void findByNationalityReturnsCorrectList() {
        List <GuitaristDto> list = guitaristController.findAllByNationality("British");

        assertFalse(list.isEmpty());
        assertEquals(list.size(), 1);
        assertThat(list.get(0).getId()).isEqualTo(1);
        assertThat(list.get(0).getFirstName()).isEqualTo("Tom");
        assertThat(list.get(0).getLastName()).isEqualTo("Lloyd-Jones");
        assertThat(list.get(0).getNationality()).isEqualTo("British");
    }

    //UPDATE TESTS
    @Test
    public void replaceReturnsDifferentObject() {
        var guitarist = guitaristController.replace(new GuitaristDto(1,"U","U","U"),1);

        assertThat(guitarist.getId()).isEqualTo(1);
        assertThat(guitarist.getFirstName()).isEqualTo("U");
        assertThat(guitarist.getLastName()).isEqualTo("U");
        assertThat(guitarist.getNationality()).isEqualTo("U");
    }
}
