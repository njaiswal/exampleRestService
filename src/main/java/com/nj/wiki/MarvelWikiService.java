package com.nj.wiki;

import java.io.IOException;
import java.util.Optional;

public interface MarvelWikiService {
    CharacterPowers getPowers(Optional<String> url) throws IOException;

}