package mestrado_08_06_2015;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.core.Instances;
import classesGerais.FilterAtrib;

public class Resultados {

	private Resultados() {}
	
	public static void main(String[] args){
		
		String[] filePaths = {"C:/Users/Artur/Desktop/Universidad/Mestrado_Mamografia/ConvIRMA novo/Taxa de acerto/PadroesIRMA-II-WA-ZE.arff" , 
							  "C:/Users/Artur/Desktop/Universidad/Mestrado_Mamografia/ConvIRMA novo/Taxa de acerto/PadroesIRMA-II-WM-ZE.arff" , 
							  "C:/Users/Artur/Desktop/Universidad/Mestrado_Mamografia/ConvIRMA novo/Taxa de acerto/PadroesIRMA-III-WA-ZE.arff" , 
							  "C:/Users/Artur/Desktop/Universidad/Mestrado_Mamografia/ConvIRMA novo/Taxa de acerto/PadroesIRMA-III-WM-ZE.arff" , 
							  "C:/Users/Artur/Desktop/Universidad/Mestrado_Mamografia/ConvIRMA novo/Taxa de acerto/PadroesIRMA-IV-WA-ZE.arff" , 
							  "C:/Users/Artur/Desktop/Universidad/Mestrado_Mamografia/ConvIRMA novo/Taxa de acerto/PadroesIRMA-IV-WM-ZE.arff"};
		
		
		for(String s : mainMLP(filePaths)){
			
			System.out.println(s);
			
		}
		
		
		
	} 
	
	public static String[] mainMLP(String[] args){
		
		LinkedList<String> results = new LinkedList<String>();
		
		for(String filePath : args){

			String result = "";
			
			System.out.println(filePath);
			
			Instances data = null;
			
			try{
				
				FileReader file = new FileReader(filePath);
				
				BufferedReader reader = new BufferedReader(file);

				data = new Instances(reader);
				 
				reader.close();
				
				data.setClassIndex(data.numAttributes() - 1);

				
			} catch (Exception e) {
			
				System.out.println("Arquivo não encontrado");
				
				return null;
				
			}
	 
			MultilayerPerceptron mlp1 = new MultilayerPerceptron();
			MultilayerPerceptron mlp2 = new MultilayerPerceptron();

			System.out.print("\nFilt ... ");
				
			Instances newData = FilterAtrib.selectAttributes(data, 100);
			
			System.out.print("OK\n");
			
			try{

				System.out.print("Build 1 ... ");
				
				mlp1.buildClassifier(data);
				
				System.out.print("OK\nBuild 2 ... ");
				
				mlp2.buildClassifier(newData);
				
				System.out.print("OK\n");
				
				System.out.print("Eval 1 ... ");
				
				Evaluation eval1 = new Evaluation(data);
				
				System.out.print("OK\nEval 2 ... ");
				
				Evaluation eval2 = new Evaluation(newData);
				
				System.out.print("OK\n");
				
				System.out.print("\tMLP 1\t");
				
				long init1 = System.nanoTime();
				eval2.crossValidateModel(mlp2, newData, 10, new Random(1));
				long end1 = System.nanoTime();
				System.out.print("OK\n\tMLP 2\t");
				long init2 = System.nanoTime();
				eval1.crossValidateModel(mlp1, data, 10, new Random(1));
				long end2 = System.nanoTime();
				System.out.println("OK");
				
				result += (eval1.toSummaryString("\nResults\n======\n", false));
				result += (eval1.toClassDetailsString());
				result += (eval1.toMatrixString());
				
				result += (eval2.toSummaryString("\nResults\n======\n", false));
				result += (eval2.toClassDetailsString());
				result += (eval2.toMatrixString());
				
				result += (data.numAttributes() + " " + newData.numAttributes());
				
				result += ("\nTempo NewData = " + (end1 - init1) + "\nTempo Data = " + (end2 - init2) );
				
				
			} catch (Exception e){
				
				System.out.println("Erro na avaliação");
			}
			
			results.add(result);

			
		}
		
		String[] output = new String[results.size()];
		
		int i = 0;
		
		for(String s : results){
			
			output[i++] = s;
			
		}
		
		return output;
		
	}

	
	public static String[] mainSVM(String[] args){
		
		LinkedList<String> results = new LinkedList<String>();
		
		for(String filePath : args){

			String result = "";
			
			System.out.println(filePath);
			
			Instances data = null;
			
			try{
				
				FileReader file = new FileReader(filePath);
				
				BufferedReader reader = new BufferedReader(file);

				data = new Instances(reader);
				 
				reader.close();
				
				data.setClassIndex(data.numAttributes() - 1);

				
			} catch (Exception e) {
			
				System.out.println("Arquivo não encontrado");
				
				return null;
				
			}
	 
			SMO scheme = new SMO();
			
			try{
				 
				scheme.setOptions(weka.core.Utils.splitOptions("-C 1.0 -L 0.0010 -P 1.0E-12 -N 0 -V -1 -W 1 -K \"weka.classifiers.functions.supportVector.PolyKernel -C 250007 -E 1.0\""));
		 		
			} catch (Exception e) {
		 
				System.out.println("Erro na configuração do classificador");
				return null;
		 
			}


				
			Instances newData = FilterAtrib.selectAttributes(data, 100);
			
			
			try{
				
				Evaluation eval1 = new Evaluation(data);
				Evaluation eval2 = new Evaluation(newData);
				
				System.out.print("\tSVM 1\t");
				
				long init1 = System.nanoTime();
				eval2.crossValidateModel(scheme, newData, 10, new Random(1));
				long end1 = System.nanoTime();
				System.out.print("OK\n\tSVM 2\t");
				long init2 = System.nanoTime();
				eval1.crossValidateModel(scheme, data, 10, new Random(1));
				long end2 = System.nanoTime();
				System.out.println("OK");
				
				result += (eval1.toSummaryString("\nResults\n======\n", false));
				result += (eval1.toClassDetailsString());
				result += (eval1.toMatrixString());
				
				result += (eval2.toSummaryString("\nResults\n======\n", false));
				result += (eval2.toClassDetailsString());
				result += (eval2.toMatrixString());
				
				result += (data.numAttributes() + " " + newData.numAttributes());
				
				result += ("\nTempo NewData = " + (end1 - init1) + "\nTempo Data = " + (end2 - init2) );
				
				
			} catch (Exception e){
				
				System.out.println("Erro na avaliação");
			}
			
			results.add(result);

			
		}
		
		String[] output = new String[results.size()];
		
		int i = 0;
		
		for(String s : results){
			
			output[i++] = s;
			
		}
		
		return output;
		
	}

	
}
