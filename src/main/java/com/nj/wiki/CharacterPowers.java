package com.nj.wiki;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class CharacterPowers {
    int code;
    String status;
    String language;
    String powers;

    public CharacterPowers() {
        code = 200;
        status = "";
        language = "EN";
        powers = "";
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPowers() {
        return powers;
    }

    public void setPowers(String powers) {
        this.powers = powers;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
