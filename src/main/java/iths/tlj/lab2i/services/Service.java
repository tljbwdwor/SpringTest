package iths.tlj.lab2i.services;

import iths.tlj.lab2i.dtos.FirstNameDto;
import iths.tlj.lab2i.dtos.GuitaristDto;
import iths.tlj.lab2i.dtos.LastNameDto;
import iths.tlj.lab2i.dtos.NationalityDto;

import java.util.List;
import java.util.Optional;

public interface Service {

    GuitaristDto createGuitarist(GuitaristDto guitarist);

    List<GuitaristDto> getAllGuitarists();

    Optional<GuitaristDto> getSingleGuitarist();

    GuitaristDto replace(int id, GuitaristDto guitaristDto);

    GuitaristDto updateFirst(int id, FirstNameDto firstNameDto);
    GuitaristDto updateLast(int id, LastNameDto lastNameDto);
    GuitaristDto updateNationality(int id, NationalityDto nationalityDto);

    void delete(int id);
}
