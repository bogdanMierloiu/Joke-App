package accesa.eu.jokeapp.service;

import accesa.eu.jokeapp.model.Joke;
import accesa.eu.jokeapp.model.JokeBook;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JokeService {

    @Async
    public Joke getJoke() {
        try {
            URL url = new URL("https://official-joke-api.appspot.com/random_joke");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder responseBuilder = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    responseBuilder.append(line);
                }

                bufferedReader.close();
                conn.disconnect();

                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(responseBuilder.toString(), Joke.class);
            } else {
                throw new RuntimeException("Failed to fetch joke: HTTP " + conn.getResponseCode());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch joke", e);
        }
    }

    public JokeBook getJokeBook(Long nrOfJokes) {
        JokeBook jokeBook = new JokeBook();
        for (var i = 0; i < nrOfJokes; i++) {
            Joke jokeToAdd = getJoke();
            jokeBook.getJokes().add(jokeToAdd);
        }
        return jokeBook;
    }

    public String getResponseFromGPT(String joke) {
        try {
            String encodedJoke = URLEncoder.encode(joke, StandardCharsets.UTF_8);
            URL url = new URL("https://europe-west4-nomadic-pathway-385517.cloudfunctions.net/internship?question=" + encodedJoke);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder responseBuilder = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    responseBuilder.append(line);
                }

                bufferedReader.close();
                conn.disconnect();
                System.out.println(responseBuilder);

                return responseBuilder.toString();
            } else {
                throw new RuntimeException("Failed to fetch joke: HTTP " + conn.getResponseCode());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch joke", e);
        }
    }

    public JokeBook getJokeBookWithSameCategory(String type, Long nrOfJokes) {
        List<Joke> jokes = new ArrayList<>();
        while (nrOfJokes > 0) {
            Joke joke = getJoke();
            if (joke.getType().equals(type)) {
                jokes.add(joke);
                nrOfJokes--;
            }
        }
        JokeBook jokeBook = new JokeBook();
        jokeBook.getJokes().addAll(jokes);
        return jokeBook;
    }

    public JokeBook getJokeBookWithFirstCategory(Long nrOfJokes) {
        List<Joke> jokes = new ArrayList<>();
        Joke firstJoke = getJoke();
        jokes.add(firstJoke);
        nrOfJokes--;
        String type = firstJoke.getType();
        while (nrOfJokes > 0) {
            Joke joke = getJoke();
            if (joke.getType().equals(type)) {
                jokes.add(joke);
                nrOfJokes--;
            }
        }
        JokeBook jokeBook = new JokeBook();
        jokeBook.getJokes().addAll(jokes);
        return jokeBook;
    }

    public Long getRatingFromResponse(String response) {
        for (var c : response.toCharArray()) {
            if (Character.isDigit(c)) {
                return Long.parseLong(String.valueOf(c));
            }
        }
        return -1L;
    }


}
