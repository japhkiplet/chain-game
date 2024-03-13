/**
 * 
 */
package cmpt373.groupd.model;

import java.awt.Point;
import java.io.Serializable;

/**
 * @author Ishan Bhutani
 *
 */
public class Move implements Serializable {

	private MoveType moveType;
	private Point data;

	/**
	 * SerialVersionUID, the other group must include this exact id
	 */
	private static final long serialVersionUID = -3471394590539340537L;

	/**
	 * 
	 * @param moveType
	 */
	public Move(MoveType moveType) {
		this.moveType = moveType;
		this.data = new Point(0, 0);
	}

	/**
	 * Move constructor
	 * 
	 * @param moveType
	 * @param data
	 *            Coordinates of peg to be removed/added, or the size of the
	 *            game board where coordinates.X == number of columns and
	 *            coodinates.Y == number of rows
	 */
	public Move(MoveType moveType, Point data) {
		this(moveType);
		this.data = data;
	}

	/**
	 * @return the move type
	 */
	public MoveType getMoveType() {
		return moveType;
	}

	/**
	 * @return the data
	 */
	public Point getData() {
		return data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Move [moveType=" + moveType + ", coordinates=" + data + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result
				+ ((moveType == null) ? 0 : moveType.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Move other = (Move) obj;
		if (moveType != other.moveType)
			return false;

		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		return true;
	}
}
