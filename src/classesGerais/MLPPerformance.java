package classesGerais;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;


public class MLPPerformance {

	private MLPPerformance() {}
	
	
	public static void getPerformance(Instances data , int times){
		
		MultilayerPerceptron mlp = new MultilayerPerceptron();

		try{
 
			mlp.buildClassifier(data);
	 
		} catch (Exception e) {
	 
			System.out.println("Erro na configuração do classificador");
			return;
	 
		}
		
		long sum = 0;
		long max = 0;
		long min = Long.MAX_VALUE;
		
		try{

			for(int i = 0 ; i < times ; i++){
				
				Evaluation eval = new Evaluation(data);
				
				long init1 = System.nanoTime();
				eval.crossValidateModel(mlp, data, 10, new Random(1));
				long end1 = System.nanoTime();
	
				long delta = end1 - init1;
				
				if(delta < min)
					min = delta;
				
				if(delta > max)
					max = delta;
				
				sum += delta;
				
			}
			
			System.out.println("Mínimo: " + (min) + "\nMáximo: " + (max) + "\nMédia: " + (sum/times) );
			
		} catch (Exception e){
			
			System.out.println("Erro na avaliação");
		}

		
	}


}
