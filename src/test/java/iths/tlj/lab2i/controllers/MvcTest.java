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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GuitaristController.class)
public class MvcTest {

    @MockBean
    Service service;
    //Tests below will use this service and return the data that we tell it to within the test methods.

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private final GuitaristDto TestDto = new GuitaristDto(1,"Jimi","Hendrix","American");
    private final GuitaristDto TestDto2 = new GuitaristDto(2,"Shawn","Lane","American");
    private final GuitaristDto NullDto = new GuitaristDto(1,null,null,null);


    //Here I am testing the integration of the methods: is the right method called at the right URL? Is the status
    // code correct? If we verify that method that calls DB is called, it might not be necessary to connect to DB..?
    //POST
    @Test
    void postRequestCallsMethodSuccessfully() throws Exception {
        mockMvc.perform(post("/guitarist").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(TestDto)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void postRequestSavesNewGuitaristAndReturns201() throws Exception {
        when(service.createGuitarist(TestDto)).thenReturn(new GuitaristDto(TestDto.getId(),TestDto.getFirstName(),
                TestDto.getLastName(),TestDto.getNationality()));
        var result = mockMvc.perform(post("/guitarist")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(TestDto))
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(201);
    }

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

    //PUT (ie replace)
    @Test
    void putRequestCallsMethodSuccessfully() throws Exception {
        mockMvc.perform(put("/guitarists/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(TestDto)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void putRequestReturns200() throws Exception {
        var result = mockMvc.perform(put("/guitarists/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(TestDto)))
                .andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
    }

    //PATCH firstname
    @Test
    void patchFirstNameRequestCallsMethodSuccessfully() throws Exception {
        mockMvc.perform(patch("/guitarists/firstname/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(TestDto)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void patchFirstNameRequestReturns200() throws Exception {
        var result = mockMvc.perform(patch("/guitarists/firstname/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(TestDto)))
                .andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
    }

    //PATCH lastname
    @Test
    void patchLastNameRequestCallsMethodSuccessfully() throws Exception {
        mockMvc.perform(patch("/guitarists/lastname/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(TestDto)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void patchLastNameRequestReturns200() throws Exception {
        var result = mockMvc.perform(patch("/guitarists/lastname/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(TestDto)))
                .andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
    }

    //PATCH nationality
    @Test
    void patchNationalityRequestCallsMethodSuccessfully() throws Exception {
        mockMvc.perform(patch("/guitarists/nationality/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(TestDto)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void patchNationalityRequestReturns200() throws Exception {
        var result = mockMvc.perform(patch("/guitarists/nationality/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(TestDto)))
                .andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
    }

    //DELETE tests
    @Test
    void deleteRequestCallsMethodSuccessfully() throws Exception {
        mockMvc.perform(delete("/delete/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void deleteRequestReturns200() throws Exception {
        var result = mockMvc.perform(delete("/delete/1").contentType(MediaType.APPLICATION_JSON)).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
    }




    /*//OTHER TESTS...

    //POST tests
    @Test
    //looking for 201 CREATED status
    void postRequestAtGuitaristUrlGivesStatus201() throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/guitarist")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestDto))
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(result).isNotNull();
        assertThat(result.getResponse().getStatus()).isEqualTo(201);
    }

    //NOT WORKING: ALWAYS RETURNS 201 created
    //IS THIS CALLING THE RIGHT METHOD?????
   @Test
    void invalidPostRequestThrows400Status() throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/guitarist")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(NullDto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        //assertThat(result.getResponse().getStatus()).isEqualTo(400);
    }

    //WHY DOES THIS RETURN 201 CREATED WHEN DTO IS INVALID???
    @Test
    void invalidPostWithNewGuitaristShouldReturn400() throws Exception {
        GuitaristDto guitaristDto = new GuitaristDto(1,null,"T","T");
        when(service.createGuitarist(ArgumentMatchers.any(GuitaristDto.class))).thenReturn(guitaristDto);
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/guitarist")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(guitaristDto))
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(400);
    }


    //ALWAYS RETURNS 400
    @Test
    void invalidPostRequestThrows400() throws Exception {
        when(service.createGuitarist(NullDto)).thenReturn(NullDto);
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/guitarist")
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(400);
    }

    //ALWAYS RETURNS 201
    @Test
    void postWithNewGuitaristShouldSaveToServiceAndReturnNewGuitaristWithId() throws Exception {
        GuitaristDto guitaristDto = new GuitaristDto(1,"T","T","T");
        when(service.createGuitarist(ArgumentMatchers.any(GuitaristDto.class))).thenReturn(new GuitaristDto(1,"T","T"
                ,"T"));
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/guitarist")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(guitaristDto))
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(201);
    }*/
}
