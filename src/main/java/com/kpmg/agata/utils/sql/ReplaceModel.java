package com.kpmg.agata.utils.sql;

import com.kpmg.agata.parser.ConfigType;

public class ReplaceModel {
    private final String placeholder;
    private final ConfigType replaceType;

    public ReplaceModel(String placeholder, ConfigType replaceType) {
        this.placeholder = placeholder;
        this.replaceType = replaceType;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public ConfigType getReplaceType() {
        return replaceType;
    }
}
