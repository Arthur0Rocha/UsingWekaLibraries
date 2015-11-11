package classesGerais;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;

public class MLPMainClass {


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
		


 
		MultilayerPerceptron mlp1 = new MultilayerPerceptron();
		MultilayerPerceptron mlp2 = new MultilayerPerceptron();

			
		Instances newData = FilterAtrib.selectAttributes(data, 90);
		
		
		try{

			mlp1.buildClassifier(data);
			mlp2.buildClassifier(newData);
			
			Evaluation eval1 = new Evaluation(data);
			Evaluation eval2 = new Evaluation(newData);
			
			long init1 = System.nanoTime();
			eval2.crossValidateModel(mlp2, newData, 10, new Random(1));
			long end1 = System.nanoTime();
			eval1.crossValidateModel(mlp1, data, 10, new Random(1));
			long end2 = System.nanoTime();
			
			System.out.println(eval1.toSummaryString("\nResults\n======\n", false));
			System.out.println(eval1.toClassDetailsString());
			System.out.println(eval1.toMatrixString());
			
			System.out.println(eval2.toSummaryString("\nResults\n======\n", false));
			System.out.println(eval2.toClassDetailsString());
			System.out.println(eval2.toMatrixString());
			
			System.out.println(data.numAttributes() + " " + newData.numAttributes());
			
			System.out.println("\n\nMLP1 training time: " + mlp1.getTrainingTime() + "\nMLP2 training time: " + mlp2.getTrainingTime() 
							 + "\nTempo NewData = " + (end1 - init1) + "\nTempo Data = " + (end2 - end1) );
			
			
		} catch (Exception e){
			
			System.out.println("Erro na avaliação");
		}
		
		MLPPerformance.getPerformance(newData, 25);
		MLPPerformance.getPerformance(data, 25);
		
		
		

		
	}
	

}
