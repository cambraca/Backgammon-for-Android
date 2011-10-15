package com.morphodev.backgammon;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class BoardArea extends View {
	Game context;
	public RelativeLayout container;

	public BoardArea(Context context) {
		super(context);
		this.context = (Game) context;
		container = (RelativeLayout) ((Game) context).findViewById(R.id.pieces);
		RelativeLayout.LayoutParams a = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		container.addView(this, a);
		
/*		this.setClickable(true);
		this.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((BoardArea) v).context.debug2("click!");
			}
		});*/
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			context.pointDragging(event.getX(), event.getY());
			return true;
		}
		
		if (between(event.getX(), 10, 10 + 140)) {
			//left side
			if      (between(event.getY(), 10 + 0*25, 10 + 1*25))
				touchEvent2(event.getAction(), false, 12);
			else if (between(event.getY(), 10 + 1*25, 10 + 2*25))
				touchEvent2(event.getAction(), false, 11);
			else if (between(event.getY(), 10 + 2*25, 10 + 3*25))
				touchEvent2(event.getAction(), false, 10);
			else if (between(event.getY(), 10 + 3*25, 10 + 4*25))
				touchEvent2(event.getAction(), false, 9);
			else if (between(event.getY(), 10 + 4*25, 10 + 5*25))
				touchEvent2(event.getAction(), false, 8);
			else if (between(event.getY(), 10 + 5*25, 10 + 6*25))
				touchEvent2(event.getAction(), false, 7);
			else if (between(event.getY(), 10 + 6*25, 10 + 190 + 0*25))
				touchEvent2(event.getAction(), true, 0);
			else if (between(event.getY(), 10 + 190 + 0*25, 10 + 190 + 1*25))
				touchEvent2(event.getAction(), false, 6);
			else if (between(event.getY(), 10 + 190 + 1*25, 10 + 190 + 2*25))
				touchEvent2(event.getAction(), false, 5);
			else if (between(event.getY(), 10 + 190 + 2*25, 10 + 190 + 3*25))
				touchEvent2(event.getAction(), false, 4);
			else if (between(event.getY(), 10 + 190 + 3*25, 10 + 190 + 4*25))
				touchEvent2(event.getAction(), false, 3);
			else if (between(event.getY(), 10 + 190 + 4*25, 10 + 190 + 5*25))
				touchEvent2(event.getAction(), false, 2);
			else if (between(event.getY(), 10 + 190 + 5*25, 10 + 190 + 6*25))
				touchEvent2(event.getAction(), false, 1);
			else if (between(event.getY(), 10 + 350, 10 + 350 + 25))
				touchEvent2(event.getAction(), false, 0);
		} else if (event.getX() >= (10 + 140) && event.getX() < (10 + 280)) {
			//right side
			if      (between(event.getY(), 10 + 0*25, 10 + 1*25))
				touchEvent2(event.getAction(), false, 13);
			else if (between(event.getY(), 10 + 1*25, 10 + 2*25))
				touchEvent2(event.getAction(), false, 14);
			else if (between(event.getY(), 10 + 2*25, 10 + 3*25))
				touchEvent2(event.getAction(), false, 15);
			else if (between(event.getY(), 10 + 3*25, 10 + 4*25))
				touchEvent2(event.getAction(), false, 16);
			else if (between(event.getY(), 10 + 4*25, 10 + 5*25))
				touchEvent2(event.getAction(), false, 17);
			else if (between(event.getY(), 10 + 5*25, 10 + 6*25))
				touchEvent2(event.getAction(), false, 18);
			else if (between(event.getY(), 10 + 6*25, 10 + 190 + 0*25))
				touchEvent2(event.getAction(), true, 1);
			else if (between(event.getY(), 10 + 190 + 0*25, 10 + 190 + 1*25))
				touchEvent2(event.getAction(), false, 19);
			else if (between(event.getY(), 10 + 190 + 1*25, 10 + 190 + 2*25))
				touchEvent2(event.getAction(), false, 20);
			else if (between(event.getY(), 10 + 190 + 2*25, 10 + 190 + 3*25))
				touchEvent2(event.getAction(), false, 21);
			else if (between(event.getY(), 10 + 190 + 3*25, 10 + 190 + 4*25))
				touchEvent2(event.getAction(), false, 22);
			else if (between(event.getY(), 10 + 190 + 4*25, 10 + 190 + 5*25))
				touchEvent2(event.getAction(), false, 23);
			else if (between(event.getY(), 10 + 190 + 5*25, 10 + 190 + 6*25))
				touchEvent2(event.getAction(), false, 24);
			else if (between(event.getY(), 10 + 350, 10 + 350 + 25))
				touchEvent2(event.getAction(), false, 25);
		}
		return true;
		//return super.onTouchEvent(event);
	}

	private void touchEvent2(int action, boolean is_bar, int nn) {
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			context.pointDragStart(is_bar, nn);
			break;
		case MotionEvent.ACTION_UP:
			context.pointDragEnd(is_bar, nn);
			break;
		}		
	}

	private boolean between(float f, int i, int j) {
		return (f >= i && f < j);
	}
	
	
}
