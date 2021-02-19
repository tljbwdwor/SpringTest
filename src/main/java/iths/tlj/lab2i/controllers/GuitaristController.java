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
@RestController("/guitarists")
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
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)     //Gives a 201 CREATED response instead of simply 200 OK.
    public GuitaristDto createGuitarist(@RequestBody GuitaristDto guitarist) {
        return guitaristService.createGuitarist(guitarist);
    }


    //READ (GET) METHODS
    //map method to url. The get request at url returns list of all users
    @GetMapping()
    public List<GuitaristDto> listAllGuitarists() {
        return guitaristService.getAllGuitarists();
    }

    //get request at url returns single object from id
    @GetMapping("/{id}")
    public Optional<GuitaristDto> findGuitaristById(@PathVariable int id) {
        return guitaristService.getOne(id);
    }

   /* @GetMapping("/firstname/{firstname}")
    public List<Guitarist> findGuitaristsByFirst(@PathVariable String firstname) {
        if (guitaristRepository.findAllByFirstName(firstname).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }else
        return guitaristRepository.findAllByFirstName(firstname);
    }

    @GetMapping("/lastname/{lastname}")
    public List<Guitarist> findGuitaristsByLast(@PathVariable String lastname) {
        if (guitaristRepository.findAllByLastName(lastname).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }else
        return guitaristRepository.findAllByLastName(lastname);
    }

    @GetMapping("/nationality/{nationality}")
    public List<Guitarist> findGuitaristsByNationality(@PathVariable String nationality) {
        if (guitaristRepository.findAllByNationality(nationality).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }else
        return guitaristRepository.findAllByNationality(nationality);
    }*/

    //UPDATE (PUT) METHODS

    @PutMapping("/{id}")
    public GuitaristDto replace(@RequestBody GuitaristDto guitaristDto, @PathVariable int id){
        return guitaristService.replace(id, guitaristDto);
    }

    @PatchMapping("/firstname/{id}")
    public GuitaristDto updateFirst(@RequestBody FirstNameDto firstNameDto, @PathVariable int id){
        return guitaristService.updateFirst(id, firstNameDto);
    }

    @PatchMapping("/lastname/{id}")
    public GuitaristDto updateLast(@RequestBody LastNameDto lastNameDto, @PathVariable int id){
        return guitaristService.updateLast(id, lastNameDto);
    }

    @PatchMapping("/nationality/{id}")
    public GuitaristDto updateNationality(@RequestBody NationalityDto nationalityDto, @PathVariable int id){
        return guitaristService.updateNationality(id, nationalityDto);
    }

   /* @PutMapping("update")
    public ResponseEntity<?> updateGuitarist(@RequestBody Guitarist guitarist) {
        if (!guitaristRepository.existsById(guitarist.getId())){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }else
            guitaristRepository.save(guitarist);
        System.out.println("Guitarist has been updated.");
        return ResponseEntity.ok("Entry updated to:\n" + guitarist.getId() + "\n" + guitarist.getFirstName() +
                "\n" + guitarist.getLastName() + "\n" + guitarist.getNationality());
    }

    //Update by ID
    @PutMapping("/update/{id}")
    Guitarist replaceGuitarist(@RequestBody Guitarist newGuitarist, @PathVariable int id){
        return guitaristRepository.findById(id).map(guitarist -> {
            guitarist.setFirstName(newGuitarist.getFirstName());
            guitarist.setLastName(newGuitarist.getLastName());
            guitarist.setNationality(newGuitarist.getNationality());
            return guitaristRepository.save(guitarist);
        }).orElseGet(() -> {
            newGuitarist.setId(id);
            return guitaristRepository.save(newGuitarist);
        });
    }*/


    //NEED PATCH METHODS HERE


    //https://nullbeans.com/using-put-vs-patch-when-building-a-rest-api-in-spring/#How_to_Configure_HTTP_PATCH_in_a_REST_controller_in_Spring




    //DELETE METHODS
    //url for deleting an object. If the ID does not exist, exception is thrown.
    //@PostMapping("/delete")
    @DeleteMapping("/{id}")
    public String deleteGuitarist(@PathVariable int id) {
        guitaristService.delete(id);
        return "Guitarist with id " + id + " has been deleted.";
        /*if(guitaristRepository.existsById(guitarist.getId())) {
            guitaristRepository.delete(guitarist);
            System.out.println("Guitarist deleted.");
            return ResponseEntity.ok("Deleted entry with ID: " + guitarist.getId());
        } else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);*/
    }
}
