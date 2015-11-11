package classesGerais;
import java.util.ArrayList;

import weka.attributeSelection.InfoGainAttributeEval;
import weka.core.Instances;


public class FilterAtrib {

	private FilterAtrib() {}
	
	public static Instances selectAttributes(Instances data , int attAmount){
		
		Instances newData = new Instances(data);
		
		ArrayList<Node> list = new ArrayList<Node>();
		
		InfoGainAttributeEval eval = new InfoGainAttributeEval();
			
		try{
		
			eval.buildEvaluator(data);
	
			for(int i = 0 ; i < data.numAttributes() ; i++){
				
				list.add(new Node(i , eval.evaluateAttribute(i)));
				
			}
			
			list = sort(list);
			
			double threshold = list.get(attAmount - 1).getRank();
			
			for(int i = (data.numAttributes()-1) ; i >= 0 ; i--){
				
				if( data.attribute(i) != data.classAttribute() && eval.evaluateAttribute(i)  < threshold )
				
					newData.deleteAttributeAt(i);
				
			}
			
			System.out.println(data.numAttributes() + "\t" + newData.numAttributes());
			
		} catch (Exception e) {
			
			System.out.println(e.getMessage() + "\n\n\nProblemas na seleção de atributos.");
			
		}
		
		return newData;
		
	}
	
	private static ArrayList<Node> sort(ArrayList<Node> list){
		
		ArrayList<Node> aux = new ArrayList<Node>();
		
		int size = list.size();
		
		for(int i = 0 ; i < size ; i ++){
			
			int index = findBestIndex(list);
			
			aux.add(list.remove(index));
			
		}
		
		return aux;
		
	}
	
	private static int findBestIndex(ArrayList<Node> list){
		
		int index = 0;
		
		Node node = list.get(0);
		
		for(int i = 1 ; i < list.size() ; i++){
			
			Node currentNode = list.get(i);
			
			if( currentNode.getRank() > node.getRank() ){
				
				index = i;
				node = currentNode;
				
			}
			
		}
		
		return index;
		
	}

}
