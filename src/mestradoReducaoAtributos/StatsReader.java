package mestradoReducaoAtributos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class StatsReader {

	public static String resultsPath = "C:/Users/Artur/Desktop/Universidad/Mestrado_Mamografia/ConvIRMA novo/Taxa de acerto/";
	public static String statsPath = resultsPath + "Stats/Vseed/";
	
	public static void main(String[] args){
		
		String[] fileNames = {	"PadroesIRMA-I-WA-ZE"   , 
	  			  				"PadroesIRMA-I-WM-ZE"   ,
	  			  				"PadroesIRMA-II-WA-ZE"  , 
	  			  				"PadroesIRMA-II-WM-ZE"  , 
	  			  				"PadroesIRMA-III-WA-ZE" , 
	  			  				"PadroesIRMA-III-WM-ZE" , 
	  			  				"PadroesIRMA-IV-WA-ZE"  , 
	  			  				"PadroesIRMA-IV-WM-ZE"  
				  			  };

		String[] algoritmosDeBusca = {"GEN" , "PSO" , "EVO" , "BEST"};
		
		String evaluatorName = "CFS";

		String classifierName = "SMO";
		
		statsPath += classifierName + "_" + evaluatorName + "/";
		
		System.out.println(statsPath);
		
		for( String name : fileNames){
			
			String[][][] info = new String[4][30][3]; // algoritmos - repeticoes - stats
			
			for(int j = 1; j < 31 ; j++){
				
				File f = new File(statsPath + name + "_Stats_" + classifierName + "_" + evaluatorName + "_" + j + ".txt" );
		
				try{
					
					FileReader fr = new FileReader(f);
					
					BufferedReader br = new BufferedReader(fr);
					
					String aux = null;
					
					int index = 0;
					
					while(( aux = br.readLine()) != null){
						
						if(aux.equals(algoritmosDeBusca[0]))
							index = 0;
						else if(aux.equals(algoritmosDeBusca[1]))
							index = 1;
						else if(aux.equals(algoritmosDeBusca[2]))
							index = 2;
						else if(aux.equals(algoritmosDeBusca[3]))
							index = 3;
						
						
						
						if(aux.length() > 23 && aux.substring(0, 23).equals("Data set attributes -> ")){
							
							info[index][j-1][0] = aux.substring(23).split(" ")[0];
							
						}
						
						else if(aux.length() > 15 && (aux.substring(0, 15).equals("Training time: "))){
							
							String s = aux.substring(15);
							
							info[index][j-1][1] = s.substring(0, s.length()-2);
							
						}
						
						else if(aux.length() > 15 && aux.substring(0, 15).equals("Correctness -> ")){
							
							info[index][j-1][2] = aux.substring(15,aux.length()-2);
							
						}
						
					}
					
					br.close();
					
				} catch(Exception e){
					
					e.printStackTrace();
					
				}
				
			}
			
			try{
				

				File f0 = new File(statsPath + name + "_Stats_" + classifierName + "_" + evaluatorName + "_GEN.csv");
				File f1 = new File(statsPath + name + "_Stats_" + classifierName + "_" + evaluatorName + "_PSO.csv");
				File f2 = new File(statsPath + name + "_Stats_" + classifierName + "_" + evaluatorName + "_EVO.csv");
				File f3 = new File(statsPath + name + "_Stats_" + classifierName + "_" + evaluatorName + "_BEST.csv");
				
				BufferedWriter[] bwW = new BufferedWriter[4];
				
				bwW[0] = new BufferedWriter(new FileWriter(f0));
				bwW[1] = new BufferedWriter(new FileWriter(f1));
				bwW[2] = new BufferedWriter(new FileWriter(f2));
				bwW[3] = new BufferedWriter(new FileWriter(f3));
				
				
				for(int ii = 0 ; ii < 4 ; ii++){
				
					String[][] infoAux = info[ii];
					
					for(String[] vec : infoAux){
						
						for(String s : vec){
							
							bwW[ii].write(s + ";");
							
						}
					
						bwW[ii].write("\r\n");
					}
					
					bwW[ii].close();
					
				}
				
				
			} catch(Exception e){
				
				e.printStackTrace();
				
			}
			
		}
	}
	
	
}
