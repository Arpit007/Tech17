package com.nitkkr.gawds.tech16.Model;

import java.util.ArrayList;

/**
 * Created by Home Laptop on 05-Nov-16.
 */

public class RoundResultModel
{
	public class ResultPosition
	{
		int Position;
		ArrayList<UserModel> Winners=new ArrayList<>();

		public ResultPosition(int position, ArrayList<UserModel> winners)
		{
			Position=position;
			Winners=winners;
		}
		public int getPosition(){return Position;}
		public ArrayList<UserModel> getWinners(){return Winners;}
	}

	private int roundNumber;
	private ArrayList<ResultPosition> RoundResult;

	public RoundResultModel(int RoundNumber, ArrayList<ResultPosition> result)
	{
		roundNumber =RoundNumber;
		RoundResult=result;
	}

	public int getRoundNumber(){return roundNumber;}
	public ArrayList<ResultPosition> getRoundResult(){return RoundResult;}

	public void setRoundNumber(int roundNumber){this.roundNumber = roundNumber;}
	public void setRoundResult(ArrayList<ResultPosition> roundResult){RoundResult=roundResult;}
}
