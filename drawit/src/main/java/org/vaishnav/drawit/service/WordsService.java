package org.vaishnav.drawit.service;


import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class WordsService {
    private List<String> words;

    @PostConstruct
    public void init() {
        try {
            InputStream is = getClass()
                    .getClassLoader()
                    .getResourceAsStream("words.txt");

            if (is == null) {
                throw new RuntimeException("words.txt not found in resources");
            }

            try (BufferedReader reader =
                         new BufferedReader(new InputStreamReader(is))) {
                words = reader.lines().collect(Collectors.toList());
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to load words", e);
        }
    }

    public String getRandomWord() {
        return words.get(
                ThreadLocalRandom.current().nextInt(words.size())
        );
    }
}
