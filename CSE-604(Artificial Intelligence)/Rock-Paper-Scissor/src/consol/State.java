package consol;

import java.util.Arrays;
import java.util.List;

public enum State {
	
	ROCK,PAPER,SCISSORS;
	
	public List<State>losesTo;
	
	public boolean losesTo(State s)
	{
		return losesTo.contains(s);
	}
	
	static {
		ROCK.losesTo=Arrays.asList(PAPER);
		PAPER.losesTo=Arrays.asList(SCISSORS);
		SCISSORS.losesTo=Arrays.asList(ROCK);
		
	}
	
}
