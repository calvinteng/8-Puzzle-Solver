package calvinteng_420p1;

import java.util.ArrayList;

public class State {
	private int[] tiles = new int[9];
	private int f, g = 0, h1 = 0, h2 = 0;
	private State parent;
	private ArrayList<State> possibleStates = new ArrayList<State>();

	public State(int[] tiles) {
		this.tiles = tiles;
		parent = null;
	}

	protected int[] getTiles() {
		return tiles;
	}

	protected int getG() {
		return g;
	}

	protected int getH1() {
		return h1;
	}

	protected int getH2() {
		return h2;
	}

	protected int getF() {
		return f;
	}

	protected State getParent() {
		return parent;
	}

	protected ArrayList<State> getPossibleStates() {
		return possibleStates;
	}

	protected void setF(int f) {
		this.f = f;
	}

	protected void setParent(State p) {
		parent = p;
	}

	/**
	 * Method creates possible next states of the current state in the
	 * possibleStates Array List.
	 */
	public void findPossibleStates() {
		possibleStates.clear();
		int[] temp = new int[9];
		for (int i = 0; i < 9; i++)
			temp[i] = tiles[i];
		for (int i = 0; i < 9; i++) {
			if (temp[i] == 0) {
				switch (i) {
				case 0:
					includeState(0, 1);
					includeState(0, 3);
					break;
				case 1:
					includeState(0, 1);
					includeState(1, 2);
					includeState(1, 4);
					break;
				case 2:
					includeState(1, 2);
					includeState(2, 5);
					break;
				case 3:
					includeState(0, 3);
					includeState(3, 4);
					includeState(3, 6);
					break;
				case 4:
					includeState(1, 4);
					includeState(3, 4);
					includeState(4, 5);
					includeState(4, 7);
					break;
				case 5:
					includeState(2, 5);
					includeState(4, 5);
					includeState(5, 8);
					break;
				case 6:
					includeState(3, 6);
					includeState(6, 7);
					break;
				case 7:
					includeState(7, 8);
					includeState(4, 7);
					includeState(6, 7);
					break;
				case 8:
					includeState(7, 8);
					includeState(5, 8);
					break;
				}
			}
		}
	}

	/**
	 * Method creates and adds successors to current state which is used in
	 * findPossibleStates method.
	 * 
	 * @param x
	 *            used to swap with y
	 * @param y
	 *            used to swap with x
	 */
	public void includeState(int x, int y) {
		int[] temp = new int[9];
		for (int i = 0; i < 9; i++)
			temp[i] = tiles[i];
		int z = temp[y];
		temp[y] = temp[x];
		temp[x] = z;
		possibleStates.add(new State(temp));
	}

	/**
	 * Calculates the g(n) function which is also the depth of the node. It
	 * backtracks to the node's parent until it reaches the root node.
	 */
	public void calculateG() {
		int depth = 0;
		State temp1 = this;
		State temp2 = this;
		while (temp2.parent != null) {
			temp2 = temp2.parent;
			depth++;
		}
		temp1.g = depth;
	}

	/**
	 * Heuristic 1 calculates all misplaced tiles besides the empty tile, 0.
	 */
	public void calculateH1() {
		h1 = 0;
		for (int i = 0; i < 9; i++) {
			if (tiles[i] == 0) {
			} else if (tiles[i] != i) {
				h1++;
			}
		}
	}

	/**
	 * Heuristic 2 calculates the total sum of distances from the tiles' goal
	 * positions, besides the empty tile's.
	 */
	public void calculateH2() {
		h2 = 0;
		for (int i = 0; i < 9; i++) {
			if (tiles[i] == 0) {
			} else if (tiles[i] != i) {
				h2 += distanceToGoal(i, tiles[i]);
			}
		}
	}

	/**
	 * The function calculates the distance by finding the column and row of the
	 * start and goal position excluding the empty tile.
	 * 
	 * @param start
	 *            is the start tile position on puzzle
	 * @param goal
	 *            is the goal tile position on puzzle
	 * @return the distance of spaces the tile needs to move to be at the goal
	 */
	public int distanceToGoal(int start, int goal) {
		int startRow = 0, startCol = 0, goalRow = 0, goalCol = 0;
		int colsAway, rowsAway, distance = 0;
		if (start == 0 || start == 3 || start == 6)
			startCol = 1;
		else if (start == 1 || start == 4 || start == 7)
			startCol = 2;
		else
			startCol = 3;

		if (goal == 0 || goal == 3 || goal == 6)
			goalCol = 1;
		else if (goal == 1 || goal == 4 || goal == 7)
			goalCol = 2;
		else
			goalCol = 3;

		if (start == 0 || start == 1 || start == 2)
			startRow = 1;
		else if (start == 3 || start == 4 || start == 5)
			startRow = 2;
		else
			startRow = 3;

		if (goal == 0 || goal == 1 || goal == 2)
			goalRow = 1;
		else if (goal == 3 || goal == 4 || goal == 5)
			goalRow = 2;
		else
			goalRow = 3;
		colsAway = Math.abs(goalCol - startCol);
		rowsAway = Math.abs(goalRow - startRow);
		switch (colsAway) {
		case 0:
			switch (rowsAway) {
			case 0:
				distance = 0;
				break;
			case 1:
				distance = 1;
				break;
			case 2:
				distance = 2;
				break;
			}
			break;
		case 1:
			switch (rowsAway) {
			case 0:
				distance = 1;
				break;
			case 1:
				distance = 2;
				break;
			case 2:
				distance = 3;
				break;
			}
			break;
		case 2:
			switch (rowsAway) {
			case 0:
				distance = 2;
				break;
			case 1:
				distance = 3;
				break;
			case 2:
				distance = 4;
				break;
			}
			break;
		}
		return distance;
	}

	@Override
	public String toString() {
		String str = "";
		for (int i = 0; i < 9; i++) {
			str += tiles[i] + " ";
			if (i == 2 || i == 5)
				str += "\n";
		}
		return str + "\n";
	}
}
