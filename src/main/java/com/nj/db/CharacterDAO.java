package com.nj.db;

import com.nj.model.Character;

import java.util.List;
import java.util.Set;

public interface CharacterDAO {
    Set<Integer> getAllCharacterId();
    void addCharacters(List<Character> characterList);
}
