import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Stack;


public class CreateThread extends Thread{
	
	private Socket socket;

	CreateThread(Socket socket){
		this.socket = socket;
	}
	
	public void run(){
		System.out.println("Connection from: "+socket.getInetAddress().getHostAddress()+" : "+socket.getPort());
		try{
			// Server connection (input/output) to the created Socket

			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter output = new PrintWriter(socket.getOutputStream(),true);
			
			while(true){
				String in = input.readLine();
				output.println("Matematical expression "+in+" is sucessfully sent to server.");
				
				int cost = calculateCost(in);
				
				int openParantheses = countOpenParantheses(in);
				int closeParantheses = countCloseParantheses(in);
				int totalParantheses = openParantheses+closeParantheses;
				
				String[] infixArray = splitInfix(in);
				String[] postfixArray = infixToPostfix(infixArray,totalParantheses);
				
				String answer = calculate(postfixArray);
				output.println("The servers answer is: "+in+" = "+answer+" The server will take a cost of 10:- for each operator. The expression "+in+" costs: "+cost+":-");
				
				
			}
		}
		catch(IOException ioe){
			ioe.printStackTrace();
			System.err.println("IOFEL 2");
		}
	}
	
	public int calculateCost(String in){
		int cost = 0;
		for(int i =0; i<in.length();i++){
			if(isOperand(String.valueOf(in.charAt(i)))==false){
				cost=cost+10;
			}
		}
		return cost;
	}
	
	
	// This algorithm splits the string per each number and operator, including two digit numbers.
	public String[] splitInfix(String infix){
		ArrayList<String> inputList = new ArrayList<String>();
		String str="";
		
		for (int i = 0; i < infix.length(); i++) {
		    String s = String.valueOf(infix.charAt(i));
		    if( s.equals("+") || s.equals("-") ||s.equals("*")){
		    	if(str.equals("")){
		    		inputList.add(s);
		    	}
		    	else {
		    		inputList.add(str);
		    		inputList.add(s);
		    		str="";
		    	}
		    }
		    else if(s.equals("(") ||s.equals(")") ){
		    	if(s.equals("(")){
			    	inputList.add(s);
			    	
		    	}
		    	else if(s.equals(")")){
		    		inputList.add(str);
			    	inputList.add(s);
			    	str="";
		    	}
		    }
		    else{
		    	str=str + s;
		    }
		}
		inputList.add(str);
		int size = inputList.size();
		for(int i = 0 ; i<size; i++){
			
			if(inputList.get(i)==""){
				inputList.remove(i);
				size--;
				}
		}
		String[] infixArray = new String[inputList.size()];
		for(int i = 0; i<infixArray.length;i++){
			infixArray[i]=inputList.get(i);
		}
		
		return infixArray;
	}
	
	public String calculate(String[] postfixArray){
		Stack<String> stack = new Stack<String>();
//		for(int i = 0; i<postfixArray.length;i++){
//			System.out.println(postfixArray[i]);
//		}
		for(int i=0; i<postfixArray.length; i++){
			if(isOperand(postfixArray[i])==true){
				stack.push(postfixArray[i]);
			}
			else{
				if(postfixArray[i].equals("+")){
					String pop1 = stack.pop();
					String pop2 = stack.pop();
					stack.push(add(pop2,pop1));
				}
				if(postfixArray[i].equals("-")){
					try{
						if(postfixArray[i+1].equals("-")){
							String pop1 = stack.pop();
							String pop2 = stack.pop();
							stack.push(add(pop2,pop1));
						}
						else{
							String pop1 = stack.pop();
							String pop2 = stack.pop();
							stack.push(subtract(pop2,pop1));
						}
					}
					catch(ArrayIndexOutOfBoundsException e){
						String pop1 = stack.pop();
						String pop2 = stack.pop();
						stack.push(subtract(pop2,pop1));
						e.printStackTrace();
					}
					
				}
				if(postfixArray[i].equals("*")){
					String pop1 = stack.pop();
					String pop2 = stack.pop();
					stack.push(multiplie(pop2,pop1));
				}
			}
		}
		String answer = topValueOfStack(stack);
		return answer;
	}
	
	
	public String add(String pop2,String pop1){
		int first = Integer.parseInt(pop2);
		int second = Integer.parseInt(pop1);
		int added = first+second;
		String answer = Integer.toString(added);
		return answer;
		
	}
	
