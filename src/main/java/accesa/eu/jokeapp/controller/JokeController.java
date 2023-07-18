package accesa.eu.jokeapp.controller;

import accesa.eu.jokeapp.model.Joke;
import accesa.eu.jokeapp.model.JokeBook;
import accesa.eu.jokeapp.service.JokeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/get-joke-book-category/{type}/{nrOfJokes}")
    public ResponseEntity<?> getJokeBookWithDefinedCategory(@PathVariable("type") String type, @PathVariable("nrOfJokes") Long nrOfJokes) {
        try {
            return new ResponseEntity<>(jokeService.getJokeBookWithSameCategory(type, nrOfJokes), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/get-joke-book-first-category/{nrOfJokes}")
    public ResponseEntity<?> getJokeBookWithFirstCategory(@PathVariable("nrOfJokes") Long nrOfJokes) {
        try {
            return new ResponseEntity<>(jokeService.getJokeBookWithFirstCategory(nrOfJokes), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/get-joke-book-with-rating/{nrOfJokes}")
    public ResponseEntity<?> getJokeBookWithRating(@PathVariable("nrOfJokes") Long nrOfJokes) {
        try {
            JokeBook jokeBook = jokeService.getJokeBook(nrOfJokes);
            for (var joke : jokeBook.getJokes()) {
                String ratingResponseFromGPT = jokeService.getResponseFromGPT(composeQuestion(joke.getSetup(), joke.getPunchline()));
                joke.setRating(jokeService.getRatingFromResponse(ratingResponseFromGPT));
            }
            return new ResponseEntity<>(jokeBook, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/get-review")
    public ResponseEntity<?> getReview() {
        try {
            JokeBook jokeBook = jokeService.getJokeBook(5L);
            String responseFromGPT = jokeService.getResponseFromGPT(composeBookJokes(jokeBook.getJokes()));
            return new ResponseEntity<>(responseFromGPT, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    private String composeQuestion(String setup, String punchline) {
        return "Acorda un rating (nr intreg) acestei glume, " +
                "pe o scara de la 1 la 5, unde 1 este o gluma slaba si 5 este o gluma foarte buna: "
                + setup + " " + punchline;
    }

    private String composeBookJokes(List<Joke> jokesList) {
        StringBuilder bookToReview = new StringBuilder("Give a very short review to this 5 jokes: ");
        for (var joke : jokesList) {
            bookToReview.append(joke.getSetup()).append(" ").append(joke.getPunchline()).append(",   ");
        }
        return bookToReview.toString();
    }


}
