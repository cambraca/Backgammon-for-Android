package com.morphodev.backgammon;

import java.util.TimerTask;

public class BGTimerTask extends TimerTask {
	Game context;

	public BGTimerTask(Game c) {
		context = c;
	}

	@Override
	public void run() {
		context.RunTimer();
	}

}
