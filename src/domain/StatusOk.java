package domain;

public class StatusOk implements Status {

	public StatusOk() {
	}

	@Override
	public boolean passed() {
		return true;
	}

	@Override
	public boolean failed() {
		return false;
	}

}
