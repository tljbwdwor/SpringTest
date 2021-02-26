package iths.tlj.lab2i.controllers;

import iths.tlj.lab2i.dtos.FirstNameDto;
import iths.tlj.lab2i.dtos.GuitaristDto;
import iths.tlj.lab2i.dtos.LastNameDto;
import iths.tlj.lab2i.dtos.NationalityDto;
import iths.tlj.lab2i.services.Service;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

public class TestService implements Service {


    @Override
    public GuitaristDto createGuitarist(GuitaristDto guitarist) {
        return new GuitaristDto(6,"Zakk","Wylde","American");
    }

    @Override
    public List<GuitaristDto> getAllGuitarists() {
        return List.of(new GuitaristDto(1,"Tom","Lloyd-Jones","British"), new GuitaristDto(2,"T2","T2","T2"),
                new GuitaristDto(3,"T3","T3","T3"));
    }

    @Override
    public Optional<GuitaristDto> getOne(int id) {
        if(id == 1)
        return Optional.of(new GuitaristDto(1,"Tom","Lloyd-Jones","British"));
        return Optional.empty();
    }

    @Override
    public List<GuitaristDto> findGuitaristsByFirst(String firstname) {
        if(firstname.equals("Tom"))
            return List.of(new GuitaristDto(1,"Tom","Lloyd-Jones","British"));
        else return null;
    }

    @Override
    public List<GuitaristDto> findGuitaristsByLast(String lastname) {
        if(lastname.equals("Lloyd-Jones"))
            return List.of(new GuitaristDto(1,"Tom","Lloyd-Jones","British"));
        else return null;
    }

    @Override
    public List<GuitaristDto> findGuitaristsByNationality(String nationality) {
        if(nationality.equals("British"))
            return List.of(new GuitaristDto(1,"Tom","Lloyd-Jones","British"));
        else return null;
    }


    @Override
    public GuitaristDto replace(int id, GuitaristDto guitaristDto) {
        GuitaristDto newData = new GuitaristDto(1, "T1", "T1", "T1");
        GuitaristDto data = Mockito.mock(GuitaristDto.class);
        //when(replace(1,data,newData))
        return guitaristDto;
    }

    @Override
    public GuitaristDto updateFirst(int id, FirstNameDto firstNameDto) {
        return null;
    }

    @Override
    public GuitaristDto updateLast(int id, LastNameDto lastNameDto) {
        return null;
    }

    @Override
    public GuitaristDto updateNationality(int id, NationalityDto nationalityDto) {
        return null;
    }

    @Override
    public void delete(int id) {

    }
}
