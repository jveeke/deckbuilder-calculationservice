package com.deckbuilder.httpfunctions;

import com.deckbuilder.models.JsonCard;
import com.deckbuilder.payloads.CalculateDeckRequest;
import com.google.gson.Gson;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import java.util.List;
import java.util.Optional;

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

        //Fetch the request body.
        Optional<String> requestBody = request.getBody();
        if(requestBody.isPresent()) {
            CalculateDeckRequest requestData = new Gson().fromJson(requestBody.get(), CalculateDeckRequest.class);
            System.out.println("===========================");
            System.out.println("// request data cardlist:");
            System.out.println("===========================");
            List<JsonCard> cardList = requestData.getCardList();
            System.out.println(cardList);

            Float averageManaCost = this.calculateAverageManaCost(cardList);
            System.out.println("Average mana cost: " + averageManaCost.toString());
        } else {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Invalid request data.").build();
        }

        return request.createResponseBuilder(HttpStatus.OK).body("Success.").build();
    }


    private Float calculateAverageManaCost(List<JsonCard> cards) {
        Integer numberOfCards = cards.size();
        Float totalManaCost = Float.valueOf("0.0");

        for(JsonCard card : cards) {
            System.out.println("===========================");
            System.out.println("// new card: " + card.getName());
            System.out.println("===========================");


            System.out.println(card.getConvertedManaCost());
            Float manaCost = Float.parseFloat(card.getConvertedManaCost());
            System.out.println("// mana cost: " + card.getConvertedManaCost());
            totalManaCost = totalManaCost + manaCost;
            System.out.println("// total mana cost: " + totalManaCost.toString());
        }

        System.out.println("***===========================");
        Float averageManaCost = totalManaCost / numberOfCards;
        System.out.println(averageManaCost.toString());
        System.out.println("***===========================");
        return averageManaCost;
    }
}
