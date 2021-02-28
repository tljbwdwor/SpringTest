package iths.tlj.lab2i.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import iths.tlj.lab2i.dtos.FirstNameDto;
import iths.tlj.lab2i.dtos.GuitaristDto;
import iths.tlj.lab2i.dtos.LastNameDto;
import iths.tlj.lab2i.dtos.NationalityDto;
import iths.tlj.lab2i.services.Service;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    private final GuitaristDto NullDto = new GuitaristDto(0,null,"Smith","American");

    public static String makeJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //CREATE (post)
    @Test
    void postRequestSavesNewGuitaristAndReturns201() throws Exception {
        when(service.createGuitarist(any(GuitaristDto.class))).thenReturn(TestDto);
        var result = mockMvc.perform(post("/guitarists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(TestDto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nationality").value("American"))
                .andExpect(status().isCreated())
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(201);
    }

    //INVALID CREATE
    @Test
    void invalidPostRequestReturnsBadRequest() throws Exception {
        when(service.createGuitarist(NullDto)).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        var result = mockMvc.perform(post("/guitarists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(NullDto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(400);
    }


    //GET ALL
    @Test
    void getAllReturnsAllObjectsAsJsonAndStatus200() throws Exception {
        when(service.getAllGuitarists()).thenReturn(List.of(TestDto, TestDto2));

        var result = mockMvc.perform(MockMvcRequestBuilders.get("/guitarists")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("Jimi"))
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
    }


    //GET ONE BY ID
    @Test
    void validGetByIdReturnsOneAsJson() throws Exception {
        when(service.getOne(1)).thenReturn(Optional.of(TestDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/guitarists/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jimi"))
                .andReturn();
    }

    //INVALID GET ONE BY ID
    @Test
    void invalidGetByIdThrows404NotFoundException() throws Exception {
        when(service.getOne(4)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        var result = mockMvc.perform(MockMvcRequestBuilders.get("/guitarists/4")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    //SEARCH BY FIRST NAME
    @Test
    void validGetByFirstReturnsJsonListWithStatus200() throws Exception {
        List list = List.of(TestDto);
        when(service.findGuitaristsByFirst(any())).thenReturn(list);

        mockMvc.perform(MockMvcRequestBuilders.get("/guitarists/firstname/Jimi")
                .content(makeJson(list))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0]lastName").value("Hendrix"))
                .andExpect(status().isOk())
                .andReturn();
    }

    //INVALID SEARCH BY FIRST NAME
    @Test
    void invalidGetByFirstReturnsStatus404() throws Exception {
        List list = List.of(TestDto);

        when(service.findGuitaristsByFirst(any())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(MockMvcRequestBuilders.get("/guitarists/firstname/X")
                .content(makeJson(list))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    //SEARCH BY LAST NAME
    @Test
    void validGetByLastReturnsJsonListWithStatus200() throws Exception {
        List list = List.of(TestDto);
        when(service.findGuitaristsByLast(any())).thenReturn(list);

        mockMvc.perform(MockMvcRequestBuilders.get("/guitarists/lastname/Hendrix")
                .content(makeJson(list))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0]firstName").value("Jimi"))
                .andExpect(status().isOk())
                .andReturn();
    }

    //INVALID SEARCH BY LAST NAME
    @Test
    void invalidGetByLastReturnsStatus404() throws Exception {
        List list = List.of(TestDto);

        when(service.findGuitaristsByLast(any())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(MockMvcRequestBuilders.get("/guitarists/lastname/Jimi")
                .content(makeJson(list))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    //SEARCH BY NATIONALITY
    @Test
    void validGetByNationalityReturnsJsonListWithStatus200() throws Exception {
        List list = List.of(TestDto, TestDto2);
        when(service.findGuitaristsByNationality(any())).thenReturn(list);

        mockMvc.perform(MockMvcRequestBuilders.get("/guitarists/nationality/American")
                .content(makeJson(list))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0]firstName").value("Jimi"))
                .andExpect(jsonPath("$.[1]lastName").value("Lane"))
                .andExpect(status().isOk())
                .andReturn();
    }

    //INVALID SEARCH BY NATIONALITY
    @Test
    void invalidGetByNationalityReturnsStatus404() throws Exception {
        List list = List.of(TestDto);

        when(service.findGuitaristsByNationality(any())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(MockMvcRequestBuilders.get("/guitarists/nationality/American")
                .content(makeJson(list))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    //REPLACE (put)
    @Test
    void replaceViaPutValidIdGivesResponseOk() throws Exception {
        GuitaristDto newG = new GuitaristDto(1,"X","Y","Z");

        when(service.replace(eq(1),any(GuitaristDto.class))).thenReturn(newG);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/guitarists/{id}",1)
                .content(makeJson(newG))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(newG.getFirstName()))
                .andExpect(status().isOk());
    }

    //INVALID REPLACE
    @Test
    void replaceViaInvalidPutGivesNotFound() throws Exception {
        GuitaristDto newG = new GuitaristDto(1,"X","Y","Z");

        when(service.replace(anyInt(),any(GuitaristDto.class))).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(MockMvcRequestBuilders
                .put("/guitarists/1")
                .content(makeJson(newG))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    //UPDATE firstname
    @Test
    void patchFirstNameRequestGivesResponseOk() throws Exception {
        when(service.updateFirst(eq(1),any(FirstNameDto.class))).thenReturn(TestDto);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/guitarists/firstname/{id}",1)
                .content(makeJson(TestDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(TestDto.getFirstName()))
                .andExpect(status().isOk());
    }

    //INVALID UPDATE firstname
    @Test
    void patchFirstNameInvalidRequestReturnsNotFound() throws Exception {
        when(service.updateFirst(anyInt(),any(FirstNameDto.class))).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(MockMvcRequestBuilders.patch("/guitarists/firstname/1")
                .content(makeJson(TestDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    //UPDATE lastname
    @Test
    void patchLastNameRequestGivesResponseOk() throws Exception {
        when(service.updateLast(eq(1),any(LastNameDto.class))).thenReturn(TestDto);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/guitarists/lastname/{id}",1)
                .content(makeJson(TestDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(TestDto.getLastName()))
                .andExpect(status().isOk());
    }

    //INVALID UPDATE lastname
    @Test
    void patchLastNameInvalidRequestReturnsNotFound() throws Exception {
        when(service.updateLast(anyInt(),any(LastNameDto.class))).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(MockMvcRequestBuilders.patch("/guitarists/lastname/1")
                .content(makeJson(TestDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    //UPDATE nationality
    @Test
    void patchNationalityRequestGivesResponseOk() throws Exception {
        when(service.updateNationality(eq(1),any(NationalityDto.class))).thenReturn(TestDto);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/guitarists/nationality/{id}",1)
                .content(makeJson(TestDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nationality").value(TestDto.getNationality()))
                .andExpect(status().isOk());
    }

    //INVALID UPDATE nationality
    @Test
    void patchNationalityInvalidRequestReturnsNotFound() throws Exception {
        when(service.updateNationality(anyInt(),any(NationalityDto.class))).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(MockMvcRequestBuilders.patch("/guitarists/nationality/1")
                .content(makeJson(TestDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    //DELETE
    @Test
    void deleteRequestValidIdReturnsOK() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/guitarists/delete/{id}",1))
                .andExpect(status().isOk());
    }

    //INVALID DELETE
    @Test
    void deleteRequestInvalidReturns400() throws Exception {
       doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(service).delete(1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/guitarists/delete/{id}",1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
