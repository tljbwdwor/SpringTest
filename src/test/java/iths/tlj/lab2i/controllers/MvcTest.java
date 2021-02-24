package iths.tlj.lab2i.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import iths.tlj.lab2i.dtos.GuitaristDto;
import iths.tlj.lab2i.services.Service;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GuitaristController.class)
public class MvcTest {

    @MockBean
    Service service;

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private final GuitaristDto TestDto = new GuitaristDto(1,"Jimi","Hendrix","American");
    private final GuitaristDto TestDto2 = new GuitaristDto(2,"Shawn","Lane","American");
    private final GuitaristDto NullDto = new GuitaristDto(1,null,null,null);

    //GET all
    @Test
    void validGetAllReturnsStatus200() throws Exception {
        when(service.getAllGuitarists()).thenReturn(List.of(new GuitaristDto()));

        var result = mockMvc.perform(MockMvcRequestBuilders.get("/guitarists")
        .accept(MediaType.APPLICATION_JSON)).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
    }

    @Test
    void validGetAllReturnsAllObjectsAsJson() throws Exception {
        when(service.getAllGuitarists()).thenReturn(List.of(TestDto, TestDto2));

        var result = mockMvc.perform(MockMvcRequestBuilders.get("/guitarists")
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(result).isNotNull();

    }

    //GET by id
    @Test
    void validGetByIdReturnsOneAsJson() throws Exception {
        when(service.getOne(1)).thenReturn(Optional.of(new GuitaristDto(1,"T","T","T")));

        var result = mockMvc.perform(MockMvcRequestBuilders.get("/guitarist/1")
        .accept(MediaType.APPLICATION_JSON)).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(result).isNotNull();
    }

    @Test
    void invalidGetByIdThrowsNotFoundException() throws Exception {
        when(service.getOne(1)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        var result = mockMvc.perform(MockMvcRequestBuilders.get("/guitarist/1")
        .accept(MediaType.APPLICATION_JSON)).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(404);
    }

    //GET by Firstname
    @Test
    void validGetByFirstReturnsWithStatus200() throws Exception {
        when(service.findGuitaristsByFirst("Jimi")).thenReturn(List.of(TestDto));

        var result = mockMvc.perform(MockMvcRequestBuilders.get("/firstname/jimi")
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(result).isNotNull();
    }

    @Test
    void invalidGetByFirstReturnsOneAsJson() throws Exception {
        when(service.findGuitaristsByFirst("jimi")).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        var result = mockMvc.perform(MockMvcRequestBuilders.get("/firstname/jimi")
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(404);
    }

    //GET by Lastname
    @Test
    void validGetByLastReturnsWithStatus200() throws Exception {
        when(service.findGuitaristsByLast("Hendrix")).thenReturn(List.of(TestDto));

        var result = mockMvc.perform(MockMvcRequestBuilders.get("/lastname/hendrix")
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(result).isNotNull();
    }

    @Test
    void invalidGetByLastReturnsOneAsJson() throws Exception {
        when(service.findGuitaristsByLast("hendrix")).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        var result = mockMvc.perform(MockMvcRequestBuilders.get("/lastname/hendrix")
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(404);
    }

    //GET by nationality
    @Test
    void validGetByNationalityReturnsWithStatus200() throws Exception {
        when(service.findGuitaristsByNationality("American")).thenReturn(List.of(TestDto, TestDto2));

        var result = mockMvc.perform(MockMvcRequestBuilders.get("/nationality/american")
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(result).isNotNull();
    }

    @Test
    void invalidGetByNationalityOneAsJson() throws Exception {
        when(service.findGuitaristsByNationality("american")).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        var result = mockMvc.perform(MockMvcRequestBuilders.get("/nationality/american")
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(404);
    }


    //POST tests
    @Test
    //looking for 201 CREATED status
    void postRequestAtGuitaristUrlGivesStatus201() throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/guitarist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"id": 1,
                                    "firstName": "Jimi",
                                    "lastName": "Hendrix",
                                    "nationality": "American"
                                  }""")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(result).isNotNull();
        assertThat(result.getResponse().getStatus()).isEqualTo(201);
    }

    //NOT WORKING: keeps returning 201 created
    @Test
    void invalidPostRequestThrows400Status() throws Exception {
            mockMvc.perform(post("/guitarist").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new GuitaristDto(1,null,null,null))))
                .andExpect(status().isBadRequest());
    }

    //PUT tests

    //PATCH tests

    //DELETE tests
}
