package iths.tlj.lab2i.services;

import iths.tlj.lab2i.dtos.FirstNameDto;
import iths.tlj.lab2i.dtos.GuitaristDto;
import iths.tlj.lab2i.dtos.LastNameDto;
import iths.tlj.lab2i.dtos.NationalityDto;
import iths.tlj.lab2i.entities.Guitarist;
import iths.tlj.lab2i.mappers.GuitaristMapper;
import iths.tlj.lab2i.repositories.GuitaristRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class GuitaristService implements iths.tlj.lab2i.services.Service {
    private final GuitaristMapper guitaristMapper = new GuitaristMapper();
    //sits between Controller and repository to help validation.
    //for every method in Controller, we need an answering method here.
    private GuitaristRepository guitaristRepository;

    public GuitaristService(GuitaristRepository guitaristRepository) {
        this.guitaristRepository = guitaristRepository;
    }

    //CREATE (POST) METHODS
    public GuitaristDto createGuitarist(GuitaristDto guitarist) {
        //validate here
        if (guitarist.getFirstName().isEmpty() || guitarist.getNationality().isEmpty())
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        return guitaristMapper.map(guitaristRepository.save(guitaristMapper.map(guitarist)));
    }

    //READ (GET) METHODS
    public List<GuitaristDto> getAllGuitarists() {
        return guitaristMapper.map(guitaristRepository.findAll());
    }

    public Optional<GuitaristDto> getOne(int id) {
        if(guitaristMapper.map(guitaristRepository.findById(id)).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    else
            return guitaristMapper.map(guitaristRepository.findById(id));
    }

    public List<GuitaristDto> findGuitaristsByFirst(String firstName) {
        if(guitaristMapper.map(guitaristRepository.findAllByFirstName(firstName)).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return guitaristMapper.map(guitaristRepository.findAllByFirstName(firstName));
    }

    public List<GuitaristDto> findGuitaristsByLast(String lastName) {
        if(guitaristMapper.map(guitaristRepository.findAllByLastName(lastName)).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return guitaristMapper.map(guitaristRepository.findAllByLastName(lastName));
    }

    public List<GuitaristDto> findGuitaristsByNationality(String nationality) {
        if(guitaristMapper.map(guitaristRepository.findAllByNationality(nationality)).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return guitaristMapper.map(guitaristRepository.findAllByNationality(nationality));
    }

    //UPDATE (PUT) METHODS
    public GuitaristDto replace(int id, GuitaristDto guitaristDto) {
        Optional<Guitarist> guitarist = guitaristRepository.findById(id);
        if(guitarist.isPresent())
        {
            Guitarist UpdatedGuitarist = guitarist.get();
            UpdatedGuitarist.setFirstName(guitaristDto.getFirstName());
            UpdatedGuitarist.setLastName(guitaristDto.getLastName());
            UpdatedGuitarist.setNationality(guitaristDto.getNationality());
            return guitaristMapper.map(guitaristRepository.save(UpdatedGuitarist));
        }
        else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id " + id + " not found.");
    }

    //UPDATE (PATCH) METHODS
    public GuitaristDto updateFirst(int id, FirstNameDto firstNameDto) {
        Optional<Guitarist> guitarist = guitaristRepository.findById(id);
        if(guitarist.isPresent())
        {
            Guitarist UpdatedGuitarist = guitarist.get();
            if (firstNameDto.firstName != null)
                UpdatedGuitarist.setFirstName(firstNameDto.firstName);
            return guitaristMapper.map(guitaristRepository.save(UpdatedGuitarist));
        }
        else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id " + id + " not found.");
    }

    public GuitaristDto updateLast(int id, LastNameDto guitaristDto) {
        Optional<Guitarist> guitarist = guitaristRepository.findById(id);
        if(guitarist.isPresent())
        {
            Guitarist UpdatedGuitarist = guitarist.get();
            if (guitaristDto.lastName != null)
                UpdatedGuitarist.setLastName(guitaristDto.lastName);
            return guitaristMapper.map(guitaristRepository.save(UpdatedGuitarist));
        }
        else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id " + id + " not found.");
    }

    public GuitaristDto updateNationality(int id, NationalityDto guitaristDto) {
        Optional<Guitarist> guitarist = guitaristRepository.findById(id);
        if(guitarist.isPresent())
        {
            Guitarist UpdatedGuitarist = guitarist.get();
            if (guitaristDto.nationality != null)
                UpdatedGuitarist.setNationality(guitaristDto.nationality);
            return guitaristMapper.map(guitaristRepository.save(UpdatedGuitarist));
        }
        else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id " + id + " not found.");
    }

    //DELETE METHODS
    public void delete(int id) {
        if (!guitaristRepository.existsById(id))
            throw new RuntimeException();
        else
        guitaristRepository.deleteById(id);
    }


}
