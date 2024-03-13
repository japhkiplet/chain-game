package network;

import java.util.logging.Logger;

public class NetworkLogger {

	private NetworkLogger() {
	}

	/**
	 * Used to get an instance of the Network Package logger
	 * @return logger reference
	 */
	public static Logger getInstance() {
		return Logger.getLogger("network");
	}
}
