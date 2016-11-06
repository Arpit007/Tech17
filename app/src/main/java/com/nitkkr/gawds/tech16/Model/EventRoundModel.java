package com.nitkkr.gawds.tech16.Model;

/**
 * Created by Home Laptop on 05-Nov-16.
 */

public class EventRoundModel
{
	public interface RoundModelListener
	{
		void RoundStatusChanged(RoundStatus status);
	}

	public enum RoundStatus
	{
		None,
		Live,
		Over,
		Delayed,
		Pause,
		Upcoming
	}

	private int Rounds;
	private int currentRound;
	private RoundStatus currentRoundStatus = RoundStatus.None;
	private RoundModelListener listener;

	public void setRoundModelListener(RoundModelListener roundModelListener)
	{
		listener = roundModelListener;
	}

	public int getRounds()
	{
		return Rounds;
	}

	public int getCurrentRound()
	{
		return currentRound;
	}

	public RoundStatus getCurrentRoundStatus()
	{
		return currentRoundStatus;
	}

	public void setRounds(int rounds)
	{
		Rounds = rounds;
		currentRound = 0;
		currentRoundStatus = RoundStatus.None;
		if (listener != null)
		{
			listener.RoundStatusChanged(currentRoundStatus);
		}
	}

	void setCurrentRound(int currentRound)
	{
		this.currentRound = currentRound;
		currentRoundStatus = RoundStatus.None;
		if (listener != null)
		{
			listener.RoundStatusChanged(currentRoundStatus);
		}
	}

	public void setCurrentRoundStatus(RoundStatus status)
	{
		currentRoundStatus = status;
		if (listener != null)
		{
			listener.RoundStatusChanged(currentRoundStatus);
		}
	}

	public void setRoundLive()
	{
		if (currentRound == 0)
		{
			currentRound = 1;
		}
		if (currentRoundStatus == RoundStatus.Over)
		{
			currentRound++;
		}
		currentRoundStatus = RoundStatus.Live;
		if (listener != null)
		{
			listener.RoundStatusChanged(currentRoundStatus);
		}
	}

	public void setRoundOver()
	{
		currentRoundStatus = RoundStatus.Over;
		if (listener != null)
		{
			listener.RoundStatusChanged(currentRoundStatus);
		}
	}

	public void setNextRound()
	{
		if (currentRoundStatus == RoundStatus.Over & !isFinalRound())
		{
			currentRound++;
			currentRoundStatus = RoundStatus.Upcoming;
		}
		if (listener != null)
		{
			listener.RoundStatusChanged(currentRoundStatus);
		}
	}

	public boolean isFinalRound()
	{
		return currentRound == Rounds;
	}

}
