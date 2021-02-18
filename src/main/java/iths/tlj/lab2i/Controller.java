package iths.tlj.lab2i;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
    @GetMapping("/test")
    public String test() {
        return "Here I am";
    }

    //CREATE METHODS
    //If you make a post request at this address you will create a new Json object in the database.
    @PostMapping("/guitarist")
    @ResponseStatus(HttpStatus.CREATED)     //Gives a 201 CREATED response instead of simply 200 OK.
    public Guitarist create(@RequestBody Guitarist guitarist) {
        if (guitaristRepository.existsById(guitarist.getId())){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        }else
        System.out.println("New guitarist object created");
        return guitaristRepository.save(guitarist);
    }


    //READ METHODS
    //map method to url. The get request at url returns list of all users
    @GetMapping("/guitarists")
    public List<Guitarist> listAll() {
        return guitaristRepository.findAll();
    }

    //get request at url returns single object from id
    @GetMapping("/guitarist/{id}")
    public Guitarist one(@PathVariable int id) {
        if(!guitaristRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }else
        return guitaristRepository.findById(id)
                .orElse(new Guitarist());
    }

    //UPDATE METHODS
    @PutMapping("guitarist")
    public Guitarist update(@RequestBody Guitarist guitarist) {
        if (!guitaristRepository.existsById(guitarist.getId())){
            throw new ResponseStatusException(HttpStatus.NOT_MODIFIED);
        }else
            guitaristRepository.save(guitarist);
        System.out.println("Guitarist has been updated.");
        return guitarist;
    }

    //DELETE METHODS
    //url for deleting an object. If the ID does not exist, a bad request exception is thrown.
    //@PostMapping("/delete")
    @DeleteMapping("/guitarist")
    @ResponseStatus(HttpStatus.OK)
    public String delete(@RequestBody Guitarist guitarist) {
        String result;
        if(guitaristRepository.existsById(guitarist.getId())) {
            result = "Deleted entry with ID " + guitarist.getId();
            guitaristRepository.delete(guitarist);
            System.out.println("Guitarist deleted.");
            return result;
        } else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
}


//original get request by id
    /*@GetMapping("/persons/{id}")    //gives you the chance to write a url with id number
    //public Optional<Person> one(@PathVariable Long id){     //Optional can be either a person object or nothing
    //But better this way... much easier code to throw an exception rather than create a ResponseEntity
    public Person one(@PathVariable Long id){
        var result = personRepository.findById(id);
        //return result.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id " + id + " not found
        // ."));
        // If there is a person here, it comes automatically or else throws exception.
        if(result.isPresent())
            return result.get();
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id " + id + " not found.");
    }*/
