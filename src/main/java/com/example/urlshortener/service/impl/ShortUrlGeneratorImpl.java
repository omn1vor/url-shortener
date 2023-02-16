package com.example.urlshortener.service.impl;

import com.example.urlshortener.service.ShortUrlGenerator;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Profile("!dev")
public class ShortUrlGeneratorImpl implements ShortUrlGenerator {
    @Override
    public String generate() {
        final int size = 6;
        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();
        List<Character> dictionary = getDictionary();

        for (int i = 0; i < size; i++) {
            int index = rnd.nextInt(dictionary.size());
            sb.append(dictionary.get(index));
            dictionary.remove(index);
        }

        return sb.toString();
    }

    private List<Character> getDictionary() {
        final List<Character> dictionary = new ArrayList<>('z' - 'a' + 1);

        for (char c = 'a'; c <= 'z'; c++) {
            dictionary.add(c);
        }

        return dictionary;
    }
}
