package com.github.orgs.kotobaminers.kotobaapi.utility;

public enum KotobaPriorityValue {
	HIGH(2),
	MIDIUM(1),
	LOW(0),
	;


	private int priority;


	private KotobaPriorityValue(int priority) {
		this.priority = priority;
	}


	public int getValue() {
		return priority;
	};

}

