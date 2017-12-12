package com.nj.translate;

import com.nj.wiki.CharacterPowers;

public interface TranslateService {
    void translate(CharacterPowers characterPowers, String targetLanguageCode) throws Exception;
}

