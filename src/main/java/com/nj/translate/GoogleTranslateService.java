package com.nj.translate;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.translate.Translate;
import com.google.api.services.translate.model.TranslationsListResponse;
import com.google.api.services.translate.model.TranslationsResource;
import com.nj.wiki.CharacterPowers;
import org.springframework.beans.factory.annotation.Value;

public class GoogleTranslateService implements TranslateService {

    @Value("${GOOGLE_API_KEY}")
    String GOOGLE_API_KEY;

    private Translate translate = null;

    public GoogleTranslateService() throws GeneralSecurityException, IOException {
        translate = new Translate.Builder(
                com.google.api.client.googleapis.javanet.GoogleNetHttpTransport.newTrustedTransport()
                , com.google.api.client.json.gson.GsonFactory.getDefaultInstance(), null)
                .setApplicationName("MarvelTranslateService")
                .build();
    }

    @Override
    public void translate(CharacterPowers powers, String targetLanguageCode) throws GeneralSecurityException, IOException {

        if (powers.getPowers() == null) {
            return;
        }

        if (!targetLanguageCode.equalsIgnoreCase("en")) {
            Translate.Translations.List list = translate.new Translations().list(Collections.singletonList(powers.getPowers()), targetLanguageCode.toUpperCase());
            list.setKey(GOOGLE_API_KEY);
            try {
                powers.setLanguage(targetLanguageCode);
                TranslationsListResponse response = list.execute();
                TranslationsResource googleTranslateResponse = response.getTranslations().get(0);
                powers.setPowers(googleTranslateResponse.getTranslatedText());
            } catch (GoogleJsonResponseException exception) {
                powers.setPowers("");
                powers.setCode(exception.getStatusCode());
                powers.setStatus(exception.getDetails().getMessage());
            }
        }
    }
}