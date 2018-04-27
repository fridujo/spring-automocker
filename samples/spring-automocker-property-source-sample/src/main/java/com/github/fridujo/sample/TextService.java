package com.github.fridujo.sample;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
class TextService {

    private final String literalText;
    private final Optional<String> optionalText;

    TextService(@Value("${text.literal}")
                    String literalText, @Value("${text.optional:}")
                    Optional<String> optionalText) {
        this.literalText = literalText;
        this.optionalText = optionalText;
    }


    public String getLiteralText() {
        return literalText;
    }

    public String getOptionalText() {
        return optionalText.get();
    }
}
