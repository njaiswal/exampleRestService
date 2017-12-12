package com.nj.rest;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nj.exceptions.MarvelRestException;
import com.nj.model.Character;
import com.nj.model.Response;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarvelRestClient {

    private static Logger log = LoggerFactory.getLogger(MarvelRestClient.class);
    private static final String MARVEL_BASE_URL = "https://gateway.marvel.com";
    private static final String CHARACTERS_RESOURCE = "/v1/public/characters";

    private Client client = ClientBuilder.newClient();
    private ObjectMapper mapper = new ObjectMapper();

    @Value("${MARVEL_PRIVATE_KEY}")
    String MARVEL_PRIVATE_KEY;

    @Value("${MARVEL_PUBLIC_KEY}")
    String MARVEL_PUBLIC_KEY;

    public int getNumberOfCharacters() throws MarvelRestException {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("limit", "1");

        Response<Character> responseObject = get(CHARACTERS_RESOURCE, Character.class, queryParams);
        return responseObject.getData().getTotal();
    }

    public List<Character> getCharacters(int offSet, int limit) throws MarvelRestException {
        List<Character> results = new ArrayList<>();

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("limit", String.valueOf(limit));
        queryParams.put("offset", String.valueOf(offSet));

        Response<Character> responseObject = get(CHARACTERS_RESOURCE, Character.class, queryParams);
        results.addAll(responseObject.getData().getResults());

        return results;
    }

    public Character getCharacter(int id) throws MarvelRestException {
        try {
            Response<Character> responseObject = get(CHARACTERS_RESOURCE + "/" + id, Character.class, null);
            return responseObject.getData().getResults().get(0);
        } catch (Exception exception) {
            log.error("Exception for {} : {}", CHARACTERS_RESOURCE + "/" + id, exception.getMessage());
            throw exception;
        }
    }

    private <T> Response<T> get(String resource, Class<T> clazz, Map<String, String> queryParams) throws MarvelRestException {
        WebTarget target = client.target(MARVEL_BASE_URL);

        for(Map.Entry<String, String> entry : getQueryParameter(queryParams).entrySet()) {
            target = target.queryParam(entry.getKey(), entry.getValue());
        }

        Map response = target.path(resource).request(MediaType.APPLICATION_JSON).get(Map.class);
        JavaType javaType = mapper.getTypeFactory().constructParametricType(Response.class, clazz);
        Response<T> responseObject = mapper.convertValue(response, javaType);

        if(responseObject.getCode() != 200){
            throw new MarvelRestException(response.toString());
        }

        return responseObject;
    }


    private Map<String, String> getQueryParameter(Map<String, String> queryParams) {
        String ts = String.valueOf(Instant.now().toEpochMilli());
        String hash = DigestUtils.md5Hex(ts + MARVEL_PRIVATE_KEY + MARVEL_PUBLIC_KEY);

        Map<String, String> params = new HashMap<>();
        params.put("ts", ts);
        params.put("apikey", MARVEL_PUBLIC_KEY);
        params.put("hash", hash);

        if(queryParams != null && !queryParams.isEmpty()) {
            params.putAll(queryParams);
        }

        return params;
    }
}
