@deckOfCards

Feature: Deck of Cards feature
  Description: In this feature should be covered 3 scenarios:
  1. Check if card count in deck is correct after drawing X cards from it (give 3 examples)
  2. Create a new deck containing only Aces and validate that player can only get aces from it
  3. Draw 5 specific cards from a bottom of the deck and check that card amount changed and drawn cards are no longer in the deck

  @drawingCards
  Scenario Outline: Check if card count in deck is correct after drawing X cards from it
    Given  Shuffle the card with deck_count: '<deck_count>'
    And Validate response success status for 'Shuffle' request
    And Get deck_id from response
    When Draw count: <cards_count> of card using deck_id
    And Validate response success status for 'Draw cards from deck' request
    Then Check the cards count after drawing
    And Validate remaining card

    Examples:
      | deck_count | cards_count |
      | 6          | 3           |
      | 22         | 53          |
      | X          | 0           |

  @acesDeck
  Scenario: Create a new deck containing only Aces
    Given Create a new deck containing only Aces: 'AS,AD,AH,AC'
    And Validate response success status for 'New deck creation' request
    And Get deck_id from response
    When Draw cards from deck using deck_id
    And Validate response success status for 'Draw cards from deck' request
    Then Validate response include only Aces
    And Validate there are no more cards left in the deck

  @getCardsFromBottom
  Scenario: Draw 5 specific cards from a bottom of the deck and check that card amount changed and drawn cards are no longer in the deck
    Given Create deck with next cards: '2S,2D,2H,2C'
    And Validate response success status for 'Deck creation' request
    And Get deck_id from response
    When Add cards: 'AS,AD,AH,AC,4S,4D,4H,4C' to piles using deck_id
    And Validate response success status for 'Deck creation' request
    Then Draw five specific cards from a bottom of the pile using deck_id
    And List pile to view all cards, that left in deck
    And Validate response success status for 'List pile' request
    And Check that card amount changed
    And Validate that drawn cards are no longer in the pile
