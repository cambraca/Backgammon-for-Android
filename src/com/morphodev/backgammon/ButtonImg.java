package com.morphodev.backgammon;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class ButtonImg extends ImageView {
	public RelativeLayout container;
	public Game.Button button;
	Game context;

	public ButtonImg(Context context) {
		super(context);
		this.context = (Game) context;
		container = (RelativeLayout) ((Game) context).findViewById(R.id.buttons);
		hide();
	}
	
	public void addButton(int l, int t, Game.Button button) {
		this.button = button;
		int imgResource = 0;
		switch (button) {
		case ROLL_DICE: imgResource = R.drawable.roll; break;
		case OK: imgResource = R.drawable.ok; break;
		case PASS: imgResource = R.drawable.pass; break;
		case CANCEL: imgResource = R.drawable.cancel; break;
		case TEMP: imgResource = R.drawable.pass; break;
		}
		this.setImageResource(imgResource);
        RelativeLayout.LayoutParams a = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        a.setMargins(l,t,0,0);
		container.addView(this, a);	
	}
	
	public void hide() {
		this.setVisibility(INVISIBLE);
	}
	
	public void show() {
		this.setVisibility(VISIBLE);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		context.debug("onTouchEvent " + button.name());
		switch (button) {
		case ROLL_DICE: context.RollNewClick(true); break;
		case OK: 
			if (context.game_finished) {
		        context.game_finished = false;
		        boolean winnerIsHuman = context.player[context.Score0 > 0 ? 1 : 0] ? true : false;
		        int finalScore = context.Score0 + context.Score1;
		        context.debug("GAME FINISHED, winner is human? " + winnerIsHuman + ", final score" + finalScore);
		        
/*		        if (winnerIsHuman && Game.isHighScore(finalScore)) {
		           //call the high score scene, passing finalScore as the argument
		            Mojo.Controller.stageController.swapScene({name:'newScore'}, finalScore);
		            } else {
		            //return to the main menu
		              Mojo.Controller.stageController.popScene(null);
		            }
*/		    }
		    else {
		        context.debug("about to call okclick");
		        context.OKClick(true);
		        context.debug("called okclick");
		    }
			break;
		case PASS: context.OKClick(true); break;
		case CANCEL: context.CancelClick(true); break;
		case TEMP: context.RunTimer(); break;
		}

		return super.onTouchEvent(event);
	}

}
