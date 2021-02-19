package iths.tlj.lab2i.mappers;

import iths.tlj.lab2i.dtos.GuitaristDto;
import iths.tlj.lab2i.entities.Guitarist;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class GuitaristMapper {
    public GuitaristMapper() {
    }

    //methods to copy info from DTO to object & vv
    public GuitaristDto map(Guitarist guitarist) {
        return new GuitaristDto(guitarist.getId(), guitarist.getFirstName(), guitarist.getLastName(),
                guitarist.getNationality());
    }

    public Guitarist map(GuitaristDto guitaristDto) {
        return new Guitarist(guitaristDto.getId(), guitaristDto.getFirstName(), guitaristDto.getLastName(),
                guitaristDto.getNationality());
    }//method to cover mapping for the Optional requirement in getOne()

    public Optional<GuitaristDto> map(Optional<Guitarist> optionalGuitarist) {
        if (optionalGuitarist.isEmpty())
            return Optional.empty();
        return Optional.of(map(optionalGuitarist.get()));
    }//method to cover mapping to list required in getAll()

    public List<GuitaristDto> map(List<Guitarist> all) {
        return all
                .stream()
                .map(this::map) //
                //.map(guitarist -> map(guitarist))
                //.filter(guitaristDto -> guitaristDto.getId() < 5)  //filters response
                //.limit(100)   //returns first 100
                .collect(Collectors.toList());
    }
}