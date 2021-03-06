package mestradoReducaoAtributos;



import java.io.BufferedWriter; 
import java.io.File;
import java.io.FileWriter;
import java.util.Random;

import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.ASSearch;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.BestFirst;
import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.EvolutionarySearch;
import weka.attributeSelection.GeneticSearch;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.PSOSearch;
import weka.attributeSelection.Ranker;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.core.Instances;




public class Main {

	public static final int numToSelect = 80;
	
	public static int currentNumToSelect = numToSelect;
	
	public static final int repetitions = 30;
	
	public static String resultsPath = "C:/Users/Artur/Desktop/Universidad/Mestrado_Mamografia/ConvIRMA novo/Taxa de acerto/";
	
	public static String statsPath = resultsPath + "Stats/VSeed/";
	
	
	public static void main(String[] args){
		
		String[] fileNames = {"PadroesIRMA-I-WA-ZE"   , 
				  			  "PadroesIRMA-I-WM-ZE"   ,
				  			  "PadroesIRMA-II-WA-ZE"  , 
				  			  "PadroesIRMA-II-WM-ZE"  , 
							  "PadroesIRMA-III-WA-ZE" , 
							  "PadroesIRMA-III-WM-ZE" , 
							  "PadroesIRMA-IV-WA-ZE"  , 
				  			  "PadroesIRMA-IV-WM-ZE"  
							  };
 		
		String[] algoritmosDeBusca = { "GEN" , "PSO" , "EVO" , "BEST"}; //{"RANK"}; 
		
		ASSearch[] search = initializeSearchVector(algoritmosDeBusca);
		
		ASEvaluation cfs = new CfsSubsetEval(); // PrincipalComponents pca = new PrincipalComponents();
		String evaluatorName = "CFS";

		SMO smo = null;
		String classifierName = "SMO";
		
		statsPath += classifierName+"_"+evaluatorName+"/";
		
		File f = new File(statsPath);
		
		if(!f.exists())
			f.mkdirs();
			
		
		try{
			
			smo = new SMO();
			
			smo.setOptions(weka.core.Utils.splitOptions("-C 1.0 -L 0.0010 -P 1.0E-12 -N 0 -V -1 -W 1 -K \"weka.classifiers.functions.supportVector.PolyKernel -C 250007 -E 1.0\""));
		
			for(String name : fileNames){
				
				Instances data = ReadArffFile.read(resultsPath + name + ".arff");
				
				BufferedWriter writer = new BufferedWriter(new FileWriter(statsPath + name + "_Stats_" + classifierName + "_" + evaluatorName + "_FullDataSet.txt" ));//path.substring(0,path.length()-5)+"_results_SMO_CFS.txt"));
				
				long deltaFull = evaluate(data, smo, null, null, true, writer, 0 , "Full Data Set");
				
				writer.close();
				
				Ranker rank = new Ranker();
				
				rank.setNumToSelect(numToSelect);
				
				writer = new BufferedWriter(new FileWriter(statsPath + name + "_Stats_" + classifierName + "_" + evaluatorName + "_InfoGain.txt" ));
				
				evaluate(data, smo, rank , new InfoGainAttributeEval(), false, writer, deltaFull , "Info Gain with Ranker");
	
				writer.close();
				
				for(int j = 1 ; j < (repetitions+1) ; j++){
				
					writer = new BufferedWriter(new FileWriter(statsPath + name + "_Stats_" + classifierName + "_" + evaluatorName + "_" + j + ".txt" ));
				
					int i = 0;
					
					for(ASSearch s : search)	
					
						evaluate(data, smo, s, cfs, false, writer, deltaFull , algoritmosDeBusca[i++]);
					
					writer.close();
					
				}
			
				
				if(writer != null)
					writer.close();
				
			}
			
		} catch(Exception e){
			
			System.out.println(e.getMessage());
			
			e.printStackTrace();
			
		}
				
	}
	
