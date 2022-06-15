package com.deckbuilder.httpfunctions;

import com.deckbuilder.models.JsonCard;
import com.deckbuilder.payloads.requests.CalculateDeckRequest;
import com.deckbuilder.payloads.responses.CalculateDeckResponse;
import com.google.gson.Gson;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.smartcardio.Card;

/**
 * Azure Functions with HTTP Trigger.
 */
public class CalculationHTTPFunction {
    @FunctionName("calculation")
    public HttpResponseMessage run(
            @HttpTrigger(
                name = "calculationrequest",
                methods = {HttpMethod.POST},
                authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        
        //Create the response object
        CalculateDeckResponse response = new CalculateDeckResponse();
            
        //Fetch the request body.
        Optional<String> requestBody = request.getBody();

        //Check if the requestbody is present.
        if(requestBody.isPresent()) {
            //Create the CalculateDeckRequest object, which is used to keep track of data.
            CalculateDeckRequest requestData = new Gson().fromJson(requestBody.get(), CalculateDeckRequest.class);
            //Fetch the cardlist from the requestdata.
            List<JsonCard> cardList = requestData.getCardList();

            //Calculate the average mana cost of the cards in the deck.
            Float averageManaCost = this.calculateAverageManaCost(cardList);
            response.setAverageConvertedManacost(averageManaCost);

            //Calculate the spread of the colors in the deck.
            Map<String, Integer> pipsPerColor = calculateColorSpread(cardList);
            System.out.println(pipsPerColor.values());
        } else {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Invalid request data.").build();
        }

        return request.createResponseBuilder(HttpStatus.OK).body(new Gson().toJson(response)).build();
    }

    //Fetch the average mana cost of the cards in the deck.
    //Having a lot of cards that cost a lot of mana makes a deck slow.
    private Float calculateAverageManaCost(List<JsonCard> cards) {
        Integer numberOfCards = cards.size();
        Float totalManaCost = Float.valueOf("0.0");

        for(JsonCard card : cards) {
            Float manaCost = Float.parseFloat(card.getConvertedManaCost());
            totalManaCost = totalManaCost + manaCost;
        }

        Float averageManaCost = totalManaCost / numberOfCards;
        return averageManaCost;
    }

    //Fetch the amount of times that a pip representing a certain color of mana apears in a list op cards.
    //Each pip represents 1 mana that is needed of a certain color.
    //Having many cards that require a lot of specific mana makes a deck less consistent.
    private Map<String, Integer> calculateColorSpread(List<JsonCard> cards) {
        Map<String, Integer> pipsPerColor = new HashMap<String, Integer>();
        pipsPerColor.put("{R}", 0); //Red
        pipsPerColor.put("{U}", 0); //Blue
        pipsPerColor.put("{B}", 0); //Black
        pipsPerColor.put("{G}", 0); //Green      
        pipsPerColor.put("{W}", 0); //White

        for(Map.Entry<String, Integer> pip : pipsPerColor.entrySet()) {
            Integer amountOfPips = 0;
            for(JsonCard card : cards) {
                List<Integer> indexes = new ArrayList<Integer>();
                int index = 0;
                while(index != -1){
                    index = card.getManaCost().indexOf(pip.getKey(), index);
                    if (index != -1) {
                        indexes.add(index);
                        index++;
                    }
                }
                amountOfPips = amountOfPips + indexes.size();
            }
            pipsPerColor.put(pip.getKey(), amountOfPips);
        }

        return pipsPerColor;
    }
}