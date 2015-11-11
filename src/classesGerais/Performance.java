package classesGerais;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.core.Instances;


public class Performance {

	private Performance() {}
	
	public static void getPerformance(Instances data , int times){
		
		weka.classifiers.functions.SMO scheme = new weka.classifiers.functions.SMO();

		try{
 
			scheme.setOptions(weka.core.Utils.splitOptions("-C 1.0 -L 0.0010 -P 1.0E-12 -N 0 -V -1 -W 1 -K \"weka.classifiers.functions.supportVector.PolyKernel -C 250007 -E 1.0\""));
	 
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
				eval.crossValidateModel(scheme, data, 10, new Random(1));
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
