package iths.tlj.lab2i.controllers;

import iths.tlj.lab2i.dtos.FirstNameDto;
import iths.tlj.lab2i.dtos.GuitaristDto;
import iths.tlj.lab2i.dtos.LastNameDto;
import iths.tlj.lab2i.dtos.NationalityDto;
import iths.tlj.lab2i.services.GuitaristService;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Data
@RestController
public class GuitaristController {

    //ORIGINAL VERSION WITHOUT SERVICE CLASS OR DTO
    //private GuitaristRepository guitaristRepository;

    //Autowired annotation ensures that this is automatically created when a Controller class is made
    /*@Autowired
    public GuitaristController(GuitaristRepository guitaristRepository) {
        this.guitaristRepository = guitaristRepository;
    }*/

    //Using dependency injection here; creating new instances below, we would need all the @Override methods in the code
    private GuitaristService guitaristService;

    public GuitaristController(GuitaristService guitaristService){this.guitaristService = guitaristService;}



    //TEST HTTP METHOD
    //simple test class using Lombok to avoid boilerplate
    @Data
    public class Greeting {

        private final long id;
        private final String content;

        public Greeting(long id, String content) {
            this.id = id;
            this.content = content;
        }
    }
    //simple test method to print hello + name or default
    private static final String template = "Good day to you, %s!";
    private final AtomicLong counter = new AtomicLong();
    @GetMapping("/hi")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "my dude") String name){
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }


    //CREATE (POST) METHODS
    //If you make a post request at this address you will create a new Json object in the database.
    @PostMapping("/guitarist")
    @ResponseStatus(HttpStatus.CREATED)     //Gives a 201 CREATED response instead of simply 200 OK.
    public GuitaristDto createGuitarist(@RequestBody GuitaristDto guitarist) {
        return guitaristService.createGuitarist(guitarist);
    }


    //READ (GET) METHODS
    //map method to url. The get request at url returns list of all users
    @GetMapping("/guitarists")
    public List<GuitaristDto> listAllGuitarists() {
        return guitaristService.getAllGuitarists();
    }

    //get request at url returns single object from id
    @GetMapping("/guitarist/{id}")
    public Optional<GuitaristDto> findGuitaristById(@PathVariable int id) {
        return guitaristService.getOne(id);
    }

    //get request at url returns list of all sharing first name (last name and nationality below)
    @GetMapping("/firstname/{firstname}")
    public List<GuitaristDto> findAllByFirstName(@PathVariable String firstname) {
        return guitaristService.findGuitaristsByFirst(firstname);
    }

    @GetMapping("lastname/{lastname}")
    public List<GuitaristDto> findAllByLastName(@PathVariable String lastname) {
        return guitaristService.findGuitaristsByLast(lastname);
    }

    @GetMapping("nationality/{nationality}")
    public List<GuitaristDto> findAllByNationality(@PathVariable String nationality) {
        return guitaristService.findGuitaristsByNationality(nationality);
    }


    //REPLACE (PUT) METHODS
    @PutMapping("/guitarists/{id}")
    public GuitaristDto replace(@RequestBody GuitaristDto guitaristDto, @PathVariable int id){
        return guitaristService.replace(id, guitaristDto);
    }

    //UPDATE (PATCH) METHODS
    @PatchMapping("/guitarists/firstname/{id}")
    public GuitaristDto updateFirst(@RequestBody FirstNameDto firstNameDto, @PathVariable int id){
        return guitaristService.updateFirst(id, firstNameDto);
    }

    @PatchMapping("/guitarists/lastname/{id}")
    public GuitaristDto updateLast(@RequestBody LastNameDto lastNameDto, @PathVariable int id){
        return guitaristService.updateLast(id, lastNameDto);
    }

    @PatchMapping("/guitarists/nationality/{id}")
    public GuitaristDto updateNationality(@RequestBody NationalityDto nationalityDto, @PathVariable int id){
        return guitaristService.updateNationality(id, nationalityDto);
    }


    //DELETE METHODS
    @DeleteMapping("/delete/{id}")
    public String deleteGuitarist(@PathVariable int id) {
        guitaristService.delete(id);
        return "Guitarist with id " + id + " has been deleted.";
    }
}
