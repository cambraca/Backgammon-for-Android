package com.morphodev.backgammon;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class Piece extends ImageView {
	public RelativeLayout container;
	public char pieceType;
	public int pieceSide;
	public int l,t;
	
	public Piece(Context context) {
		super(context);
		container = (RelativeLayout) ((Game) context).findViewById(R.id.pieces);
	}
	
	public void removePiece() {
		container.removeView(this);
	}
	
	public void addPiece(int l, int t) {
		this.l=l;
		this.t=t;
        RelativeLayout.LayoutParams a = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        a.setMargins(l,t,0,0);
		container.addView(this, a);
	}
	
	public void setPieceImage(char t, int s) {
		this.pieceType = t;
		this.pieceSide = s;
		this.setImageResource(
			(
				t == 'o' 
				? (s == 0 ? R.drawable.o0 : R.drawable.o1) 
				: (s == 0 ? R.drawable.p0 : R.drawable.p1)
			)
		);
	}
	
	public void setStyle(int t, int l) {
		this.t=t;
		this.l=l;
        RelativeLayout.LayoutParams a = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		a.setMargins(l,t,0,0);
		this.setLayoutParams(a);

	}
}
