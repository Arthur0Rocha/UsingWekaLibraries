package classesGerais;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.core.Instances;


public class MainClass {

	
	public static void main(String[] args){
		
		String filePath = "C:/Users/Artur/Desktop/Universidad/Mestrado_Mamografia/ConvIRMA novo/Taxa de acerto/PadroesIRMA-III-WM-ZE.arff";
		Instances data = null;
		
		try{
			
			FileReader file = new FileReader(filePath);
			
			BufferedReader reader = new BufferedReader(file);

			data = new Instances(reader);
			 
			reader.close();
			
			data.setClassIndex(data.numAttributes() - 1);

			
		} catch (Exception e) {
		
			System.out.println("Arquivo não encontrado");
			
			return;
			
		}
		


 
		weka.classifiers.functions.SMO scheme = new weka.classifiers.functions.SMO();

		
		
		try{
 
			scheme.setOptions(weka.core.Utils.splitOptions("-C 1.0 -L 0.0010 -P 1.0E-12 -N 0 -V -1 -W 1 -K \"weka.classifiers.functions.supportVector.PolyKernel -C 250007 -E 1.0\""));
	 		
		} catch (Exception e) {
	 
			System.out.println("Erro na configuração do classificador");
			return;
	 
		}
		
			
		Instances newData = FilterAtrib.selectAttributes(data, 60);
		
		try{
		
			
			Evaluation eval1 = new Evaluation(data);
			Evaluation eval2 = new Evaluation(newData);
		
			eval1.crossValidateModel(scheme, data, 10, new Random(1));
			eval2.crossValidateModel(scheme, newData, 10, new Random(1));
			
			System.out.println(eval1.toSummaryString("\nResults\n======\n", false));
			System.out.println(eval1.toClassDetailsString());
			System.out.println(eval1.toMatrixString());
			
			System.out.println(eval2.toSummaryString("\nResults\n======\n", false));
			System.out.println(eval2.toClassDetailsString());
			System.out.println(eval2.toMatrixString());
			
			System.out.println(data.numAttributes() + " " + newData.numAttributes());
			
		} catch (Exception e){
			
			System.out.println("Erro na avaliação");
		}
		
		Performance.getPerformance(newData, 50);
		Performance.getPerformance(data, 50);
		
		
		
	}
	
}
