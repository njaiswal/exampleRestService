package com.nj.processors;


import com.nj.db.CharacterDAO;
import com.nj.model.Character;
import com.nj.rest.MarvelRestClient;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class DBInitializerProcessor implements Processor{

    private static Logger log = LoggerFactory.getLogger(DBInitializerProcessor.class);

    @Autowired
    CharacterDAO characterDAO;

    @Autowired
    MarvelRestClient marvelRestClient;

    @Override
    public void process(Exchange exchange) throws Exception {
        log.info("DB initializer Processor called");

        int count = marvelRestClient.getNumberOfCharacters();
        log.info("{} total number of characters", count);

        int offset = 0;
        int limit = 100;
        while(offset + limit <= count){
            List<Character> characterList = marvelRestClient.getCharacters(offset, limit);
            log.info("Fetched {} characters from {} to {}", characterList.size(), offset, offset + limit);
            characterDAO.addCharacters(characterList);
            offset = offset + limit;
        }
    }
}