package com.deckbuilder.payloads;
import java.util.List;

import com.deckbuilder.models.JsonCard;

import lombok.*;

@NoArgsConstructor
@Getter @Setter
public class CalculateDeckRequest {
    private String authorName;
    private String deckName;
    private List<JsonCard> cardList;
}
