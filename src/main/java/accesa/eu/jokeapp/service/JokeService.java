package accesa.eu.jokeapp.service;

import accesa.eu.jokeapp.model.Joke;
import accesa.eu.jokeapp.model.JokeBook;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class JokeService {

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


}
