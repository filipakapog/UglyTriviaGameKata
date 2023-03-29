package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.List;

public class Game {

	private static final int NR_OF_COINS_TO_END_THE_GAME = 6;

	List<String> players = new ArrayList<>();
	List<Integer> places = new ArrayList<>();
	List<Integer> purses = new ArrayList<>();
	List<Boolean> inPenaltyBox = new ArrayList<>();

	GameQuestions gameQuestions;

	int currentPlayer = 0;
	boolean isGettingOutOfPenaltyBox;

	private Game() {
		gameQuestions = new GameQuestions(50);
	}

	public static Game newGame(List<String> playerNames) {
		Game basicGame = new Game();

		checkIfMinimumNumberOfPlayers(playerNames);
		addPlayersInGame(basicGame, playerNames);
		initGame(basicGame);

		return basicGame;
	}

	private static void checkIfMinimumNumberOfPlayers(List<String> playerNames) {
		if (playerNames.size() < 2) {
			throw new IllegalArgumentException("We need at least two players");
		}
	}

	private static void addPlayersInGame(Game basicGame, List<String> playerNames) {
		for (String playerName : playerNames) {
			throwExceptionIfPlayerNameIsNotUnique(basicGame, playerName);
			basicGame.add(playerName);
		}
	}

	private static void throwExceptionIfPlayerNameIsNotUnique(Game basicGame, String playerName) {
		if (basicGame.players.contains(playerName)) {
			throw new IllegalArgumentException("Player names must be unique");
		}
	}

	private static void initGame(Game game) {
		game.places = new ArrayList<>(howManyPlayers(game));
		game.purses = new ArrayList<>(howManyPlayers(game));
		game.inPenaltyBox = new ArrayList<>(howManyPlayers(game));

		for (int i = 0; i < howManyPlayers(game); i++) {
			game.places.add(0);
			game.purses.add(0);
			game.inPenaltyBox.add(false);
		}
	}

	private static int howManyPlayers(Game game) {
		return game.players.size();
	}

	private void add(String playerName) {
		if (null == playerName || playerName.isBlank()) {
			throw new IllegalArgumentException("Name should not be blank");
		}

	    players.add(playerName);
	    
	    System.out.println(playerName + " was added");
	    System.out.println("They are player number " + players.size());
	}

	public void roll(int roll) {
		System.out.println(players.get(currentPlayer) + " is the current player");
		System.out.println("They have rolled a " + roll);

		if (isCurrentUserInPenaltyBox()) {
			if (roll % 2 != 0) {
				isGettingOutOfPenaltyBox = true;

				System.out.println(players.get(currentPlayer) + " is getting out of the penalty box");
				places.set(currentPlayer, places.get(currentPlayer) + roll);
				if (places.get(currentPlayer) > 11) places.set(currentPlayer, places.get(currentPlayer) - 12);

				System.out.println(players.get(currentPlayer)
						+ "'s new location is "
						+ places.get(currentPlayer));
				System.out.println("The category is " + currentCategory());
				askQuestion();
			} else {
				System.out.println(players.get(currentPlayer) + " is not getting out of the penalty box");
				isGettingOutOfPenaltyBox = false;
				}
			
		} else {

			places.set(currentPlayer, places.get(currentPlayer) + roll);
			if (places.get(currentPlayer) > 11) places.set(currentPlayer, places.get(currentPlayer) - 12);

			System.out.println(players.get(currentPlayer)
					+ "'s new location is "
					+ places.get(currentPlayer));
			System.out.println("The category is " + currentCategory());
			askQuestion();
		}

	}

	private Boolean isCurrentUserInPenaltyBox() {
		return inPenaltyBox.get(currentPlayer);
	}

	private void askQuestion() {
		switch (currentCategory()) {
			case POP -> System.out.println(gameQuestions.drawAPopQuestion());
			case ROCK -> System.out.println(gameQuestions.drawARockQuestion());
			case SPORTS -> System.out.println(gameQuestions.drawASportsQuestion());
			case SCIENCE -> System.out.println(gameQuestions.drawAScienceQuestion());
		}
	}

	private Category currentCategory() {
		return switch (places.get(currentPlayer)) {
			case 0, 4, 8 -> Category.POP;
			case 1, 5, 9 -> Category.SCIENCE;
			case 2, 6, 10 -> Category.SPORTS;
			default -> Category.ROCK;
		};
	}

	public boolean wasCorrectlyAnswered() {
		if (isCurrentUserInPenaltyBox()) {
			if (isGettingOutOfPenaltyBox) {
				System.out.println("Answer was correct!!!!");
				purses.set(currentPlayer, purses.get(currentPlayer) + 1);
				System.out.println(players.get(currentPlayer)
						+ " now has "
						+ purses.get(currentPlayer)
						+ " Gold Coins.");

				boolean winner = didPlayerWin();
				currentPlayer++;
				if (currentPlayer == players.size()) currentPlayer = 0;
				
				return winner;
			} else {
				currentPlayer++;
				if (currentPlayer == players.size()) currentPlayer = 0;
				return true;
			}
			
			
			
		} else {

			System.out.println("Answer was corrent!!!!");
			purses.set(currentPlayer, purses.get(currentPlayer) + 1);
			System.out.println(players.get(currentPlayer)
					+ " now has "
					+ purses.get(currentPlayer)
					+ " Gold Coins.");
			
			boolean winner = didPlayerWin();
			currentPlayer++;
			if (currentPlayer == players.size()) currentPlayer = 0;
			
			return winner;
		}
	}
	
	public boolean wrongAnswer(){
		System.out.println("Question was incorrectly answered");
		System.out.println(players.get(currentPlayer) + " was sent to the penalty box");
		inPenaltyBox.set(currentPlayer, true);
		
		currentPlayer++;
		if (currentPlayer == players.size()) currentPlayer = 0;
		return true;
	}


	private boolean didPlayerWin() {
		return !(purses.get(currentPlayer) == NR_OF_COINS_TO_END_THE_GAME);
	}
}
