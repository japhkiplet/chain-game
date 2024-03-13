package ai;

import java.awt.Point;
import java.util.List;
import java.util.Random;

import domain.Board;
import domain.PlayerID;
import domain.PlayerMove;

public class MoveFinder{
	private Board myBoard;
	private PlayerID myPlayer;
	private Random random = new Random();
	// How 'centered' randomization should be (percent of edge
	// considered for placement: 0.1-1.0)
	private int MAX_LOOP_ITERATIONS = 1000;
	private float RANDOM_EDGE_CONSTRAINT_PERCENT = 0.5f;
	private DirectionOfTravel direction = DirectionOfTravel.DOWN;
	private OwnedEdges ownedEdge = OwnedEdges.VERTICAL;
	
	public MoveFinder(Board newBoard, PlayerID thisPlayer) {
		this.myBoard = newBoard;
		this.setPlayer(thisPlayer);
	}
	
	/**
	 * Sets the playerID for this AI, as well as the associated orientation of play
	 * (PLAYER_A = horizontal, PLAYER_B = vertical)
	 * @param thisPlayer
	 */
	void setPlayer(PlayerID thisPlayer) {
		this.myPlayer = thisPlayer;
		if (thisPlayer == PlayerID.PLAYER_A) {
			ownedEdge = OwnedEdges.HORIZONTAL;
		} else if (thisPlayer == PlayerID.PLAYER_B) {
			ownedEdge = OwnedEdges.VERTICAL;
		}
	}

	public PlayerMove getValidPegOnEdge() {
		PlayerMove newMove;
		int curIter = 0;
		do {
			Point movePoint = randomPointOnOwnedEdges();
			newMove = new PlayerMove(movePoint, myPlayer);
			curIter++;
		} while (!myBoard.isValidMove(newMove) && curIter < MAX_LOOP_ITERATIONS);
		if (!myBoard.isValidMove(newMove)) {
			newMove = getRandomValidPeg();
		}
		return newMove;
	}
	
	private PlayerMove getRandomValidPeg() {
		PlayerMove newMove;
		int curIter = 0;
		do {
			Point movePoint = randomPoint();
			newMove = new PlayerMove(movePoint, myPlayer);
			curIter++;
		} while (!myBoard.isValidMove(newMove)  && curIter < MAX_LOOP_ITERATIONS);
		if (!myBoard.isValidMove(newMove)) {
			newMove = PlayerMove.nullInstance();
		}
		return newMove;
	}

	private Point randomPoint() {
		int moveX = random.nextInt(myBoard.getNumColumns());
		int moveY = random.nextInt(myBoard.getNumRows());
		return new Point(moveX, moveY);
	}

	private Point randomPointOnOwnedEdges() {
		Point movePoint = getPointOnEdgeByConstraint();
		direction = DirectionOfTravel.getDirectionToOppositeEdge(
				movePoint, myBoard.getNumRows(), myBoard.getNumColumns());
		return movePoint;
	}
	
	private Point getPointOnEdgeByConstraint() {
		int moveX;
		int moveY;
		switch (ownedEdge) {
		case VERTICAL:
			moveX = getLocationWithinConstrainedArea(myBoard.getNumColumns());
			moveY = random.nextInt(2);
			moveY = moveY * (myBoard.getNumRows()-1);
			break;
		case HORIZONTAL:
			moveX = random.nextInt(2);
			moveX = moveX * (myBoard.getNumColumns()-1);
			moveY = getLocationWithinConstrainedArea(myBoard.getNumRows());
			break;
		default:
			moveX = 1;
			moveY = 1;
			break;
		}
		Point movePoint = new Point(moveX, moveY);
		return movePoint;
	}

	private int getLocationWithinConstrainedArea(int maxArea) {
		int constraint = getConstrainedValByMax(maxArea);
		int constrainedVal = random.nextInt(maxArea-(constraint*2))
				+constraint;
		return constrainedVal;
	}

	private int getConstrainedValByMax(int maxArea) {
		int constraint;
		constraint = Math.round(
				(maxArea-2)*RANDOM_EDGE_CONSTRAINT_PERCENT);
		constraint = (maxArea - constraint) / 2;
		return constraint;
	}

	public DirectionOfTravel getDirection() {
		return direction;
	}

	public PlayerID getPlayer() {
		return myPlayer;
	}

	public List<Point> getConnectionOffsets() {
		return myBoard.getConnectionOffsets();
	}
}