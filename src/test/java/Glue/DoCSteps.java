package Glue;

import DecOfCardsHelper.DoCAbstractTest;
import DecOfCardsHelper.DoCContext;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class DoCSteps extends DoCAbstractTest {


    private DoCContext context;
    private JsonPath response;

    @Before("@deckOfCards")
    public void setUp(Scenario scenario) {
        logger.info("====================Scenario Starts Here====================");
        logger.info(String.format("Scenario: %s : Line: %d", scenario.getName(), scenario.getLine()));
        context = DoCContext.builder().build();
    }

    @Given("Shuffle the card with deck_count: {string}")
    public void shuffleTheCardWithDeck_countDeck_count(String deck_count) {
        logger.info("Shuffle the card with deck_count value : " + deck_count);
        context.setDeckCount(deck_count);
        // add regex for char and symbol
        if (deck_count.matches("[a-zA-Z]+")) {
            sendGetRequest(properties.getProperty("shuffle.endpoint") + deck_count, 500);
            context.setServerErrorFlag(true);
            context.setSuccessFlag(false);
        } else if (!deck_count.matches("[a-zA-Z]+")) {
            sendGetRequest(properties.getProperty("shuffle.endpoint") + deck_count, 200);
            logger.info("Shuffle response is : " + response.prettyPrint());
            context.setSuccessFlag(response.getBoolean("success"));
            logger.info("Success parameter is : " + context.getSuccessFlag());
        }
    }

    @And("Validate response success status for {string} request")
    public void validateResponseSuccessStatusForShuffleRequest(String action) {
        if (context.getServerErrorFlag().equals(true)) {
            logger.info("Got server error for " + action + " request");
        } else if (context.getServerErrorFlag().equals(false)) {
            logger.info("Success status is " + context.getSuccessFlag() + " for " + action + " request");
        }
    }

    @And("Get deck_id from response")
    public void getDeck_idFromResponse() {
        if (context.getSuccessFlag().equals(true)) {
            context.setDeck_id(response.getString("deck_id"));
        } else if (context.getSuccessFlag().equals(false) && context.getServerErrorFlag().equals(false)) {
            context.setErrorDescription(response.getString("error"));
            logger.info("Success flag is false, due to error : " + context.getErrorDescription());
            context.setDeck_id("new");
        } else if (context.getServerErrorFlag().equals(true)){
            logger.info("Response is empty due to server error");
            context.setDeck_id("new");
        } else {
            logger.info("Unknown error");
        }
    }

    @When("Draw count: {int} of card using deck_id")
    public void drawCountCountOfCardUsingDeck_id(int count) {
        context.setCardsCount(count);
        logger.info("Draw a card with count value : " + count);
        sendGetRequest(context.getDeck_id() + properties.getProperty("draw.endpoint") + count, 200);
        if (context.getDeck_id().equals("new") && count > 52 || context.getDeck_id().matches("1") && count > 52) {
            Assert.assertTrue(response.getString("error").contains("Not enough cards"));
            context.setExpectedRemaining(0);
        } else if (!context.getDeck_id().matches("[a-zA-Z]+")) {
            context.setRemainingCards(response.getInt("remaining"));
            context.setExpectedRemaining((Integer.parseInt(context.getDeckCount()) * 52) - context.getCardsCount());
            logger.info(context.getExpectedRemaining().toString());
        } else if (context.getDeck_id().equals("new") && count == 0) {
            context.setExpectedRemaining(52);
        }
    }

    @Then("Check the cards count after drawing")
    public void checkTheCardsCountAfterDrawing() {
        context.setCardsAfterDrawing(new ArrayList<>(response.getJsonObject("cards")).size());
        logger.info("Response provided " + context.getCardsAfterDrawing() + " cards");
    }

    @And("Validate remaining card")
    public void validateRemainingCard() {
        assertEquals(response.getInt("remaining"), (int) context.getExpectedRemaining());
    }

    @Given("Create a new deck containing only Aces: {string}")
    public void createANewDeckContainingOnlyAcesASADAHAC(String aces) {
        sendGetRequest(properties.getProperty("partial.deck.endpoint") + aces, 200);
        logger.info("Deck with Aces creation response : " + response.prettyPrint());
        context.setSuccessFlag(true);
        context.setServerErrorFlag(false);
    }

    @When("Draw cards from deck using deck_id")
    public void drawCardsFromDeckUsingDeck_id() {
        sendGetRequest(context.getDeck_id() + properties.getProperty("draw.endpoint") + 4, 200);
        logger.info("Deck drawing response : " + response.prettyPrint());
    }

    @Then("Validate response include only Aces")
    public void validateResponseIncludeOnlyAces() {
        JSONArray cards = new JSONObject(response.prettyPrint()).getJSONArray("cards");
        for (int i = 0; i < cards.length(); i++) {
            String value = cards.getJSONObject(i).getString("value");
            String suit = cards.getJSONObject(i).getString("suit");
            Assert.assertTrue(value.equalsIgnoreCase("ACE"));
            logger.info("Cards value is " + value + ", suite is " + suit);
        }
    }

    @And("Validate there are no more cards left in the deck")
    public void validateThereAreNoMoreCardsLeftInTheDeck() {
        Assert.assertTrue(response.getString("remaining").equalsIgnoreCase("0"));
    }

    @Given("Create deck with next cards: {string}")
    public void createDeckWithNextCardsSDHC(String cards) {
        sendGetRequest(properties.getProperty("partial.deck.endpoint") + cards, 200);
        logger.info("Deck with Aces creation response : " + response.prettyPrint());
        context.setSuccessFlag(true);
        context.setServerErrorFlag(false);
    }

    @When("Add cards: {string} to piles using deck_id")
    public void addCardsASADAHACSDHCToPilesUsingDeck_id(String cards) {
        context.setPileName("test");
        sendGetRequest(context.getDeck_id() + properties.getProperty("pile.endpoint")
                + context.getPileName() + properties.getProperty("add.cards.endpoint")
                + cards, 200);
        logger.info("Pile creation response : " + response.prettyPrint());

        // need do set remaining cards using list pile endpoint instead of this
        context.setRemainingCards(cards.split(",").length);
    }

    @Then("Draw five specific cards from a bottom of the pile using deck_id")
    public void drawFiveSpecificCardsFromABottomOfThePileUsingDeck_id() {
        List<String> drawnCards = new ArrayList();
        for (int i = 0; i < 5; i++) {
            sendGetRequest(context.getDeck_id() + properties.getProperty("pile.endpoint")
                    + context.getPileName() + properties.getProperty("draw.bottom.card.endpoint"), 200);
            String cardDrown = new JSONObject(response.prettyPrint()).getJSONArray("cards")
                    .getJSONObject(0).getString("code");
            drawnCards.add(cardDrown);
            logger.info("Drown card: " + cardDrown);
        }
        context.setDrownCards(drawnCards);
    }

    @And("List pile to view all cards, that left in deck")
    public void listPileToViewAllCardsThatLeftInDeck() {
        sendGetRequest(context.getDeck_id() + properties.getProperty("pile.endpoint")
                + context.getPileName() + properties.getProperty("pile.list.endpoint"), 200);
        logger.info("Pile list response : " + response.prettyPrint());
    }

    @And("Check that card amount changed")
    public void checkThatCardAmountChanged() {
        assertEquals(new JSONObject(response.prettyPrint()).getJSONObject("piles")
                        .getJSONObject("test").getInt("remaining"),
                context.getRemainingCards() - context.getDrownCards().size());
    }

    @And("Validate that drawn cards are no longer in the pile")
    public void validateThatDrawnCardsAreNoLongerInThePile() {
        boolean cardsPresent =
                context.getDrownCards()
                        .stream()
                        .anyMatch((s) -> s.contains(response.prettyPrint()));
        logger.info("Down cards present status is: " + cardsPresent);
        Assert.assertFalse(cardsPresent);
    }

    public void sendGetRequest(String endpoint, int expectedStatusCode) {
        response = given()
                .baseUri(baseUrl)
                .when()
                .get(endpoint)
                .then()
                .assertThat()
                .statusCode(expectedStatusCode)
                .extract()
                .jsonPath();
    }
}
