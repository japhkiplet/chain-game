package ai;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import domain.PlayerMove;

public class MoveLookAhead {
	AIMoves movesMade;
	AIBoardChecker moveChecker;
	private int MAX_PATH_RETRIES = 25;
	private int MOVE_CAP = 100;
	private List<Integer> offsetsForDirectedIterator = Arrays.asList(0,1,-1,2,-2,3,-3,4);
	private Random random = new Random();
	MoveFinder moveFinder;
	
	public MoveLookAhead(AIMoves moveTracker,AIBoardChecker helper,
			MoveFinder moveFinder) {
		this.movesMade = moveTracker;
		this.moveChecker = helper;
		this.moveFinder = moveFinder;
	}

	public PlayerMove moveLookahead() {
		PlayerMove aiMove = PlayerMove.nullInstance();
		int bestWeight = 0;
		int curRetries = 0;
		List<PlayerMove> alreadyTried = new ArrayList<PlayerMove>();
		int originalNumMoves = movesMade.getNumMoves();
		do {
			movesMade.addMove(getValidPegTowardsGoal(alreadyTried));
			alreadyTried.add(movesMade.getLastMove());
			int possiblePathWeight = lookAhead(alreadyTried);
			
			int numMovesTried = movesMade.getNumMoves() - originalNumMoves;
			boolean betterMove = isBetterMove(aiMove, bestWeight, possiblePathWeight);
			if (betterMove) {
				bestWeight = possiblePathWeight;
				// Get next best move, not farthest lookahead move
				aiMove = movesMade.getMove(
						movesMade.getNumMoves()-numMovesTried);
				curRetries = 0;
			} else {
				curRetries++;
			}
			removeLookAheadMoves(numMovesTried);
		} while (curRetries < MAX_PATH_RETRIES);
		return aiMove;
	}
	
	private int lookAhead(List<PlayerMove> alreadyTried) {
		int pathWeight = moveChecker.checkPegWeighting(movesMade.getLastMove());
		while (movesMade.getNumMoves() < MOVE_CAP) {
			if (moveChecker.isPegOnGoalEdge(movesMade.getLastMove(),
					moveFinder.getDirection())) {
				break;
			}
			movesMade.addMove(getValidPegTowardsGoal(alreadyTried));
			pathWeight += moveChecker.checkPegWeighting(movesMade.getLastMove());
		}
		return pathWeight;
	}

	private boolean isBetterMove(PlayerMove move, int bestWeight, int pathWeight ) {
		boolean betterMove = false;
		if (move == PlayerMove.nullInstance()) {
			betterMove = true;
		} else if (pathWeight < bestWeight) {
			betterMove = true;
		}
		return betterMove;
	}

	private void removeLookAheadMoves(int numMovesTried) {
		for (int i = 0; i < numMovesTried; i++) {
			movesMade.removeLastMove();
		}
	}

	private PlayerMove getValidPegTowardsGoal(List<PlayerMove> doNotConsider) {
		ListIterator<PlayerMove> movesItr = movesMade.getMovesIterator(
				movesMade.getNumMoves());
		List<Point> connectionOffsets = moveFinder.getConnectionOffsets();
		// Check each peg for potential further moves, starting from last placed
		return findPossibleMove(movesItr, connectionOffsets, doNotConsider);
	}

	private PlayerMove findPossibleMove(ListIterator<PlayerMove> movesItr,
			List<Point> connectionOffsets, List<PlayerMove> doNotConsider) {
		PlayerMove newMove = PlayerMove.nullInstance();
		while (movesItr.hasPrevious()) {
			PlayerMove lastMove = movesItr.previous();
			ListIterator<Integer> offsetItr =
					offsetsForDirectedIterator.listIterator();
			// Check possible moves to make from this peg
			while (offsetItr.hasNext()) {
				int currListOffset = offsetItr.next();
				newMove = getPotentialMoveFromConnectionOffsets(currListOffset,
						connectionOffsets, lastMove);
				
				if (moveChecker.checkMoveIsGood(lastMove, newMove) &&
						!doNotConsider.contains(newMove)) {
					return newMove;
				}
			}
		}
		// If no other good move found, pick a new valid edge peg
		// to restart from
		return moveFinder.getValidPegOnEdge();
	}
	
	private PlayerMove getPotentialMoveFromConnectionOffsets(
			int currListOffset, List<Point> connectionOffsets,
			PlayerMove lastMove) {
		PlayerMove newMove;
		int bestLocationToTry = getBestConnectionOffsetToTry(connectionOffsets);
		bestLocationToTry = currListOffset + bestLocationToTry;
		bestLocationToTry = wrapArrayIndex(
				bestLocationToTry, connectionOffsets.size());
		Point currentOffset = connectionOffsets.get(bestLocationToTry);
		
		int toCheckX = lastMove.getX() + currentOffset.x;
		int toCheckY = lastMove.getY() + currentOffset.y;
		Point toCheck = new Point(toCheckX, toCheckY);
		newMove = new PlayerMove(toCheck, moveFinder.getPlayer());
		return newMove;
	}
	
	int wrapArrayIndex(int index, int size) {
		int wrappedIdx = index % size;
		if (wrappedIdx < 0) {
			wrappedIdx += size;
		}
		return wrappedIdx;
	}

	/**
	 * Finds the first connectionOffset with the highest value pointing
	 * in the direction we are trying to go
	 * @param connectionOffsets 
	 * @return bestIndex
	 */
	private int getBestConnectionOffsetToTry(List<Point> connectionOffsets) {
		int bestIndex = 0;
		int currIdx = 1;
		// Flip a coin!
		if (random.nextInt(2) == 1) {
			Collections.reverse(connectionOffsets);
		}
		switch (moveFinder.getDirection()) {
		case UP:
			for (Point offset : connectionOffsets) {
				if (offset.y < connectionOffsets.get(bestIndex).y) {
					bestIndex = currIdx;
				}
				currIdx++;
			}
			break;
		case DOWN:
			for (Point offset : connectionOffsets) {
				if (offset.y > connectionOffsets.get(bestIndex).y) {
					bestIndex = currIdx;
				}
				currIdx++;
			}
			break;
		case LEFT:
			for (Point offset : connectionOffsets) {
				if (offset.x < connectionOffsets.get(bestIndex).x) {
					bestIndex = currIdx;
				}
				currIdx++;
			}
			break;
		case RIGHT:
			for (Point offset : connectionOffsets) {
				if (offset.x > connectionOffsets.get(bestIndex).x) {
					bestIndex = currIdx;
				}
				currIdx++;
			}
			break;
		}
		return bestIndex;
	}
}
