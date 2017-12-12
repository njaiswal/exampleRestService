package com.nj.wiki;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

public class MarvelWikiServiceImpl implements MarvelWikiService {
    private static Logger log = LoggerFactory.getLogger(MarvelWikiServiceImpl.class);

    @Override
    public CharacterPowers getPowers(Optional<String> url) throws IOException {

        CharacterPowers characterPowers = new CharacterPowers();

        if(url.isPresent()) {
            try {
                Document doc = Jsoup.connect(url.get()).get();
                if (doc.getElementById("char-powers-content") != null) {
                    characterPowers.setPowers(doc.getElementById("char-powers-content").text());
                }
            } catch (HttpStatusException exception) {
                log.error("Error fetching url: {},  {}:{}", url.get(), exception.getStatusCode(), exception.getMessage());
                characterPowers.setCode(exception.getStatusCode());
                characterPowers.setStatus(exception.getMessage());
            }
        }

        return characterPowers;
    }
}