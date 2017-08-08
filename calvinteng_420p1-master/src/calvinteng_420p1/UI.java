package calvinteng_420p1;

import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

public class UI {

	private Scanner sc = new Scanner(System.in);

	/**
	 * Displays a UI for the user to generate a random puzzle, enter a puzzle
	 * manually on 3 lines with each number separated by a space, or to exit the
	 * program. The UI checks for invalid inputs which will prompt the user back
	 * to the beginning of the UI.
	 */
	public void start() {
		boolean done = false;
		while (!done) {
			try {
				System.out.println("[1] Generate a random puzzle.");
				System.out.println("[2] Input a puzzle manually.");
				System.out.println("[3] Exit program.");
				int input = sc.nextInt();

				switch (input) {
				case 1:
					int[] randPuzz = randomPuzzle();
					while (!isSolvable(randPuzz))
						randPuzz = randomPuzzle();
					new Puzzle(randPuzz, 1);
					new Puzzle(randPuzz, 2);
					break;
				case 2:
					int[] puzz = new int[9];
					System.out.println("Enter a 3 x 3 puzzle with each number separated by spaces in 3 lines: ");
					sc.nextLine();
					String in = sc.nextLine();
					while (in.length() <= 0)
						in = sc.nextLine();
					String temp[] = in.split(" ");
					for (int i = 0; i < 3; i++)
						puzz[i] = Integer.parseInt(temp[i]);
					in = sc.nextLine();
					temp = in.split(" ");
					for (int i = 0; i < 3; i++)
						puzz[i + 3] = Integer.parseInt(temp[i]);
					in = sc.nextLine();
					temp = in.split(" ");
					for (int i = 0; i < 3; i++)
						puzz[i + 6] = Integer.parseInt(temp[i]);

					if (!isSolvable(puzz)) {
						System.out.println("This puzzle is not solvable through A* search.");
					} else if (isInvalidPuzzle(puzz)) {
						System.out.println("You have entered an invalid puzzle.");
					} else {
						new Puzzle(puzz, 1);
						new Puzzle(puzz, 2);
					}
					break;
				case 3:
					System.out.println("Program exiting . . .");
					done = true;
					break;
				default:
					System.out.println("Invalid input.");
					break;
				}
			} catch (Exception e) {
				System.out.println("Invalid input.");
				sc.next();
			}
		}
		sc.close();

	}

	/**
	 * Function generates a randomly generated 8-puzzle. It uses a Hash Set to
	 * keep track of repeating numbers from 0-8 in the puzzle.
	 * 
	 * @return a randomly generated 8-puzzle
	 */
	public int[] randomPuzzle() {
		Random rand = new Random();
		HashSet<Integer> seenSet = new HashSet<Integer>();
		int puzzle[] = new int[9];
		int counter = 0;
		while (counter < 9) {
			int randomInt = rand.nextInt(9);
			if (!seenSet.contains(randomInt)) {
				puzzle[counter] = randomInt;
				seenSet.add(puzzle[counter]);
				counter++;
			}
		}
		return puzzle;
	}

	/**
	 * Function checks for the total number of inversions within the 8-puzzle.
	 * If there is an odd number of inversions ( when one number is greater than
	 * a number in front of it in the array, +1 ) then the puzzle is not
	 * solvable using A* search.
	 * 
	 * @param puzzle
	 *            is the puzzle that is input or randomly generated
	 * @return false if the 8-puzzle is not solvable, otherwise true
	 */
	public boolean isSolvable(int[] puzzle) {
		int inversions = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = i + 1; j < 9; j++) {
				if (puzzle[j] == 0 || puzzle[i] == 0) {
				} else if (puzzle[i] > puzzle[j])
					inversions++;
			}
		}
		return ((inversions % 2) == 1) ? false : true;
	}

	/**
	 * Function checks to see that if there is an invalid input for the 8-puzzle
	 * from the user.
	 * 
	 * @param puzz
	 *            is the puzzle that is input
	 * @return true if invalid input, false if otherwise
	 */
	public boolean isInvalidPuzzle(int[] puzz) {
		for (int i = 0; i < puzz.length; i++) {
			if (puzz[i] > 8 || puzz[i] < 0)
				return true;
		}
		return false;
	}
}