	public String subtract(String pop2, String pop1){
		int first = Integer.parseInt(pop2);
		int second = Integer.parseInt(pop1);
		int subtracted = first-second;
		String answer = Integer.toString(subtracted);
		return answer;
	}
	
	public String multiplie(String pop2, String pop1){
		int first = Integer.parseInt(pop2);
		int second = Integer.parseInt(pop1);
		int multiplied = first*second;
		String answer = Integer.toString(multiplied);
		return answer;
	}
	
	public int countOpenParantheses(String infix){
		int count = 0;
		for(int i=0; i<infix.length();i++){
			if(infix.charAt(i)=='('){
				count++;
			}
		}
		return count;
	}
	
	public int countCloseParantheses(String infix){
		int count = 0;
		for(int i = 0 ; i<infix.length();i++){
			if(infix.charAt(i)==')'){
				count++;
			}
		}
		return count;
	}
	
	public String[] infixToPostfix(String[] infixArray, int parantheses){
		Stack<String> stack = new Stack<String>();
		String[] postfixArray = new String[(infixArray.length)-parantheses];
		int k = 0;
		for(int i=0; i<infixArray.length;i++){
//			System.out.println("Ny loop");
//			System.out.println("i: "+i);
//			System.out.println("k: " +k);
			if(isOperand(infixArray[i])==true){
				postfixArray[k] = infixArray[i];
//				System.out.println("postfixarray av k: "+postfixArray[k]);
				k++;
			}
			else if(isOperand(infixArray[i])==false && (infixArray[i].equals("(")==false && infixArray[i].equals(")"))==false){
				if(stack.empty()==true || (stack.empty()==false && topValueOfStack(stack).equals("("))){
					System.out.println("infixarray av i: "+infixArray[i]);
					stack.push(infixArray[i]);
					System.out.println(stack);
				
				}
				else{
					if(checkHigherPriority(topValueOfStack(stack),infixArray[i])==false){
						System.out.println("Kommer in hit");
						stack.push(infixArray[i]);
						System.out.println(stack);
					
					}
					else{
						System.out.println("Kommer in i else hŠr");
						while(stack.empty()==false && checkHigherPriority(topValueOfStack(stack),infixArray[i])==true){
							postfixArray[k] = stack.pop();
							System.out.println("postfixarray av k: "+postfixArray[k]);
							k++;
							}
						//k++;
						stack.push(infixArray[i]);
						System.out.println(stack);
						}
					}	
				
			}
			else if(infixArray[i].equals("(")){
				System.out.println("infixarray av i inne i (: "+infixArray[i]);
				stack.push(infixArray[i]);
				System.out.println(stack);
			}
			else if(infixArray[i].equals(")")){
				System.out.println("infixarray av i inne i ): "+infixArray[i]);
				System.out.println(stack);
				while(stack.empty()==false && topValueOfStack(stack).equals("(")==false){
					postfixArray[k] = stack.pop();
					System.out.println(stack);
					System.out.println("postfixarray av i inne i ( och inne i while: "+postfixArray[k]);
					k++;
					System.out.println("k Šr nu: "+k);
				}
				stack.pop();
				System.out.println(stack);
			}
		}//empties the stack, pops everything left in stack to postfixArray.
		System.out.println("kommer till sista steget!");
		while(stack.empty()==false){
			System.out.println("kommer in i sista while satsen");
			postfixArray[(postfixArray.length)-(stack.size())]=stack.pop();
			// detta va inne i postfixarray argumentet(postfixArray.length)-(stack.size())
		}
		
		return postfixArray;
		
	}
	
	public boolean isOperand(String character){
		if(character.equals("+") || character.equals("-") || character.equals("*") || character.equals("(") || character.equals(")")){
			return false;
		}
		else{
			return true;
		}
	}
	
	public String topValueOfStack(Stack s){
		String topValue = (String) s.pop();
		s.push(topValue);
		return topValue;
	}
	
	public boolean checkHigherPriority(String stackValue, String charValue){
		if(stackValue.equals("*") && (charValue.equals("+") || charValue.equals("-"))){
			return true;
		}
		else{
			return false;
		}
	}
}
