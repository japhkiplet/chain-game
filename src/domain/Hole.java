package domain;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import domain.Hole;
import domain.Peg;
import domain.NullPeg;
import domain.PlayerPeg;
import domain.PlayerID;

public class Hole {
	private Peg pegSlot;
	private Point location;
	private Set<Connection> neighbourPegs = new HashSet<Connection>();

	private static Hole offBoardInstance = new OffBoardHole();

	public Hole(Point point) {
		pegSlot = new NullPeg();
		location = point;
	}

	public Hole(int x, int y) {
		this(new Point(x, y));
	}

	// Returns true on success
	public boolean canPlacePeg() {
		return pegSlot.isNull();
	}
	
	public boolean canRemovePeg() {
		return !pegSlot.isNull();
	}

	public void placePeg(PlayerID playerID) {
		assert (canPlacePeg());
		pegSlot = new PlayerPeg(playerID);
	}
	
	public void removePeg() {
		assert(canRemovePeg());
		pegSlot = new NullPeg();
	}

	public boolean hasPeg() {
		return !pegSlot.isNull();
	}

	public Peg getPeg() {
		return pegSlot;
	}

	public Point getLocation() {
		return location;
	}

	public Set<Connection> getNeighbours() {
		return neighbourPegs;
	}

	public static Hole offBoardInstance() {
		return offBoardInstance;
	}

	public static class OffBoardHole extends Hole {
		public OffBoardHole() {
			super(new Point(0, 0));
		}

		@Override
		public boolean canPlacePeg() {
			return false;
		}
		
		@Override
		public boolean canRemovePeg() {
			return false;
		}

		@Override
		public void placePeg(PlayerID playerID) {
			throw new RuntimeException("attempt to place a peg into an OffBoardHole.");
		}

		@Override
		public Peg getPeg() {
			return new NullPeg();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Hole other = (Hole) obj;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (pegSlot == null) {
			if (other.pegSlot != null)
				return false;
		} else if (!pegSlot.equals(other.pegSlot))
			return false;
		return true;
	}
}