	public static ASSearch[] initializeSearchVector(String[] input){
		
		ASSearch[] output = new ASSearch[input.length];
		
		int i = 0;
		
		for(String s : input)
			
			if(s.equals("PSO")){
				
				PSOSearch pso = new PSOSearch();
					
				pso.setIndividualWeight(0.34);	// Padr�o 0.34
				pso.setInertiaWeight(0.33); 	// Padrao 0.33
				pso.setIterations(20); 			// Padr�o 20
				pso.setMutationProb(0.01); 		// Padr�o 0.01
				pso.setPopulationSize(20); 		// Padr�o 20
				pso.setReportFrequency(20); 	// Padr�o 20
				pso.setSocialWeight(0.33); 		// Padr�o 0.33
															
				output[i++] = pso;
			}
			else if(s.equals("GEN")){
				
				GeneticSearch gen = new GeneticSearch();
				
				gen.setCrossoverProb(0.6); 	// Padr�o 0.6
				gen.setMaxGenerations(20); 	// Padr�o 20
				gen.setMutationProb(0.033); // Padr�o 0.033
				gen.setPopulationSize(20); 	// Padr�o 20
				gen.setReportFrequency(20); // Padr�o 20
			
				output[i++] = gen;
			}
			else if(s.equals("EVO")){
				EvolutionarySearch evo = new EvolutionarySearch();
				//TODO setar parametros do algoritmo
				evo.setCrossoverProbability(0.8); 	// Padr�o 0.6
				evo.setGenerations(100); 			// Padr�o 20
				evo.setMutationProbability(0.1); 	// Padr�o 0.01
				evo.setPopulationSize(32);			// Padr�o 20
				evo.setReportFrequency(20); 		// Padr�o 20

				output[i++] = evo;
			}
			else if(s.equals("BEST")){
			
				BestFirst best = new BestFirst();

				best.setLookupCacheSize(1); // Padr�o 1
				//best.setSearchTermination(5); // Padr�o 5 Throws Exception
				
				output[i++] = best;
			}
			else if(s.equals("RANK")){
				Ranker rank = new Ranker();
				
				//rank.setGenerateRanking(true); // Padr�o true
				rank.setNumToSelect(numToSelect); // Padr�o -1
				//rank.setThreshold(-1.7976931348623157E308); // Padr�o -1.7976931348623157E308
				output[i++] = rank;
				
			}
		
		return output;
		
	}
	
	public static long evaluate(Instances data , Classifier classif , ASSearch search , ASEvaluation ev , boolean full ,  BufferedWriter writer , long deltaFull , String text){
		
		String result = "\n******************************************************************************************************************\n\n" + text + "\n";
		
		long delta = deltaFull;
		
		try{
		
			if(!full){
			
				AttributeSelection selec = new AttributeSelection();
			
				ev.buildEvaluator(data);
			
				selec.setSearch(search);
				
				selec.setEvaluator(ev);
				
				selec.SelectAttributes(data);
			
				data = selec.reduceDimensionality(data);

			}	
				
			classif.buildClassifier(data);
			
			Evaluation eval = new Evaluation(data);
			
			
			long init = System.nanoTime();
			eval.crossValidateModel(classif, data, 10, new Random(1));
			long end = System.nanoTime();
			
			delta = end - init;
			
			
			result += ("\nData set attributes -> " + (data.numAttributes()-1) + " atributes.\nTraining time: " + delta/1000000 + "ms\n\n");
			
			result += (eval.toSummaryString("\nResults\n======\n", false));
			result += (eval.toClassDetailsString());
			result += (eval.toMatrixString());
			
			long del = deltaFull - delta;
			
			if(del > 0){
				
				result += "\n\nRedu��o de " + ( (100*del)/deltaFull) + "% no tempo.\n\n";
				
			} else {
				
				result += "\n\nTempo de treinamento aumentou.\n\n";
				
			}
			
			
			writer.write(result.replaceAll("\n", "\r\n"));
			
		} catch (Exception e){
			
			if(e.getMessage().equals("More attributes requested than exist in the data")){
				
				currentNumToSelect -= 1;
				
				System.out.println("Tentando com " + currentNumToSelect+" attributes to select.");
				
				((Ranker) search).setNumToSelect(currentNumToSelect);
				
				evaluate(data, classif, search, ev, full, writer, deltaFull, text);
				
			} else 
				
				e.printStackTrace();
			
			if(currentNumToSelect != numToSelect){
				
				currentNumToSelect = numToSelect;
				
				((Ranker) search).setNumToSelect(numToSelect);
				
			}
			
		}
		
		return delta;
		
	}
	
	
}
