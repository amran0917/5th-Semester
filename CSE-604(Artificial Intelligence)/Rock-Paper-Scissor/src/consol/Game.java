package consol;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;
import java.util.Scanner;




public class Game {
	
	public int [][] transitionModel;
	public State previous = null;
	public int no_ofMove=0;
	private static final Random rand = new Random();
	  private static DecimalFormat DECIMAL_FORMATTER = new DecimalFormat(".##");

	public int[] result = new int[] {0, 0, 0};  //User win,lose,tie
	public Game()
	{
		
	}
	
	  public void updateTransitionModel(State previous, State next) {
		    transitionModel[previous.ordinal()][next.ordinal()]++;
		  }
	public State nextMove(State prev)
	{
		//First move wherer no need markobe model
		if(no_ofMove<1)
		{
			//Randomely chosen
			return State.values()[rand.nextInt(State.values().length)];
		}
		
		int nextIndex=0;
		
		for (int i = 0; i < State.values().length; i++) {
		      int prevIndex = prev.ordinal();
		      if(transitionModel[prevIndex][i]>transitionModel[prevIndex][nextIndex])
		      {
		    	  nextIndex=i;
		      }
		}
		
		// Item probably played by the user is in nextIndex
	    State predictedNext = State.values()[nextIndex];

	    // we choose amongst state for which this probably state loses
	    List<State> losesTo = predictedNext.losesTo;
	    return losesTo.get(rand.nextInt(losesTo.size()));
		
	}
	
	public void initialize()
	{
		int length;
		length=State.values().length;
		
		transitionModel = new int[length][length];
		
		for(int i=0;i<length;i++)
		{
			for(int j=0;j<length;j++)
			{
				transitionModel[i][j]=0;
			}
		}
	}
	
	public void play()
	{
		initialize();
		
		System.out.println("Make your Choice:\n");
		Scanner input = new Scanner(System.in);
		
		
		while(input.hasNextLine())
		{
			String input2=input.nextLine();
			
			if(input2.equals("STOP")) break;
			
			//user choice read from  state
			
			 State u_choice = null;

		      try {
		        u_choice = State.valueOf(input2.toUpperCase());
		      } catch (Exception e) {
		        System.out.println(" choice is Invalid!!!");
		        
		      }
		      
		      //Read AI choice 
		      State compChoice = nextMove(previous);
		      no_ofMove++;
			
		      //Update transition
		      
		      
		      if(previous!=null)
		      {
		    	  updateTransitionModel(previous,u_choice);
		    	  	System.out.println("Updating matrix\n");
		          for (int i = 0; i < State.values().length; i++) 
		          {
		          	for(int j = 0; j < State.values().length; j++)
		          	{
		          		System.out.print(transitionModel[i][j]+"\t" );
		          	}
		          	System.out.println("\n");
		          }

		      }
		      
		      previous=u_choice;
		      System.out.println("Computer choice : " + compChoice);
		      if(compChoice.equals(u_choice))
		      {
		    	  System.out.println("----TIE----\n");
		    	  result[2]++;
		      }
			
		      else if(compChoice.losesTo(u_choice))
		      {
		    	  System.out.println("----BEPARNAH ZITCHEN ARKI----\n");
		    	  result[0]++;
		      
		    	  
		      }
		      
		      else
		      {
		    	  System.out.println("----MOn kharap ken HArchen ARKI----\n");
		    	  result[1]++;
		      }
		      
		      System.out.println("MAKe your choice:\n");
		}
		
		input.close();
		
		System.out.println("Winning probability\n");
		
		int total=result[0]+result[1]+result[2];
		System.out.println("USER Win: " + result[0] + " - " + 
		          DECIMAL_FORMATTER.format(result[0] / (float) total * 100f) + "%");
	    System.out.println("Tie : " + result[2] + " - " + 
		          DECIMAL_FORMATTER.format(result[2] / (float) total * 100f) + "%");
		 System.out.println("Computer Win : " + result[1] + " - " + 
		          DECIMAL_FORMATTER.format(result[1] / (float) total * 100f) + "%");
		
	
	}

}
