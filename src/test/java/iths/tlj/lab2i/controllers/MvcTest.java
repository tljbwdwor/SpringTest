package iths.tlj.lab2i.controllers;

import iths.tlj.lab2i.dtos.GuitaristDto;
import iths.tlj.lab2i.services.Service;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@WebMvcTest(GuitaristController.class)
public class MvcTest {

    @MockBean
    Service service;

    @Autowired
    private MockMvc mockMvc;
    private final GuitaristDto TestDto = new GuitaristDto(1,"Jimi","Hendrix","American");


    @Test
    void urlGuitaristsReturnsAllObjectsAsJason() throws Exception {
        //Test that getAll returns status of 200 and that it is not null
        when(service.getAllGuitarists()).thenReturn(List.of(new GuitaristDto()));

        var result = mockMvc.perform(MockMvcRequestBuilders.get("/guitarists")
        .accept(MediaType.APPLICATION_JSON)).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(result).isNotNull();


        //Test that created item gives a status of 201
        var result2 =
                mockMvc.perform(MockMvcRequestBuilders.post("/guitarist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"id": 1,
                                    "firstName": "Jimi",
                                    "lastName": "Hendrix",
                                    "nationality": "American"
                                  }""")
                        .accept(MediaType.APPLICATION_JSON))
                        .andReturn();


        assertThat(result2.getResponse().getStatus()).isEqualTo(201);

    }
}
