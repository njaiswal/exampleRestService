package com.nj.shared;

import com.nj.exceptions.MarvelRestException;

import com.nj.model.Character;
import com.nj.rest.MarvelRestClient;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.List;

public class MarvelRestClientTest {

    private static Logger log = LoggerFactory.getLogger(MarvelRestClientTest.class);

    private MarvelRestClient marvelRestClient = new MarvelRestClient();


    @Test(enabled = false)
    public void numberOfCharactersAndLastBatch() throws MarvelRestException {
        int count = marvelRestClient.getNumberOfCharacters();
        log.info("{} total characters", count);
        Assert.assertThat(count, Matchers.greaterThan(0));

        List<Character> characterList = marvelRestClient.getCharacters(count - 5, 10);
        characterList.forEach(c -> log.info("{}", c));
        Assert.assertThat(characterList.size(), Is.is(CoreMatchers.equalTo(5)));
    }
}