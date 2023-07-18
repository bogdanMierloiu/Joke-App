package accesa.eu.jokeapp.controller;

import accesa.eu.jokeapp.service.JokeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/joke")
public class JokeController {

    private final JokeService jokeService;


    @GetMapping("/get-joke")
    public ResponseEntity<?> getJoke() {
        try {
            return new ResponseEntity<>(jokeService.getJoke(), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/get-joke-book/{nrOfJokes}")
    public ResponseEntity<?> getJokeBook(@PathVariable("nrOfJokes") Long nrOfJokes) {
        try {
            return new ResponseEntity<>(jokeService.getJokeBook(nrOfJokes), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/get-joke-book/{type}")
    public ResponseEntity<?> getJokeBook(@PathVariable("type") String type) {
        try {
            return new ResponseEntity<>(jokeService.getJokeBookWithSameCategory(type), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }



}
