package com.nj.service;

import com.nj.db.CharacterDAO;
import com.nj.exceptions.MarvelRestException;
import com.nj.model.Character;
import com.nj.model.Urls;
import com.nj.rest.MarvelRestClient;
import com.nj.translate.GoogleTranslateService;
import com.nj.wiki.CharacterPowers;
import com.nj.wiki.MarvelWikiServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.*;

@Api
@Produces("application/json")
@Path("/")
public class MarvelService {

    private static Logger log = LoggerFactory.getLogger(MarvelService.class);

    @Autowired
    CharacterDAO characterDAO;

    @Autowired
    MarvelRestClient marvelRestClient;

    @Autowired
    MarvelWikiServiceImpl marvelWikiServiceImpl;

    @Autowired
    GoogleTranslateService googleTranslateService;

    @GET
    @Path("/status")
    public Response status() {
        log.info("Status get called");
        Map<String, Object> response = new HashMap<>();
        response.put("status", "live");
        response.put("characterCount", characterDAO.getAllCharacterId().size());

        return Response.ok(response).build();
    }

    @GET
    @Path("/characters")
    @ApiOperation(
            value = "Get all available characters ids",
            response = Integer[].class
    )
    public Response characters() {
        log.info("/characters get called");
        return Response.ok(characterDAO.getAllCharacterId()).build();
    }

    @GET
    @Path("/characters/{id}")
    @ApiOperation(
            value = "Find character by ID",
            notes = "Returns a single character",
            response = Character.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Character not found")
    })
    public Response characters(@PathParam("id") Integer id) throws MarvelRestException {
        log.info("/characters/{} get called", id);
        Character character = marvelRestClient.getCharacter(id);
        log.info(character.toString());
        return Response.ok(character).build();
    }

    @GET
    @Path("/characters/{id}/powers")
    @ApiOperation(
            value = "Find character's powers by ID",
            response = CharacterPowers.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Character not found"),
            @ApiResponse(code = 400, message = "Google Translation issue")
    })
    public Response charactersPowers(@PathParam("id") Integer id, @DefaultValue("en") @QueryParam("language") String language) throws Exception {
        log.info("/characters/{} get called", id);
        Character character = marvelRestClient.getCharacter(id);

        Optional<String> wikiUrl = character.getUrls().stream().filter(u -> u.getType().equalsIgnoreCase("wiki")).map(Urls::getUrl).findFirst();
        CharacterPowers powers = marvelWikiServiceImpl.getPowers(wikiUrl);
        log.info("{} powers are: {}", character.getName(), powers);

        googleTranslateService.translate(powers, language);
        log.info("{} powers got translated: {}", character.getName(), powers);
        return Response.ok(powers).status(powers.getCode()).build();
    }
}