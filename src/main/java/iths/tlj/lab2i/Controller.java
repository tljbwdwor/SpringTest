package iths.tlj.lab2i;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Data
@RestController
public class Controller {

    //Using dependency injection here; if we were to create a new instance below, we would need all the @Override
    // methods in the code
    private GuitaristRepository guitaristRepository;

    //Autowired annotation ensures that this is automatically created when a Controller class is made
    @Autowired
    public Controller(GuitaristRepository guitaristRepository) {
        this.guitaristRepository = guitaristRepository;
    }

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
    public ResponseEntity<?> createGuitarist(@RequestBody Guitarist guitarist) {
        if (guitaristRepository.existsById(guitarist.getId())){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }else
        System.out.println("New guitarist object created");
        guitaristRepository.save(guitarist);
        return ResponseEntity.ok("New entry to database:\n" + guitarist.getId() + "\n" + guitarist.getFirstName() +
                        "\n" + guitarist.getLastName() + "\n" + guitarist.getNationality() + "\nhas been created.");
    }


    //READ (GET) METHODS
    //map method to url. The get request at url returns list of all users
    @GetMapping("/guitarists")
    @ResponseStatus(HttpStatus.OK)
    public List<Guitarist> listAllGuitarists() {
        if (guitaristRepository.findAll().isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return guitaristRepository.findAll();
    }

    //get request at url returns single object from id
    @GetMapping("/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Guitarist findGuitaristById(@PathVariable int id) {
        if(!guitaristRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }else
        return guitaristRepository.findById(id)
                .orElse(new Guitarist());
    }

    @GetMapping("/firstname/{firstname}")
    @ResponseStatus(HttpStatus.OK)
    public List<Guitarist> findGuitaristsByFirst(@PathVariable String firstname) {
        if (guitaristRepository.findAllByFirstName(firstname).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }else
        return guitaristRepository.findAllByFirstName(firstname);
    }

    @GetMapping("/lastname/{lastname}")
    @ResponseStatus(HttpStatus.OK)
    public List<Guitarist> findGuitaristsByLast(@PathVariable String lastname) {
        if (guitaristRepository.findAllByLastName(lastname).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }else
        return guitaristRepository.findAllByLastName(lastname);
    }

    @GetMapping("/nationality/{nationality}")
    @ResponseStatus(HttpStatus.OK)
    public List<Guitarist> findGuitaristsByNationality(@PathVariable String nationality) {
        if (guitaristRepository.findAllByNationality(nationality).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }else
        return guitaristRepository.findAllByNationality(nationality);
    }

    //UPDATE (PUT & PATCH) METHODS
    @PutMapping("update")
    @ResponseStatus(HttpStatus.OK)
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
    }


    //NEED PATCH METHODS HERE

    private GuitaristRestModel mapPersistenceModelToRestModel(Guitarist guitarist) {
        GuitaristRestModel guitaristRestModel = new GuitaristRestModel();
        guitaristRestModel.setId(guitarist.getId());
        guitaristRestModel.setFirstname(guitarist.getFirstName());
        guitaristRestModel.setLastname(guitarist.getLastName());
        guitaristRestModel.setNationality(guitarist.getNationality());
        return guitaristRestModel;
    }

    private void mapRestModelToPersistenceModel(GuitaristRestModel guitaristRestModel, Guitarist guitarist){
        guitarist.setFirstName(guitaristRestModel.getFirstname());
        guitarist.setLastName(guitaristRestModel.getLastname());
        guitarist.setNationality(guitaristRestModel.getNationality());
        guitarist.setId(guitaristRestModel.getId());
    }
    //https://nullbeans.com/using-put-vs-patch-when-building-a-rest-api-in-spring/#How_to_Configure_HTTP_PATCH_in_a_REST_controller_in_Spring
    /*@PatchMapping("/patch/firstname/{id}")
    public ResponseEntity patchFirstName(@PathVariable int id, @RequestBody Map<String, Object> changes){
        Guitarist guitarist = guitaristRepository.findById(id).get();
        GuitaristRestModel guitaristRestModel = mapPersistenceModelToRestModel(guitarist);
        changes.forEach(
                (change, value) -> {
                    switch (change){
                        case "firstname": guitaristRestModel.setFirstname((String) value); break;
                        case "lastname": guitaristRestModel.setLastname((String) value); break;
                        case "nationality": guitaristRestModel.setNationality((String) value); break;
                    }
                }
        );
    }

    @PatchMapping("/patch/lastname/{id}")
    public ResponseEntity<?> patchLastName(){

    }

    @PatchMapping("/patch/nationality/{id}")
    public ResponseEntity<?> patchNationality(){

    }*/

    //DELETE METHODS
    //url for deleting an object. If the ID does not exist, exception is thrown.
    //@PostMapping("/delete")
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteGuitarist(@RequestBody Guitarist guitarist) {
        if(guitaristRepository.existsById(guitarist.getId())) {
            guitaristRepository.delete(guitarist);
            System.out.println("Guitarist deleted.");
            return ResponseEntity.ok("Deleted entry with ID: " + guitarist.getId());
        } else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
