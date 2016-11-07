package com.nitkkr.gawds.tech16.Model;

import java.util.ArrayList;

/**
 * Created by Home Laptop on 07-Nov-16.
 */

public class RoundResultModel
{
	private int RoundNumber;
	private ArrayList<ResultPositionModel> WinningPosition;

	public int getRoundNumber(){return RoundNumber;}
	public ArrayList<ResultPositionModel> getWinningPosition(){return WinningPosition;}

	public void setRoundNumber(int roundNumber){RoundNumber = roundNumber;}
	public void setWinningPosition(ArrayList<ResultPositionModel> winningPosition){WinningPosition=winningPosition;}
}
