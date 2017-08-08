package calvinteng_420p1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

public class Puzzle {

	private final String answer = "[0, 1, 2, 3, 4, 5, 6, 7, 8]";
	private State root;
	private Comparator<State> comparator = new FunctionComparator();
	private PriorityQueue<State> frontier = new PriorityQueue<State>(comparator);
	private HashSet<String> exploredSet = new HashSet<String>();

	public Puzzle(int[] puzzle, int h) {
		root = new State(puzzle);
		aStarSolver(h);
	}

	/**
	 * Performs A* Graph-Search using a HashSet as the Explored Set to prevent
	 * visiting previously visited states. The frontier is implemented using a
	 * priority queue ordered based on each state's evaluation function. This
	 * method will output the steps taken to reach the goal, the goal's depth,
	 * the number of nodes generated, and the time taken to run the algorithm.
	 * 
	 * @param h
	 *            is used to tell the function to use heuristic 1 or 2
	 */
	public void aStarSolver(int h) {
		long startTime = System.nanoTime();
		State current = root;
		current.findPossibleStates();
		State prev = null;
		int nodesGenerated = 1;
		if (h == 1)
			System.out.println("Heuristic 1:\n");
		else
			System.out.println("Heuristic 2:\n");
		String stateString = "Initial State:\n" + current.toString() + "\n";

		exploredSet.add(Arrays.toString(current.getTiles()));

		while (!(Arrays.toString(current.getTiles()).equals(answer))) { //ends search when it wants to expand upon a goal node
			if (prev != current) {
				for (int i = 0; i < current.getPossibleStates().size(); i++) {
					current.getPossibleStates().get(i).setParent(current);
					current.getPossibleStates().get(i).calculateG();

					if (h == 1) {
						current.getPossibleStates().get(i).calculateH1();
						current.getPossibleStates().get(i).setF(
								current.getPossibleStates().get(i).getG() + current.getPossibleStates().get(i).getH1());
					} else {
						current.getPossibleStates().get(i).calculateH2();
						current.getPossibleStates().get(i).setF(
								current.getPossibleStates().get(i).getG() + current.getPossibleStates().get(i).getH2());
					}
					if (!exploredSet.contains(Arrays.toString(current.getPossibleStates().get(i).getTiles()))) {
						frontier.add(current.getPossibleStates().get(i));
						nodesGenerated++;
					}
				}
			}
			State temp = frontier.poll();
			if (!exploredSet.contains(Arrays.toString(temp.getTiles()))) {
				prev = current;
				current = temp;
				current.findPossibleStates();
				exploredSet.add(Arrays.toString(current.getTiles()));
			}
		}
		long endTime = System.nanoTime();
		long totalTime = endTime - startTime;
		stateString += backTrackSolution(current, current.getG());
		System.out.println(stateString);
		System.out.println("depth: " + current.getG());
		System.out.println("nodes generated: " + nodesGenerated);
		System.out.println("time: " + totalTime);
		System.out.println("\n-------------------------------------------------------------");
		frontier.clear();
		exploredSet.clear();
	}

	/**
	 * This function creates a string used to show the steps taken using A*
	 * search to find the optimal solution of the 8-puzzle.
	 * 
	 * @param goal
	 *            state is backtracked to its parent until it reaches the root
	 *            node.
	 * @param steps
	 *            is used to keep count and label the states and their steps
	 * @return a string containing the steps of the optimal solution
	 */
	public String backTrackSolution(State goal, int steps) {
		String str = "";
		List<String> list = new ArrayList<String>();
		while (goal.getParent() != null) {
			list.add("Step " + steps + ":\n" + goal.toString() + "\n");
			steps--;
			goal = goal.getParent();
		}
		Collections.reverse(list);
		for (int i = 0; i < list.size(); i++) {
			str += list.get(i);
		}
		return str;
	}

	/**
	 * @author Calvin Teng Class is used to order priority queue frontier in
	 *         order of the evaluation function f.
	 */
	class FunctionComparator implements Comparator<State> {
		@Override
		public int compare(State o1, State o2) {
			return o1.getF() - o2.getF();
		}
	}
}
