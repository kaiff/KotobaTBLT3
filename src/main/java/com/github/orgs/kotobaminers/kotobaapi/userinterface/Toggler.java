package com.github.orgs.kotobaminers.kotobaapi.userinterface;

public interface Toggler {
	default void toggle() {
		setFlag(!getFlag());
	}
	boolean getFlag();
	void  setFlag(boolean flag);
}
