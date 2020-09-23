# Deck of Cards

![alt text](https://img.freepik.com/free-vector/casino-poker-cards-roulette-wheel-banner_91128-250.jpg?size=626&ext=jpg)

## Running
For running test, use next gradle command

`gradle test -Dcucumber.options="-tags @deckOfCards"`

There are preset 3 scenarios : 
  1. Check if card count in deck is correct after drawing X cards from it (give 3 examples)
  2. Create a new deck containing only Aces and validate that player can only get aces from it
  3. Draw 5 specific cards from a bottom of the deck and check that card amount changed and drawn cards are no longer in the deck

You can run each of them separately using tags : 
1. @drawingCards
2. @acesDeck
3. @getCardsFromBottom

or run all scenarios using tag set in gradle command example

## Reporting

report you can see following path

`build/cucumber-html-report.html`