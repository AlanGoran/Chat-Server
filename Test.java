import java.util.ArrayList;


public class Test {
	public static void main(String[] args){
		ArrayList<String> inputList = new ArrayList<String>();
		String str="";
		String infix = "he";
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
		
		
	}
}

