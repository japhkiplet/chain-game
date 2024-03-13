package domain;

public class StatusFail implements Status{

	public StatusFail(){	
	}

	@Override
	public boolean passed() {
		return false;
	}

	@Override
	public boolean failed() {
		return true;
	}
}
