package com.deckbuilder.payloads.responses;

import lombok.*;

@NoArgsConstructor
@Getter @Setter
public class CalculateDeckResponse {
    private String authorName;
    private String deckName;
    private Float averageConvertedManacost;
}
