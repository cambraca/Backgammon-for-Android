package com.morphodev.backgammon;

import java.util.ArrayList;
import java.util.Timer;

import android.app.Activity;
import android.graphics.drawable.Drawable.ConstantState;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class Game extends Activity {
	private static final int timeBetweenSteps = 100;
	
	private int piece_original_top;
	private int piece_original_left;
	private boolean isSelected = false;
	Piece dragging_piece;
	int dragging_frames;
	ArrayList<Position> possiblePoints;
	boolean global_is_bar;
    int global_nn;
    int global_nn_bar;
    
	private int[] point = new int[26];
	private int[] bar = new int[2];
	private int[][] dice = new int[2][4];
	private int MoveType, NDraw, MoveValue, MaxValue;
	private int[] MoveFrom = new int[4];
	private int[] MoveTo = new int[4];
	public int Score0, Score1;
	public boolean[] player = new boolean[2];
	private int CurDice, BestDice, BestValue;
	private int[] CurClick = new int[4];
	private int[] BestClick = new int[4];
	private boolean save_game_flag = false;
	public boolean game_finished = false;
	//private Timer timer;
	//private BGTimerTask task;
	
	private ArrayList<Piece>[] pointImages = new ArrayList[26];
	private ArrayList<Piece>[] barImages = new ArrayList[2];
	private Die[][] diceImages = new Die[2][4];
	
	private enum ButtonPosition {LEFT, CENTER, RIGHT, DEFAULT, FALSE};
	public enum Button {ROLL_DICE, OK, PASS, CANCEL, TEMP};
	private ButtonPosition[] buttonsShown = new ButtonPosition[5]; //should be 4
	private ButtonImg[][] buttonImages = new ButtonImg[5][3]; //5 should be 4
	
	private ConfigDifficulty configDifficulty = ConfigDifficulty.EASY;
	public enum ConfigDifficulty {EASY, HARD};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        
/*        tv = new TextView(this);
        tv.setText("Hola hola!");
        RelativeLayout p1_6 = (RelativeLayout) findViewById(R.id.p1_6);
        RelativeLayout.LayoutParams a = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        a.setMargins(0,25,0,0);
        p1_6.addView(tv, a);
        tv.append(tv.getLeft() + ", " + tv.getTop());
*/

        new BoardArea(this);
        
        //buttonImages[Button.TEMP.ordinal()][ButtonPosition.LEFT.ordinal()] = new ButtonImg(this);
        //buttonImages[Button.TEMP.ordinal()][ButtonPosition.LEFT.ordinal()].addButton(0,0,Button.TEMP);
        //buttonImages[Button.TEMP.ordinal()][ButtonPosition.LEFT.ordinal()].show();
        
        buttonImages[Button.ROLL_DICE.ordinal()][ButtonPosition.LEFT.ordinal()] = new ButtonImg(this);
        buttonImages[Button.ROLL_DICE.ordinal()][ButtonPosition.LEFT.ordinal()].addButton(0,18,Button.ROLL_DICE);
        buttonImages[Button.ROLL_DICE.ordinal()][ButtonPosition.CENTER.ordinal()] = new ButtonImg(this);
        buttonImages[Button.ROLL_DICE.ordinal()][ButtonPosition.CENTER.ordinal()].addButton(0,113,Button.ROLL_DICE);
        buttonImages[Button.ROLL_DICE.ordinal()][ButtonPosition.RIGHT.ordinal()] = new ButtonImg(this);
        buttonImages[Button.ROLL_DICE.ordinal()][ButtonPosition.RIGHT.ordinal()].addButton(0,208,Button.ROLL_DICE);
        buttonImages[Button.OK.ordinal()][ButtonPosition.LEFT.ordinal()] = new ButtonImg(this);
        buttonImages[Button.OK.ordinal()][ButtonPosition.LEFT.ordinal()].addButton(0,4,Button.OK);
        buttonImages[Button.OK.ordinal()][ButtonPosition.CENTER.ordinal()] = new ButtonImg(this);
        buttonImages[Button.OK.ordinal()][ButtonPosition.CENTER.ordinal()].addButton(0,135,Button.OK);
        buttonImages[Button.OK.ordinal()][ButtonPosition.RIGHT.ordinal()] = new ButtonImg(this);
        buttonImages[Button.OK.ordinal()][ButtonPosition.RIGHT.ordinal()].addButton(0,194,Button.OK);
        buttonImages[Button.PASS.ordinal()][ButtonPosition.LEFT.ordinal()] = new ButtonImg(this);
        buttonImages[Button.PASS.ordinal()][ButtonPosition.LEFT.ordinal()].addButton(0,40,Button.PASS);
        buttonImages[Button.PASS.ordinal()][ButtonPosition.CENTER.ordinal()] = new ButtonImg(this);
        buttonImages[Button.PASS.ordinal()][ButtonPosition.CENTER.ordinal()].addButton(0,135,Button.PASS);
        buttonImages[Button.PASS.ordinal()][ButtonPosition.RIGHT.ordinal()] = new ButtonImg(this);
        buttonImages[Button.PASS.ordinal()][ButtonPosition.RIGHT.ordinal()].addButton(0,230,Button.PASS);
        buttonImages[Button.CANCEL.ordinal()][ButtonPosition.LEFT.ordinal()] = new ButtonImg(this);
        buttonImages[Button.CANCEL.ordinal()][ButtonPosition.LEFT.ordinal()].addButton(0,77,Button.CANCEL);
        buttonImages[Button.CANCEL.ordinal()][ButtonPosition.CENTER.ordinal()] = new ButtonImg(this);
        buttonImages[Button.CANCEL.ordinal()][ButtonPosition.CENTER.ordinal()].addButton(0,135,Button.CANCEL);
        buttonImages[Button.CANCEL.ordinal()][ButtonPosition.RIGHT.ordinal()] = new ButtonImg(this);
        buttonImages[Button.CANCEL.ordinal()][ButtonPosition.RIGHT.ordinal()].addButton(0,267,Button.CANCEL);
        buttonsShown[Button.ROLL_DICE.ordinal()] = ButtonPosition.FALSE;
        buttonsShown[Button.OK.ordinal()] = ButtonPosition.FALSE;
        buttonsShown[Button.PASS.ordinal()] = ButtonPosition.FALSE;
        buttonsShown[Button.CANCEL.ordinal()] = ButtonPosition.FALSE;
        
        player[0] = true; //true=human, false=computer
        player[1] = false;
        
        Init();
        
    }
    
    private void Init() {
        debug("init called");


        for (int i=0; i <= 25; i++)
        	pointImages[i] = new ArrayList<Piece>(15);
        for (int i=0; i <= 1; i++)
        	barImages[i] = new ArrayList<Piece>(15);
        for (int ii = 0; ii < 26; ii++)
            point[ii] = 0;
        point[1] = -2;
        point[6] = 5;
        point[8] = 3;
        point[12] = -5;
        point[13] = 5;
        point[17] = -3;
        point[19] = -5;
        point[24] = 2;
        for (int ii = 0; ii < 2; ii++) 
            bar[ii] = 0;
        for (int ii = 0; ii < 4; ii++) { 
            dice[0][ii] = 0;
            dice[1][ii] = 0;
            diceImages[0][ii] = new Die(this);
            diceImages[0][ii].addDie();
            diceImages[1][ii] = new Die(this);
            diceImages[1][ii].addDie();
        }
        MoveType = -1;
        NDraw = 0;
        
        for (int ii = 0; ii < 4; ii++) {
            MoveFrom[ii] = -1;
            MoveTo[ii] = -1;
        }

        /*
        bar[0] = 2;
        bar[1] = 4;
        dice[0][0] = 1;
        dice[1][0] = 5;
        */
        RefreshScreen();
        
        showRollButton(true, ButtonPosition.CENTER);
        showOKButton(false);
        showPassButton(false);
        showCancelButton(false);

         //message("Roll the dice");
        Score0 = 167;
        Score1 = 167;
        
        mHandler.removeCallbacks(mUpdateTimeTask);
        mHandler.postDelayed(mUpdateTimeTask, timeBetweenSteps);
        //task = new BGTimerTask(this);
        //timer = new Timer();
        ///////////timer.scheduleAtFixedRate(task, 1000, 1000);
    }
    private Handler mHandler = new Handler();
    private Runnable mUpdateTimeTask = new Runnable() {
    	public void run() {
    		RunTimer();
		 
    		mHandler.postDelayed(this, timeBetweenSteps);
    	}
    };
    
    private Position RefreshPoint(int ii) {
		return RefreshPoint(ii, null);
	}
    private Position RefreshBar(int ii) {
		return RefreshBar(ii, null);
	}
    public void debug(String txt) {
    	Log.i("BG", txt);
    }
    public void debug2(String txt) {
    	Log.i("BG2", txt);
    }
    private void message(String txt) {
    	Log.i("BGmsg", txt);
    }
    private void message(String txt, ButtonPosition pos) {
    	message(txt);
    	/*$$('div.message,div.message_back').each(Element.hide);
        if (txt != '') {
            var position = pos ? pos : (MoveType == 1 ? 'left' : (MoveType == 0 ? 'right' : 'center'));
            $('message_back_' + position).show();
            $('message_' + position).show();
            $('message_' + position).innerHTML = txt;
            debug("MSG in " + position + ": " + txt);
        }*/
    }
    private void RefreshScreen() {
        int ii;
        for (ii = 0; ii < 26; ii++) RefreshPoint(ii);
        for (ii=0; ii<2; ii++) RefreshBar(ii);
        RefreshDice(true);
        debug("screen refreshed");
    }
    private int random(int nn) {
    	return (int) (Math.floor(Math.random() * 1000) % nn);
    }
    private int randomRange(int Lower_Bound, int Upper_Bound) {
    	return (int) (Lower_Bound + Math.random() * ( Upper_Bound - Lower_Bound) + 0.5) ;
    }
    private void RefreshDice(boolean random_move) {
		if (random_move) {
            int number, calcTop, calcLeft;
            for (int kk = 0; kk < 2; kk++) {
                int start = -1;
                number = 0;
                for (int ii = 0; ii < 4; ii++) {
                    if (dice[kk][ii] != 0) {
                        number++;
                        if (start == -1) 
                            start = ii;
                    }
                }
                if (number != 0) 
                    for (int ii = start; ii < number + start; ii++) {
                        calcLeft = randomRange(10 + 125, 10 + 133);
                        calcTop = randomRange(10 + 194 + (145 / number * (ii - start))
                            - 190 * kk, 10 + 194 + (145 / number * (ii - start + 1))
                            - 26 - 190 * kk);
                        diceImages[kk][ii].setStyle(calcTop, calcLeft, randomRange(-30,30));
                    }
            }
        }
        for (int ii = 0; ii < 4; ii++) {
            for (int kk = 0; kk < 2; kk++) {
                if (dice[kk][ii] == 0) 
                	diceImages[kk][ii].hide();
                else
                    if (dice[kk][ii] > 0) {
                    	diceImages[kk][ii].src(kk, dice[kk][ii]);
                    	diceImages[kk][ii].show();
                    } else {
                    	diceImages[kk][ii].src(2, Math.abs(dice[kk][ii]));
                    	diceImages[kk][ii].show();
                    }
            }
        }
    }
    private Position RefreshPoint(int ii, Position fromPosition) {
    	debug2(ii + " " + point[ii] + " " + pointImages[ii].size());
    	ArrayList<Piece> temp = pointImages[ii];
    	int nn = point[ii];
    	int jj;
    	int calcLeft=0, calcTop=0;
    	int alreadySet;
    	
//tv.append(String.valueOf(ii));
//	if (fromPosition == null) return null;
    	
    	if (fromPosition != null) {
    		if (temp.size() == 1 && Math.abs(nn)==1) {
    			//piece has been eaten
    			jj = 0;
    			temp.get(0).removePiece();
    			temp.remove(0);
    		} else
    			jj = temp.size();
    		
    		Piece newPiece = new Piece(this);
    		newPiece.setPieceImage(ii==0 || ii==25 ? 'o' : 'p', nn > 0 ? 0 : 1);
    		/*
    		if (ii == 0)
    			newPiece.container = (RelativeLayout) findViewById(R.id.p0);
    		else if (ii <= 6)
    			newPiece.container = (RelativeLayout) findViewById(R.id.p1_6);
    		else if (ii <= 12)
    			newPiece.container = (RelativeLayout) findViewById(R.id.p7_12);
    		else if (ii <= 18)
    			newPiece.container = (RelativeLayout) findViewById(R.id.p13_18);
    		else if (ii <= 24)
    			newPiece.container = (RelativeLayout) findViewById(R.id.p19_24);
    		else
    			newPiece.container = (RelativeLayout) findViewById(R.id.p25);
    		*/
    		
    		if (ii == 0) {
                calcLeft = 10 + 9 * jj;
                calcTop = 10 + 351;
            }
            if (ii == 25) {
                calcLeft = 10 + 271 - 9 * jj;
                calcTop = 10 + 351;
            }
            if (ii >= 1 && ii <= 12) 
                calcLeft = 10 + 24 * (jj % 5);
            if (ii >= 1 && ii <= 6) 
                calcTop = 10 + 316 - 25 * (ii - 1);
            if (ii >= 7 && ii <= 12) 
                calcTop = 10 + 126 - 25 * (ii - 7);
            if (ii >= 13 && ii <= 24) 
                calcLeft = 10 + 256 - 24 * (jj % 5);
            if (ii >= 13 && ii <= 18) 
                calcTop = 10 + 1 + 25 * (ii - 13);
            if (ii >= 19 && ii <= 24) 
                calcTop = 10 + 191 + 25 * (ii - 19);
            if (ii > 0 && ii < 25) {
                calcTop += Math.floor(jj / 5);
                calcLeft -= Math.floor(jj / 5) * 3;
            }
            //a.setMargins(0,25,0,0);
            newPiece.addPiece(calcLeft, calcTop);
            pointImages[ii].add(newPiece);
    	} else if (Math.abs(nn) == temp.size() - 1) {
    		debug2("removing one piece from point " + ii);
			//just one piece removed. remove it and return its position
    		Position ret = new Position(temp.get(temp.size()-1).l,temp.get(temp.size()-1).t);
    		temp.get(temp.size()-1).removePiece();
			temp.remove(temp.size()-1);
			return ret;
/*				temp = temp.pop();
				var ret = new Array(parseInt(temp.getStyle('left')), parseInt(temp.getStyle('top')));
				temp.remove();
				debug(ret[0] + ' removed one ' + ret[1]);
				return ret;
*/
		} else {
			if (nn == 0) {
				while (temp.size() > 0) {
					temp.get(0).removePiece();
	    			temp.remove(0);
				}
				alreadySet = 0;
			} else if (temp.size() > 0 && temp.get(0).pieceType == 'p' && temp.get(0).pieceSide == (nn > 0 ? 1 : 0)) {
				while (temp.size() > 0) {
					temp.get(0).removePiece();
	    			temp.remove(0);
				}
				alreadySet = 0;
			} else 
				alreadySet = temp.size();
			while (alreadySet > Math.abs(nn)) {
				temp.get(temp.size()-1).removePiece();
				temp.remove(temp.size()-1);
				alreadySet--;
			}
			for (jj = alreadySet; jj < Math.abs(nn); jj++) {
	    		Piece newPiece = new Piece(this);
	    		newPiece.setPieceImage(ii==0 || ii==25 ? 'o' : 'p', nn > 0 ? 0 : 1);
	    		if (ii == 0) {
                    calcLeft = 10 + 9 * jj;
                    calcTop = 10 + 351;
                }
                if (ii == 25) {
                    calcLeft = 10 + 271 - 9 * jj;
                    calcTop = 10 + 351;
                }
                if (ii >= 1 && ii <= 12) 
                    calcLeft = 10 + 24 * (jj % 5);
                if (ii >= 1 && ii <= 6) 
                    calcTop = 10 + 316 - 25 * (ii - 1);
                if (ii >= 7 && ii <= 12) 
                    calcTop = 10 + 126 - 25 * (ii - 7);
                if (ii >= 13 && ii <= 24) 
                    calcLeft = 10 + 256 - 24 * (jj % 5);
                if (ii >= 13 && ii <= 18) 
                    calcTop = 10 + 1 + 25 * (ii - 13);
                if (ii >= 19 && ii <= 24) 
                    calcTop = 10 + 191 + 25 * (ii - 19);
                if (ii > 0 && ii < 25) {
                    calcTop += Math.floor(jj / 5);
                    calcLeft -= Math.floor(jj / 5) * 3;
                }
	            //a.setMargins(0,25,0,0);
	            newPiece.addPiece(calcLeft,calcTop);
	            pointImages[ii].add(newPiece);
			}
		}
		return null;
    }
    private Position RefreshBar(int ii, Position fromPosition) {
        int jj, nn = bar[ii];
        int alreadySet;
    	ArrayList<Piece> temp = barImages[ii];
    	int calcLeft=0, calcTop=0;

    	if (nn == temp.size() - 1) {
            //just one piece removed. remove it and return its position
    		Position ret = new Position(temp.get(temp.size()-1).l,temp.get(temp.size()-1).t);
    		temp.get(temp.size()-1).removePiece();
			temp.remove(temp.size()-1);
			return ret;
        } else {
            if (nn == 0) {
				while (temp.size() > 0) {
					temp.get(0).removePiece();
	    			temp.remove(0);
				}
                alreadySet = 0;
            }
            else 
                alreadySet = temp.size();
            while (alreadySet > nn) {
        		temp.get(temp.size()-1).removePiece();
    			temp.remove(temp.size()-1);
                alreadySet--;
            }
            for (jj = alreadySet; jj < nn; jj++) {
	    		Piece newPiece = new Piece(this);
	    		newPiece.setPieceImage('p', ii);
                calcTop = 159;
                if (ii == 1) {
                    calcLeft = 10 + 10 + 30 * (jj % 4);
                    calcTop += Math.floor(jj / 4) * 2;
                    calcLeft += Math.floor(jj / 4) * 15;
                } else if (ii == 0) {
                    calcLeft = 10 + 245 - 30 * (jj % 4);
                    calcTop += Math.floor(jj / 4) * 2;
                    calcLeft -= Math.floor(jj / 4) * 15;
                }
                /*if (fromPosition) {
                    temp.setStyle({
                        left: fromPosition[0] + 'px',
                        top: fromPosition[1] + 'px'
                    });
                    $('chips_holder').insert(temp);
                    var toPosition = new Array(calcLeft, calcTop);
                    animatePiece(temp.id, fromPosition[0], fromPosition[1],
                                 toPosition[0], toPosition[1], 0, 6);
                } else {*/
	            newPiece.addPiece(calcLeft,calcTop);
	            barImages[ii].add(newPiece);
                //}
            }
        }
        return null;
    }
    
    
    
    private void showRollButton(boolean show, ButtonPosition position) {
        showGenericButton(Button.ROLL_DICE, show, position);
    }
    private void showOKButton(boolean show, ButtonPosition position) {
        showGenericButton(Button.OK, show, position);
    }
    private void showPassButton(boolean show, ButtonPosition position) {
        showGenericButton(Button.PASS, show, position);
    }
    private void showCancelButton(boolean show, ButtonPosition position) {
        showGenericButton(Button.CANCEL, show, position);
    }
    private void showRollButton(boolean show) {
        showGenericButton(Button.ROLL_DICE, show, ButtonPosition.DEFAULT);
    }
    private void showOKButton(boolean show) {
        showGenericButton(Button.OK, show, ButtonPosition.DEFAULT);
    }
    private void showPassButton(boolean show) {
        showGenericButton(Button.PASS, show, ButtonPosition.DEFAULT);
    }
    private void showCancelButton(boolean show) {
        showGenericButton(Button.CANCEL, show, ButtonPosition.DEFAULT);
    }
    private boolean showingRollButton() {
        return buttonsShown[Button.ROLL_DICE.ordinal()] != ButtonPosition.FALSE;
    }
    private boolean showingOKButton() {
        return buttonsShown[Button.OK.ordinal()] != ButtonPosition.FALSE;
    }
    private boolean showingPassButton() {
        return buttonsShown[Button.PASS.ordinal()] != ButtonPosition.FALSE;
    }
    private boolean showingCancelButton() {
        return buttonsShown[Button.CANCEL.ordinal()] != ButtonPosition.FALSE;
    }
    private void showGenericButton(Button button, boolean show, ButtonPosition position) {
        //if (position == 0) position = 'left';
        //else if (position == 1) position = 'right';
        
/////    	buttonsShown[Button.OK] = ButtonPosition.LEFT;
    	
        if (button == Button.ROLL_DICE || button == Button.OK)
        	buttonImages[button.ordinal()][ButtonPosition.CENTER.ordinal()].hide();
        buttonImages[button.ordinal()][ButtonPosition.LEFT.ordinal()].hide();
        buttonImages[button.ordinal()][ButtonPosition.RIGHT.ordinal()].hide();
        
        if (show) {
        	buttonImages[button.ordinal()][position.ordinal()].show();
            buttonsShown[button.ordinal()] = position;
        } else
            buttonsShown[button.ordinal()] = ButtonPosition.FALSE;
    }
    
	public void RollNewClick(boolean vv) {
		if (vv && !showingRollButton()) 
			return;
		if (showingRollButton()) {
			if ((MoveType > 0) && (!player[MoveType])) 
				return;
			RollDice(false);
			showRollButton(false);
			//TODO: show a NEW GAME button??? or just add it to the scene menu, better that way
			//    document.Options.RollNewButton.value="New Game";
			//    RollNewButtonValue="New Game";
			if (MoveType < 0) {
				showOKButton(true, ButtonPosition.CENTER);
				//OKButtonDisable(false);
			} else {
				if (MaxValue == 0) {
					showOKButton(false);
					showPassButton(true, MoveType == 0 ? ButtonPosition.LEFT : ButtonPosition.RIGHT);
					message("You must pass");
					//document.Options.OKButton.value="Pass";
					//OKButtonDisable(false);
				}
			}
		} else {
			if ((Score0 == 0) || (Score1 == 0)) 
				Init();
			else {
				//TODO!!!!
				//if (confirm("Do you really want to surrender the current game ?")) Init();
			}
		}
	}
	private void RollDice(boolean ee) {
	    if (MoveType < 0) {
	        do {
	            dice[0][1] = random(6) + 1;
	            dice[1][2] = random(6) + 1;
	        } while (dice[0][1] == dice[1][2]);
            playRollSound();
            MoveValue = 0;
            MaxValue = dice[0][1] + dice[1][2];
            RefreshDice(true);
	        if (dice[0][1] > dice[1][2]) 
	            message("Player 1 begins");
	        else 
	            message("Player 2 begins");
	    }
	    if (MoveType >= 0) {
	        dice[MoveType][1] = random(6) + 1;
	        dice[MoveType][2] = random(6) + 1;
	        if (dice[MoveType][1] == dice[MoveType][2]) {
	            dice[MoveType][0] = dice[MoveType][2];
	            dice[MoveType][3] = dice[MoveType][2];
	        } else {
	            dice[MoveType][0] = 0;
	            dice[MoveType][3] = 0;
	        }
	        playRollSound();
	        MoveValue = 0;
	        GetMaxValue(ee);
	        RefreshDice(true);
	        if (player[MoveType]) {
	            message("Make your move");
	        }
	    }
	}
	private void GetMaxValue(boolean ee) {
	    debug("entering getMaxValue");
	    int nn1, mm = dice[MoveType][0] + dice[MoveType][1] + dice[MoveType][2] + dice[MoveType][3];
	    MaxValue = 0;
	    if (ee) {
	        for (nn1 = 0; nn1 < 4; nn1++) 
	            CurClick[nn1] = -1;
	        CurDice = 0;
	        BestDice = 0;
	        BestValue = -9999;
	    }
	    if (dice[MoveType][0] == 0) {
	        VirtualClick(2, ee, 0);
	        if (!ee && (MaxValue == mm)) 
	            return;
	        SwitchDice(MoveType, 0);
	        if (ee) 
	            CurDice = 1;
	        VirtualClick(2, ee, 0);
	        SwitchDice(MoveType, 0);
	    }
	    else 
	        VirtualClick(4, ee, 0);
	    debug("exiting getMaxValue");
	}
	private boolean SwitchDice(int nn, int vv) {
		debug("switching dice!");
	    if (MoveType < 0) 
	        return (false);
	    if (nn != MoveType) 
	        return (false);
	    if ((vv == 1) && (!player[nn])) 
	        return (false);
	    if ((vv == 1) && showingCancelButton())
	        return (false);
	    int tt = dice[nn][1];
	    dice[nn][1] = dice[nn][2];
	    dice[nn][2] = tt;
	    //also switch the positions and rotations of the images
	    int tt_top = diceImages[nn][1].getStyleTop();
	    int tt_left = diceImages[nn][1].getStyleLeft();
	    int tt_rotate = diceImages[nn][1].getStyleRotation();
	    diceImages[nn][1].setStyle(
	    		diceImages[nn][2].getStyleTop(),
	    		diceImages[nn][2].getStyleLeft(),
	    		diceImages[nn][2].getStyleRotation()
	    );
	    diceImages[nn][2].setStyle(
	    		tt_top,
	    		tt_left,
	    		tt_rotate
	    );
	    //finished switching position & rotation of the images
	    if (vv > 0) 
	        RefreshDice(false);
	    return (true);
	}
	private void VirtualClick(int jj, boolean ee, int vv) {
		if (jj == 0) {
	        if (MaxValue <= vv) {
	            if (ee) 
	                Evaluate(vv);
	            else 
	                MaxValue = vv;
	        }
	        return;
	    }
	    int ii, jjj = jj - 1, nn;
	    if (bar[MoveType] > 0) {
	        CurClick[jjj] = 0;
	        nn = ClickBar(MoveType, 0);
	        if (nn > 0) {
	            nn += vv;
	            VirtualClick(jjj, ee, nn);
	            CancelClick(false);
	        } else 
	            VirtualClick(0, ee, vv);
	    } else {
	        for (ii = 1; ii <= 24; ii++) {
	            CurClick[jjj] = ii;
	            nn = ClickPoint(ii, 0);
	            if (nn > 0) {
	                nn += vv;
	                VirtualClick(jjj, ee, nn);
	                CancelClick(false);
	            } else 
	                VirtualClick(0, ee, vv);
	        }
	    }
	}
	private int ClickPoint(int nn, int vv) {
		Position fromPosition = null;
	    if (MoveType < 0) 
	        return (0);
	    if ((vv == 1) && (!player[MoveType])) 
	        return (0);
	    if (point[nn] == 0) 
	        return (0);
	    if ((point[nn] < 0) && (MoveType == 0) || (point[nn] > 0) && (MoveType == 1)) 
	        return (0);
	    if (bar[MoveType] > 0) 
	        return (0);
	    int mm, ii, dd = 0;
	    while ((dd <= 3) && (dice[MoveType][dd] <= 0)) 
	        dd++;
	    if (dd == 4)
	        return (0);
	    save_game_flag = true;
	    if (MoveType == 0) {
	        mm = nn - dice[0][dd];
	        if (mm <= 0) {
	            mm = 0;
	            for (ii = 7; ii < 25; ii++) {
	                if (point[ii] > 0) 
	                    return (0);
	            }
	        }
	        if (point[mm] < -1) 
	            return (0);
	        MoveFrom[dd] = nn;
	        MoveTo[dd] = point[mm];
	        point[nn]--;
	        if (vv > 0) 
	            fromPosition = RefreshPoint(nn);
	        point[mm]++;
	        boolean moveToBar = false;
	        if (point[mm] == 0) {
	            bar[1]++;
	            point[mm]++;
	            moveToBar = true;
	        }
	        if (vv > 0) 
	            fromPosition = RefreshPoint(mm, fromPosition);
	        if (moveToBar && vv > 0) 
	            RefreshBar(1, fromPosition);
	        MoveValue += nn - mm;
	        dice[0][dd] *= -1;
	        if ((vv == 1) && ((MoveValue == MaxValue) || (dice[0][0] <= 0
	            && dice[0][1] <= 0 && dice[0][2] <= 0 && dice[0][3] <= 0)))
	            showOKButton(true, ButtonPosition.LEFT); 
	            //OKButtonDisable(false);
	        if (vv > 0) {
	            RefreshDice(false);//RefreshDice(0);
	            message("");
	        }
	        if (vv == 1)
	            showCancelButton(true, ButtonPosition.LEFT);
	            //CancelButtonDisable(false);
	        return (nn - mm);
	    }
	    else {
	        mm = nn + dice[1][dd];
	        if (mm >= 25) {
	            mm = 25;
	            for (ii = 1; ii < 19; ii++) {
	                if (point[ii] < 0) {
	                    return (0);
	                }
	            }
	        }
	        if (point[mm] > 1) 
	            return (0);
	        MoveFrom[dd] = nn;
	        MoveTo[dd] = point[mm];
	        point[nn]++;
	        if (vv > 0)
	            fromPosition = RefreshPoint(nn);
	        point[mm]--;
	        boolean moveToBar = false;
	        if (point[mm] == 0) {
	            bar[0]++;
	            point[mm]--;
	            moveToBar = true;
	        }
	        if (vv > 0)
	            fromPosition = RefreshPoint(mm, fromPosition);
	        if (moveToBar && vv > 0) 
	            RefreshBar(0, fromPosition);
	        MoveValue += mm - nn;
	        dice[1][dd] *= -1;
	        if ((vv == 1) && ((MoveValue == MaxValue) || (dice[1][0] <= 0
	            && dice[1][1] <= 0 && dice[1][2] <= 0 && dice[1][3] <= 0)))
//	        if ((vv == 1) && (MoveValue == MaxValue)) 
	            showOKButton(true, ButtonPosition.RIGHT);
	            //OKButtonDisable(false);
	        if (vv > 0) {
	            RefreshDice(false);//RefreshDice(1);
	            message("");
	        }
	        if (vv == 1)
	            showCancelButton(true, ButtonPosition.RIGHT);
	            //CancelButtonDisable(false);
	        return (mm - nn);
	    }
	}
	public void CancelClick(boolean vv) {
		if (vv && !showingCancelButton())
		    //if ((IsNetOper) && (vv) && (document.Options.CancelButton.value == "")) 
	        return;
	    if (MoveType < 0) 
	        return;
	    Position fromPosition = null;
	    int mm, dd = 3;
	    MoveValue = 0;
	    while ((dd >= 0) && (dice[MoveType][dd] >= 0)) 
	        dd--;
	    if (dd < 0) {
	        if (vv) {
	            RefreshDice(false);
	            showOKButton(false);
	            //OKButtonDisable(true);
	            showCancelButton(false);
	            //CancelButtonDisable(true);
	        }
	        return;
	    }
	    dice[MoveType][dd] *= -1;
	    if (MoveType == 0) {
	        if (MoveFrom[dd] == 25) {
	            bar[0]++;
	            //if (vv) 
	            //    RefreshBar(0);
	        }
	        else {
	            point[MoveFrom[dd]]++;
	            //if (vv) 
	            //    RefreshPoint(MoveFrom[dd]);
	        }
	        mm = MoveFrom[dd] - dice[0][dd];
	        if (mm < 0) 
	            mm = 0;
	        point[mm] = MoveTo[dd];
	        if (vv) 
	            fromPosition = RefreshPoint(mm);
	        //begin: code for animation
	        if (MoveFrom[dd] == 25) {
	            if (vv) 
	                RefreshBar(0);
	        }
	        else {
	            if (vv) 
	                RefreshPoint(MoveFrom[dd], fromPosition);
	        }
	        //end: code for animation
	        if (MoveTo[dd] < 0) {
	            bar[1]--;
	            if (vv) 
	                RefreshBar(1);
	        }
	    }
	    else {
	        if (MoveFrom[dd] == 0) {
	            bar[1]++;
	        }
	        else {
	            point[MoveFrom[dd]]--;
	        }
	        mm = MoveFrom[dd] + dice[1][dd];
	        if (mm > 25) 
	            mm = 25;
	        point[mm] = MoveTo[dd];
	        if (vv) 
	            fromPosition = RefreshPoint(mm);
	        //begin: code for animation
	        if (MoveFrom[dd] == 0) {
	            if (vv) 
	                RefreshBar(1);
	        }
	        else {
	            if (vv) 
	                RefreshPoint(MoveFrom[dd], fromPosition);
	        }
	        //end: code for animation
	        if (MoveTo[dd] > 0) {
	            bar[0]--;
	            if (vv) 
	                RefreshBar(0);
	        }
	    }
	    if (vv) 
	        CancelClick(vv);
	}
	private int ClickBar(int nn, int vv) {
		debug("Click bar");
	    if (MoveType != nn) 
	        return (0);
	    if ((vv == 1) && (!player[nn])) 
	        return (0);
	    if (bar[nn] == 0) 
	        return (0);
	    int dd = 0;
	    while ((dd < 3) && (dice[MoveType][dd] <= 0)) 
	        dd++;
	    if (dd == 4) 
	        return (0);
	    save_game_flag = true;
	    Position fromPosition = null;
	    boolean moveFromBar = false;
	    if (MoveType == 0) {
	        if (point[25 - dice[0][dd]] < -1) 
	            return (0);
	        MoveFrom[dd] = 25;
	        MoveTo[dd] = point[25 - dice[0][dd]];
	        bar[nn]--;
	        if (vv > 0) 
	            fromPosition = RefreshBar(nn);
	        point[25 - dice[0][dd]]++;
	        if (point[25 - dice[0][dd]] == 0) {
	            bar[1]++;
	            moveFromBar = true;
	            point[25 - dice[0][dd]]++;
	        }
	        if (vv > 0) 
	            fromPosition = RefreshPoint(25 - dice[0][dd], fromPosition);
	        if (vv > 0 && moveFromBar) 
	            RefreshBar(1, fromPosition);
	        MoveValue += dice[0][dd];
	        if ((vv == 1) && (MoveValue == MaxValue)) 
	            showOKButton(true, MoveType == 1 ? ButtonPosition.RIGHT : ButtonPosition.LEFT);
	        //OKButtonDisable(false);
	        dice[0][dd] *= -1;
	        if (vv > 0) {
	            RefreshDice(false);//RefreshDice(0);
	            message("");
	        }
	        if (vv == 1) 
	            showCancelButton(true, MoveType == 1 ? ButtonPosition.RIGHT : ButtonPosition.LEFT);
	        //CancelButtonDisable(false);
	        return (-dice[0][dd]);
	    }
	    else {
	        if (point[dice[1][dd]] > 1) 
	            return (0);
	        MoveFrom[dd] = 0;
	        MoveTo[dd] = point[dice[1][dd]];
	        bar[nn]--;
	        if (vv > 0) 
	            fromPosition = RefreshBar(nn);
	        point[dice[1][dd]]--;
	        if (point[dice[1][dd]] == 0) {
	            bar[0]++;
	            moveFromBar = true;
	            point[dice[1][dd]]--;
	        }
	        if (vv > 0) 
	            fromPosition = RefreshPoint(dice[1][dd], fromPosition);
	        if (vv > 0 && moveFromBar) 
	            RefreshBar(0, fromPosition);
	        MoveValue += dice[1][dd];
	        if ((vv == 1) && (MoveValue == MaxValue)) 
	            showOKButton(true, MoveType == 1 ? ButtonPosition.RIGHT : ButtonPosition.LEFT);
	        //OKButtonDisable(false);
	        dice[1][dd] *= -1;
	        if (vv > 0) {
	            RefreshDice(false);//RefreshDice(1);
	            message("");
	        }
	        if (vv == 1) 
	            showCancelButton(true, MoveType == 1 ? ButtonPosition.RIGHT : ButtonPosition.LEFT);
	        //CancelButtonDisable(false);
	        return (-dice[1][dd]);
	    }
	}
	private void Evaluate(int vv) {
		if (vv == 0) 
	        return;
	    int ii;
	    float ss0 = 0, ss1 = 0, ff0 = 0, ff1 = 25, ss, iinv = -1;
	    for (ii = 1; ii < 25; ii++) {
	        if (point[ii] < 0)//yellow pieces
	        {
	            ss1 += (ii - 25) * point[ii];//moves to do by yellow pieces
	            ss1 += -point[ii] * (ii - 12.5) * (ii - 12.5) / 50;//keep the stones in the middle
	            if (iinv == -1) 
	                iinv = 0;//yellow pieces found
	            if (ff1 > ii) 
	                ff1 = ii;//find last yellow piece
	        }
	        if (point[ii] > 0)//green pieces
	        {
	            ss0 += ii * point[ii];
	            ss0 += point[ii] * (ii - 12.5) * (ii - 12.5) / 50;//keep the stones in the middle
	            if (iinv == 0) 
	                iinv = 1;//green pieces found after yellow pieces -> inversion
	            if (ff0 < ii) 
	                ff0 = ii;//find last green piece
	        }
	    }
	    if (bar[0] > 0) {
	        if (configDifficulty == ConfigDifficulty.EASY)
	            ss0 += bar[0] * 8;//the way, green pieces have to go = 25 + bonus for capturing
	        else
	            ss0 += bar[0] * 32;//the way, green pieces have to go = 25 + bonus for capturing
	        ff0 = 25;//position of the last green piece
	    }
	    if (bar[1] > 0) {
	        if (configDifficulty == ConfigDifficulty.EASY)
	            ss1 += bar[1] * 8;//the way, yellow pieces have to go = 25 + bonus for capturing
	        else
	            ss1 += bar[1] * 32;//the way, yellow pieces have to go = 25 + bonus for capturing
	        ff1 = 0;//position of the last yellow piece
	    }
	    if (MoveType == 0)//evaluate from green point of view
	    {
	        ss = ss1 - ss0;
	        if ((iinv > 0) || (bar[1] > 0))//there's an inversion
	        {
	            for (ii = 1; ii < 25; ii++) {
	                if ((point[ii] == 1) && (ff1 < ii)) 
	                    ss -= (25 - ii) / 3;//can be captured
	                if (point[ii] > 1) 
	                    ss += 3;
	            }
	        }
	        else {
	            for (ii = 1; ii <= 6; ii++) {
	                if (point[ii] != 0) 
	                    ss += 3;
	            }
	        }
	        ss += point[0] * 3;
	    }
	    else//evaluate from yellow point of view
	    {
	        ss = ss0 - ss1;
	        if ((iinv > 0) || (bar[0] > 0))//there's an inversion
	        {
	            for (ii = 1; ii < 25; ii++) {
	                if ((point[ii] == -1) && (ff0 > ii)) 
	                    ss -= ii / 3;//can be captured
	                if (point[ii] < -1) 
	                    ss += 3;
	            }
	        }
	        else {
	            for (ii = 19; ii <= 24; ii++) {
	                if (point[ii] != 0) 
	                    ss += 3;
	            }
	        }
	        ss -= point[25] * 3;
	    }
	    ss += Math.random() / 100;
	    if ((MaxValue < vv) || (BestValue < ss)) {
	        MaxValue = vv;
	        BestValue = (int) ss;
	        BestDice = CurDice;
	        for (ii = 0; ii < 4; ii++) {
	            BestClick[ii] = CurClick[ii];
	        }
	    }
	}
	public void OKClick(boolean vv){ //done with TODOs
	    if (vv && !(showingOKButton() || showingPassButton())) return;
	    //if ((IsNetOper)&&(vv)&&(document.Options.OKButton.value=="")) return;
	    if (MoveType < 0) {
	        if (dice[0][1] > dice[1][2]) {
	            dice[0][2] = dice[1][2];
	            dice[1][2] = 0;
	            MoveType = 0;
	        }
	        else {
	            dice[1][1] = dice[1][2];
	            dice[1][2] = dice[0][1];
	            dice[0][1] = 0;
	            MoveType = 1;
	        }
	        if (player[MoveType]) 
	            message("Make your move");
	        showOKButton(false);
	        showPassButton(false);
	        //OKButtonDisable(true);
	/*        if (player[MoveType])
	            showRollButton(true, 1-MoveType); 
	            //RollNewButtonDisable(false);
	        else
	            showRollButton(false);
	            //RollNewButtonDisable(true);
	*/
	        RefreshDice(true);
	    } else {
	        dice[MoveType][0] = 0;
	        dice[MoveType][1] = 0;
	        dice[MoveType][2] = 0;
	        dice[MoveType][3] = 0;
	        RefreshDice(false);
	        MoveType = 1 - MoveType;
	        if (player[MoveType]) {
	            showRollButton(true, MoveType == 1 ? ButtonPosition.LEFT : ButtonPosition.RIGHT);
	            //RollNewButtonDisable(false);
	            //message("Roll the dice");
	        }
	        else
	            showRollButton(false);
	            //RollNewButtonDisable(true);
	        showOKButton(false);
	        showPassButton(false);
	        //OKButtonDisable(true);
	        showCancelButton(false);
	        //CancelButtonDisable(true);
	        int ii, ss = 0;
	        for (ii = 1; ii < 25; ii++) {
	            if (point[ii] > 0) 
	                ss += ii * point[ii];
	        }
	        ss += bar[0] * 25;
	        Score0 = ss;
	        if (ss == 0) {
	            //TODO: a way to show player 0 has won (green = 0)
	            //showRollButton(true, MoveType);
	            //RollNewButtonDisable(false);
	            //document.Options.RollNewButton.value = "New Game";
	            //RollNewButtonValue = "New Game";
	            finishGame(0);
	        }
	        ss = 0;
	        for (ii = 1; ii < 25; ii++) {
	            if (point[ii] < 0) 
	                ss -= (25 - ii) * point[ii];
	        }
	        ss += bar[1] * 25;
	        Score1 = ss;
	        if (ss == 0) {
	            //TODO: a way to show player 1 has won (yellow = 1)
	            //RollNewButtonDisable(false);
	            //document.Options.RollNewButton.value = "New Game";
	            //RollNewButtonValue = "New Game";
	            finishGame(1);
	            //message("Player 2 has won !");
	            //showRollButton(false);
	            //MoveType = -1;
	        }
	    }
	}
	private void finishGame(int player_won) {
		game_finished = true;
	    message("Player " + (player_won == 0 ? '1' : '2') + " has won", ButtonPosition.CENTER);
	    showOKButton(true, ButtonPosition.CENTER);
	    showRollButton(false);
	    MoveType = -1;
	    save_game_flag = false;
	    //Game.setResume(false);
	    /*if (Game.isResume()) {
	        $('btnResume').show();
	    } else 
	        $('btnResume').hide();
	   	*/
	}

	public void RunTimer() {
		//debug2(String.valueOf(point[1]));
		//point[3] = 4;
		//RefreshPoint(3);
	    //findViewById(R.id.board).postInvalidate();

		if (save_game_flag) {
	        save_game_flag = false;
	        debug("saving...");
	        saveGame();
	        debug("game saved" + isResume());
	    }
	    if (MoveType < 0)
	        return;
	    if (NDraw > 0) {
	        if (BestDice > 0) {
	            //message("Switching dice ...");
	            SwitchDice(MoveType, 2);
	            BestDice = 0;
	            RunTimer();
	            return;
	        }
	        for (int ii = 3; ii >= 0; ii--) {
	            if (BestClick[ii] >= 0) {
	                debug("entering A");
	                if (BestClick[ii] == 0) 
	                    ClickBar(MoveType, 2);
	                else {
	                    ClickPoint(BestClick[ii], 2);
	                }
	                message("Moving...");
	                BestClick[ii] = -1;
	                NDraw++;
	                return;
	            }
	        }
	        if (NDraw == 1) {
	            message("I must pass");
	            NDraw++;
	            return;
	        }
	        message("");
	        OKClick(false);
	        NDraw = 0;
	    } else {
	        if (!player[MoveType]) {
//	            if (!showingRollButton()) {
	                /*if ((document.Options.RollNewButton.value != "Roll Dice") && (RollNewButtonValue != "Roll Dice")) {
	     //??????????? what is this????
	     debug('STRANGE CODE!!!!!!!');
	     CancelClick(true);
	     GetMaxValue(true);
	     }
	     else {*/
//	                CancelClick(true);
//	                GetMaxValue(true);
//	            } else {
	        if (Score0 == 167 && Score1 == 167) {
	                CancelClick(true);
	                GetMaxValue(true);
	            } else {
	                message("Rolling dice...");
	                RollDice(true);
	            }
//	            }
	            NDraw = 1;
	        }
	    }
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mHandler.removeCallbacks(mUpdateTimeTask);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mHandler.removeCallbacks(mUpdateTimeTask);
		mHandler.postDelayed(mUpdateTimeTask, 1000);
	}
	
	private boolean isResume() {
		// TODO Auto-generated method stub
		return false;
	}

	private void saveGame() {
		// TODO Auto-generated method stub
		
	}

	private void playRollSound() {
		// TODO Auto-generated method stub
		
	}

	public void pointDragStart(boolean is_bar, int nn) {
		int nn_bar = 0;
		
	    if (MoveType<0) {return;}

	    //var el = Event.element(event);
	    if (!is_bar) {
	        if (!player[MoveType]) return; //player is computer
	        if (point[nn] == 0) return;
	        if ((point[nn] < 0) && (MoveType == 0) || (point[nn] > 0)
	            && (MoveType == 1)) return; //this point has other player pieces
	        
	        if (bar[MoveType] > 0) return; //player has pieces in bar, has to move those first
	        int dd = 0;
	        while ((dd <= 3) && (dice[MoveType][dd] <= 0)) dd++;
	        if (dd == 4) {return; }
	        
	    } else {
	    	nn_bar = nn;
	        nn = nn == 1 ? 0 : 25;
	        if (MoveType != nn_bar) return;
	        if (!player[nn_bar]) return;
	        if (bar[nn_bar] == 0) return;
	        int dd = 0;
	        while ((dd < 3) && (dice[MoveType][dd] <= 0)) dd++;
	        if (dd == 4) return;
	    }
	    
	    global_is_bar = is_bar;
	    global_nn = nn;
	    global_nn_bar = nn_bar;
	    
	    //calculate possible landing spaces
	    possiblePoints = new ArrayList<Position>();
	    int ddd;
		boolean nextDice;
		int mm;
		int ii;
		if (MoveType == 0) {
	        for (ddd = 0; ddd < 4; ddd++) {
	            nextDice = false;
	            if (dice[0][ddd] <= 0)
	                continue;
	            mm = nn - dice[0][ddd];
	            if (mm < 0) {
	                mm = 0;
	                for (ii = nn + 1; ii <= 6; ii++) {
	                    if (point[ii] > 0) {
	                        nextDice = true;
	                        break;
	                    }
	                }
	            }
	            if (nextDice)
	                continue;
	            if (mm == 0) {
	                for (ii = 7; ii < 25; ii++) {
	                    if (point[ii] > 0) {
	                        nextDice = true;
	                        break;
	                    }
	                }
	            }
	            if (nextDice)
	                continue;
	            if (point[mm] < -1)
	                continue;
	            //make sure possibles don't repeat            
	            for (ii = 0; ii < possiblePoints.size(); ii++) {
	                if (mm == possiblePoints.get(ii).x) {
	                    nextDice = true;
	                    break;
	                }
	            }
	            if (nextDice)
	                continue;
	                
	            possiblePoints.add(new Position(mm, ddd));
	        }
	    } else {
	        for (ddd = 0; ddd < 4; ddd++) {
	            nextDice = false;
	            if (dice[1][ddd] <= 0)
	                continue;
	            mm = nn + dice[1][ddd];
	            if (mm > 25) {
	                mm = 25;
	                for (ii = nn - 1; ii >= 19; ii--) {
	                    if (point[ii] < 0) {
	                        nextDice = true;
	                        break;
	                    }
	                }
	            }
	            if (nextDice)
	                continue;
	            if (mm == 25) {
	                for (ii = 18; ii > 0; ii--) {
	                    if (point[ii] < 0) {
	                        nextDice = true;
	                        break;
	                    }
	                }
	            }
	            if (nextDice)
	                continue;
	            if (point[mm] > 1)
	                continue;

	            //make sure possibles don't repeat            
	            for (ii = 0; ii < possiblePoints.size(); ii++) {
	                if (mm == possiblePoints.get(ii).x) {
	                    nextDice = true;
	                    break;
	                }
	            }
	            if (nextDice)
	                continue;
	                
	            possiblePoints.add(new Position(mm, ddd));
	        }
	    }
	    
	    debug("possibles "+possiblePoints.size());
	    
	    if (possiblePoints.size() == 0)
	        return;
	    
	    /*//draw the semitransparent pieces
	    var jj, temp, tempArea;
	    for (mm = 0; mm < possiblePoints.length; mm++) {
	        ii = possiblePoints[mm][0];
	        temp = document.createElement('img');
	        tempArea = document.createElement('img');
	        temp.src = theme((ii == 0 || ii == 25 ? 'o' : 'p') + MoveType);
	        tempArea.src = theme('suggestArea');
	        temp.addClassName((ii == 0 || ii == 25 ? 'o' : 'p'));
	        temp.addClassName('possible');
	        tempArea.addClassName('triangle');
	        jj = Math.abs(point[ii]);
	        if (ii == 0) {
	            calcLeft = 10 + 9 * jj;
	            calcTop = 361;
	        }
	        if (ii == 25) {
	            calcLeft = 281 - 9 * jj;
	            calcTop = 361;
	        }
	        if (ii >= 1 && ii <= 12) 
	            calcLeft = 10 + 24 * (jj % 5);
	        if (ii >= 1 && ii <= 6) 
	            calcTop = 326 - 25 * (ii - 1);
	        if (ii >= 7 && ii <= 12) 
	            calcTop = 136 - 25 * (ii - 7);
	        if (ii >= 13 && ii <= 24) 
	            calcLeft = 266 - 24 * (jj % 5);
	        if (ii >= 13 && ii <= 18) 
	            calcTop = 11 + 25 * (ii - 13);
	        if (ii >= 19 && ii <= 24) 
	            calcTop = 201 + 25 * (ii - 19);
	        if (ii > 0 && ii < 25) {
	            calcTop += Math.floor(jj / 5);
	            calcLeft -= Math.floor(jj / 5) * 3;
	        }
	        temp.setStyle({
	            left: calcLeft + 'px',
	            top: calcTop + 'px'
	        });
	        
	        var calcLeftArea=0;
	        
	        if (calcLeft < 125) {
	            calcLeftArea = 10;
	            //pixel extra correction
	            calcTop --;
	        }
	        else {
	            calcLeftArea = 162;
	            tempArea.addClassName('rotate180');
	        }
	        tempArea.setStyle({
	            left:calcLeftArea  + 'px',
	            top: calcTop + 'px'
	        });
	        $('chips_holder').insert(tempArea);
	        $('chips_holder').insert(temp);
	        //}    
	    }*/
	    
	    //get the last piece of the nn point, so it's dragged in pointDragging()
	    ArrayList<Piece> piecesInPoint = is_bar ? barImages[nn] : pointImages[nn];
	    dragging_piece = piecesInPoint.get(piecesInPoint.size() - 1);
	    //dragging_piece.setStyle({zIndex: uniqid}); uniqid++;
	    //dragging_piece.setStyle({height:'50px'});
	    //dragging_piece.setStyle({width:'50px'});
	    dragging_frames = 0;
	    piece_original_top = dragging_piece.t;
	    piece_original_left = dragging_piece.l;
	    isSelected = true;
	}
	
	public void pointDragging(float x, float y) {
	    if (dragging_piece != null) {
	        int maxFrames = 10;
	        int calcLeft = (int) (x - 12);
	        int calcTop = (int) (y - 12);
	        if (dragging_frames < maxFrames) {
	            float temp = 1 - ((float) dragging_frames / (float) maxFrames);
	            temp = 1 - temp;
	            debug2(String.valueOf(temp));
	            int from_left = dragging_piece.l;
	            int from_top = dragging_piece.t;
	            calcLeft = from_left + Math.round((calcLeft - from_left) * temp);
	            calcTop = from_top + Math.round((calcTop - from_top) * temp);
	            dragging_frames++;
	        }
	        debug2(calcTop + ", " + calcLeft);
	        dragging_piece.setStyle(calcTop, calcLeft);
	    }
	}

	public void pointDragEnd(boolean is_bar, int nn) {
	    if (dragging_piece != null) {
	        /*//delete semitransparent pieces
	        $$('img.possible').each(Element.remove);
	        $$('img.triangle').each(Element.remove);
	        */
	        //calculate the landing point
	        int landing = -1; //landing is the point # of where the drag ended
	    
	        //int xx = dragging_piece.l + 12;
	        //int yy = dragging_piece.t + 12;
	        
	        if (!is_bar) landing = nn;
//	        debug('point:'+possiblePoints[0][0])
	        
	        /*
	        if (yy >= 360 && yy <= (360+25)) {
	            //bottom (0 and 25)
	            if (xx >= 10 && xx < (10+140)) landing = 0;
	            else if (xx >= 150 && xx < (150+140)) landing = 25
	        } else if (xx >= 10 && xx < (10+120)) {
	            //left
	            if (yy >= 10 && yy < (10+150)) {
	                //top (7 to 12)
	                landing = 7 + Math.floor((10+150-yy)/25);
	            } else if (yy >= 200 && yy < (200+150)) {
	                //bottom (1 to 6)
	                landing = 1 + Math.floor((200+150-yy)/25);
	            }
	        } else if (xx >= 170 && xx < (170+120)) {
	            //right
	            if (yy >= 10 && yy < (10+150)) {
	                //top (13 to 18)
	                landing = 13 + Math.floor((yy-10)/25);
	            } else if (yy >= 200 && yy < (200+150)) {
	                //bottom (19 to 24)
	                landing = 19 + Math.floor((yy-200)/25);
	            }
	        }*/
	        debug("LANDING IN " + landing);
	        //Closer Feature
	        
	        //@TODO: Implement maximum distance
	        
	        /*
	        if(config['automove'] == 'true'){ 
	            if(possiblePoints.length == 1){
	                debug('one option');
	                var max = config['maxdiff'];
	                var landingPoint = getCoordenate(possiblePoints[0][0]);
	                var diff = pitagoras(pp,landingPoint);
	                        debug('diference : '+ diff);
	                if(diff < max)
	                landing    = possiblePoints[0][0];
	            }
	            if(possiblePoints.length == 2){
	                debug('two option');
	                landing = getCloser(possiblePoints,pp);
	            }
	        }*/
	        if (landing != -1) {
	            //see if it's in the possible points list
	            for (int i = 0; i < possiblePoints.size(); i++) {
	                if (landing == possiblePoints.get(i).x) {
	                    //it's good!
	                    //see if it's necessary to switch the dice
	                    for (int j = 0; j < possiblePoints.get(i).y; j++) {
	                        if (dice[MoveType][j] > 0) {
	                            debug("dice switched");
	                            SwitchDice(MoveType, 1);
	                            break;
	                        }
	                    }
	            //do the move
	            if (global_is_bar){
	                ClickBar(global_nn_bar, 1);
	            }else{
	                ClickPoint(global_nn, 1);
	                }
	            isSelected = false;
	            return;
	            }
	            }
	        }
	        //not good. just return the piece to its original position
	        //dragging_piece.setStyle({height:'25px'});
	        //dragging_piece.setStyle({width:'25px'});
	        animatePiece(dragging_piece, dragging_piece.l, dragging_piece.t, piece_original_left, piece_original_top);
	        
	        dragging_piece = null;
	        isSelected = false;
	    }
	}

	private void animatePiece(Piece piece, int from_left, int from_top,
			int to_left, int to_top) {
		///TODO: animation
		piece.setStyle(to_left, to_top);
		
	}
}