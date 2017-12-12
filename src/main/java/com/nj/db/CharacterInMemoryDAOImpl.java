package com.nj.db;


import com.nj.model.Character;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CharacterInMemoryDAOImpl implements CharacterDAO {

    Set<Integer> characterIds = Collections.synchronizedSet(new HashSet<>());

    @Override
    public Set<Integer> getAllCharacterId() {
        return characterIds;
    }

    @Override
    public void addCharacters(List<Character> characterList) {
        characterIds.addAll(characterList.stream().map(c -> c.getId()).collect(Collectors.toSet()));
    }
}
