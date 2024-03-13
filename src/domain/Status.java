package domain;

public interface Status {

	public default boolean passed() {
		return false;
	};

	public default boolean failed() {
		return true;
	};
}
