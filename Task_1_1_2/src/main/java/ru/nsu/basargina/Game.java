package ru.nsu.basargina;

import java.util.Scanner;
import ru.nsu.basargina.deck.Deck;
import ru.nsu.basargina.players.Dealer;
import ru.nsu.basargina.players.Human;

/**
 * Class where game logic is described.
 */
public class Game {
    Scanner inp = new Scanner(System.in);

    Dealer dealer;
    Human human;
    Deck deck;
    Deck droppedDeck; // a deck where used cards go

    int winsCnt;
    int losesCnt;
    private int tiesCnt;
    private int roundCnt;
    private final int blJackScore = 21;
    private int dealerHandSum;
    private int humanHandSum;

    /**
     *  Start new game.
     */
    public Game() {
        winsCnt = 0;
        losesCnt = 0;
        tiesCnt = 0;
        roundCnt = 0;

        dealer = new Dealer();
        human = new Human(inp);

        this.deck = new Deck(true);
        this.droppedDeck = new Deck(false);
    }

    /**
     * Prints score in addition to "you won/lost ...".
     */
    public void printScore() {
        System.out.print("Score " + winsCnt + ":" + losesCnt);

        if (winsCnt > losesCnt) {
            System.out.println(" in your favor.");
        } else if (winsCnt < losesCnt) {
            System.out.println(" in dealer's favor.");
        } else {
            System.out.println(" is equal.");
        }
    }

    /**
     * Checks if players have blackjack.
     */
    public void hasBlackJack() {
        if (dealer.hasBlackjack()) {

            if (human.hasBlackjack()) {
                System.out.print("Both have 21. Tie! ");
                printScore();
                tiesCnt++;
                startRound();
            } else {
                System.out.print("You lost the round! ");
                printScore();
                losesCnt++;
                startRound();
            }
        }

        if (human.hasBlackjack()) {
            System.out.print("You won the round! ");
            printScore();
            winsCnt++;
            startRound();
        }
    }

    /**
     * Checks who won in the end of the round.
     */
    public void whoWonInTheRound() {

        dealerHandSum = dealer.getPlayerHand().countHandSum();
        humanHandSum = human.getPlayerHand().countHandSum();

        if (dealerHandSum > blJackScore) {
            System.out.print("Dealer lost the round! ");
            winsCnt++;
        } else if (dealerHandSum > humanHandSum) {
            System.out.print("You lost the round! ");
            losesCnt++;
        } else if (humanHandSum > dealerHandSum) {
            System.out.print("You won the round! ");
            winsCnt++;
        } else {
            System.out.print("Both have 21. Tie! ");
            tiesCnt++;
        }

        printScore();

        startRound();
    }

    /**
     * Drops player's hands to droppedDeck in the beginning of each round.
     */
    public void dropPlayersHands() {
        if (winsCnt > 0 || losesCnt > 0 || tiesCnt > 0) {

            dealer.getPlayerHand().dropHand(droppedDeck);
            human.getPlayerHand().dropHand(droppedDeck);
        }

        if (deck.cardsLeftCnt() < 4) { // check if at least 4 cards left
            deck.remakeDeck(droppedDeck);
        }
    }

    /**
     * Describes round logic.
     */
    public void startRound() {
        int move; // Checks if you want to continue playing
        System.out.println("Input: 2 to continue or enter: 3 to stop");
        move = inp.nextInt();
        if (move == 2) {
            dropPlayersHands();
            dealer.setDealerClosedCard(true);

            roundCnt++;
            System.out.println("Round " + roundCnt);
            System.out.println("Dealer dealt the cards");

            dealer.getPlayerHand().takeCard(deck);
            dealer.getPlayerHand().takeCard(deck);

            human.getPlayerHand().takeCard(deck);
            human.getPlayerHand().takeCard(deck);

            human.printHumanHand();
            dealer.printDealerHand(dealer.ifHasClosed());

            hasBlackJack();

            System.out.println("Your's turn\n" + "-------");
            human.makeMove(deck, droppedDeck, dealer);

            // if human gets more than 21 in total after all moves
            if (human.getPlayerHand().countHandSum() > blJackScore) {
                System.out.println("You lost the round! ");
                printScore();

                losesCnt++;

                startRound();
            }

            System.out.println("Dealer's turn\n" + "-------");
            while (dealer.getPlayerHand().countHandSum() < 17) {

                dealer.take(deck, droppedDeck);

                human.printHumanHand();
                dealer.printDealerHand(dealer.ifHasClosed());
            }

            whoWonInTheRound();
        } else if (move == 3) {
            return;
        }
    }
}
