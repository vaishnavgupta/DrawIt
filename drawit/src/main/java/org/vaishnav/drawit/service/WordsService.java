package org.vaishnav.drawit.service;


import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class WordsService {
    private final List<String> words;

    public WordsService() throws IOException {
        this.words = Files.readAllLines(
                Paths.get("src/main/resources/words.txt")
        );
    }

    public String getRandomWord(){
        return words.get(
                ThreadLocalRandom.current().nextInt(words.size())
        );
    }
}
