package com.morphodev.backgammon;

import android.content.Context;
import android.graphics.Matrix;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class Die extends ImageView {
	public RelativeLayout container;
	public char pieceType;
	public int pieceSide;
	public int l,t,rot;
	
	public Die(Context context) {
		super(context);
		container = (RelativeLayout) ((Game) context).findViewById(R.id.pieces);
		this.setScaleType(ScaleType.MATRIX);
		this.setMinimumHeight(40);
		this.setMinimumWidth(40);
	}
	
/*	public void removeDie() {
		container.removeView(this);
	}
	*/
	public void addDie() {
        RelativeLayout.LayoutParams a = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        a.setMargins(0,0,0,0);
        this.hide();
		container.addView(this, a);	
	}
	
	public void setStyle(int t, int l, int rot) {
		this.t = t;
		this.l = l;
		this.rot = rot;
		Matrix m = this.getImageMatrix();
		m.setRotate(rot, 12, 12);
//		this.setImageMatrix(m);
        RelativeLayout.LayoutParams a = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		a.setMargins(l,t,0,0);
		this.setLayoutParams(a);
	}
	
	public void hide() {
		this.setVisibility(INVISIBLE);
	}
	
	public void show() {
		this.setVisibility(VISIBLE);
	}
	
	public void src(int type, int number) {
		switch (type) {
		case 0:
			switch (number) {
			case 1: this.setImageResource(R.drawable.d01); break;
			case 2: this.setImageResource(R.drawable.d02); break;
			case 3: this.setImageResource(R.drawable.d03); break;
			case 4: this.setImageResource(R.drawable.d04); break;
			case 5: this.setImageResource(R.drawable.d05); break;
			case 6: this.setImageResource(R.drawable.d06); break;
			}
			break;
		case 1:
			switch (number) {
			case 1: this.setImageResource(R.drawable.d11); break;
			case 2: this.setImageResource(R.drawable.d12); break;
			case 3: this.setImageResource(R.drawable.d13); break;
			case 4: this.setImageResource(R.drawable.d14); break;
			case 5: this.setImageResource(R.drawable.d15); break;
			case 6: this.setImageResource(R.drawable.d16); break;
			}
			break;
		case 2:
			switch (number) {
			case 1: this.setImageResource(R.drawable.d21); break;
			case 2: this.setImageResource(R.drawable.d22); break;
			case 3: this.setImageResource(R.drawable.d23); break;
			case 4: this.setImageResource(R.drawable.d24); break;
			case 5: this.setImageResource(R.drawable.d25); break;
			case 6: this.setImageResource(R.drawable.d26); break;
			}
			break;
		}
	}

	public int getStyleLeft() {
		return l;
	}

	public int getStyleTop() {
		return t;
	}

	public int getStyleRotation() {
		return rot;
	}

}
