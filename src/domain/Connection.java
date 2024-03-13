package domain;

import java.awt.geom.Line2D;

public class Connection {
	private Hole holeA;
	private Hole holeB;
	private PlayerID owningPlayer;

	/**
	 * creates a connection with the two pegs in the argument as the vertices
	 * 
	 * @param holeA
	 * @param holeB
	 */
	public Connection(Hole holeA, Hole holeB, PlayerID owningPlayer) {
		this.holeA = holeA;
		this.holeB = holeB;
		this.owningPlayer = owningPlayer;
	}

	public Hole getHoleA() {
		return holeA;
	}

	public Hole getHoleB() {
		return holeB;
	}

	public PlayerID getOwningPlayer() {
		return owningPlayer;
	}

	public boolean crosses(Connection connection) {
		Line2D line1 = makeLineFromConnection(this);
		Line2D line2 = makeLineFromConnection(connection);

		return line1.intersectsLine(line2);
	}

	private static Line2D makeLineFromConnection(Connection connection) {
		int x1 = connection.holeA.getLocation().x;
		int y1 = connection.holeA.getLocation().y;

		int x2 = connection.holeB.getLocation().x;
		int y2 = connection.holeB.getLocation().y;

		return new Line2D.Float(x1, y1, x2, y2);
	}

	
	public boolean equals(Connection c) {
		if (this.owningPlayer == c.owningPlayer) {
			if ((this.holeA == c.holeA) && (this.holeB == c.holeB)) {
				return true;
			}
			else if ((this.holeA == c.holeB) && (this.holeB == c.holeA)) {
				return true;
			}	
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((holeA == null) ? 0 : holeA.hashCode());
		result = prime * result + ((holeB == null) ? 0 : holeB.hashCode());
		result = prime * result + ((owningPlayer == null) ? 0 : owningPlayer.hashCode());
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
		Connection other = (Connection) obj;
		if (holeA == null) {
			if (other.holeA != null)
				return false;
		} else if (!holeA.equals(other.holeA))
			return false;
		if (holeB == null) {
			if (other.holeB != null)
				return false;
		} else if (!holeB.equals(other.holeB))
			return false;
		if (owningPlayer != other.owningPlayer)
			return false;
		return true;
	}
	
	
	
	
	
}