package mestradoTesteSeed;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class OrganizaCSV {

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
		
		double[][][] media = new double[4][8][2];
		double[][][] desvio = new double[4][8][2];
		
		int n = 0;
		
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
			
			for(int l = 0 ; l < 4 ; l++){
				
				for(int c = 0 ; c < 30 ; c++){
					
					media[l][n][0] += Double.parseDouble(info[l][c][2])/30;
					media[l][n][1] += Double.parseDouble(info[l][c][0])/30;
					
				}
				
				
			}
		
			for(int l = 0 ; l < 4 ; l++){
				
				for(int c = 0 ; c < 30 ; c++){
					
					desvio[l][n][0] += Math.pow((Double.parseDouble(info[l][c][2]) - media[l][n][0]) , 2)/29;
					desvio[l][n][1] += Math.pow((Double.parseDouble(info[l][c][0]) - media[l][n][1]) , 2)/29;
					
				}
				
				media[l][n][0] = media[l][n][0]/(6.99);
				
				
				
				desvio[l][n][0] = Math.sqrt(desvio[l][n][0])/(6.99);
				desvio[l][n][1] = Math.sqrt(desvio[l][n][1]);
			}
			
			n++;
			
		}
		
		
		
			try{
				

				File f0 = new File(statsPath + "_MediaGeral.csv");
				
				BufferedWriter bwW = new BufferedWriter(new FileWriter(f0));
				
				for(int l = 0 ; l < 4 ; l++){
					
					for(int c = 0 ; c < 8 ; c++){
						
						bwW.write( (Double.toString(media[l][c][0]) + ";").replace('.', ','));
						bwW.write( (Double.toString(media[l][c][1]) + ";").replace('.', ','));
						
					}
					
					bwW.write("\r\n");
					
					for(int c = 0 ; c < 8 ; c++){
						
						bwW.write( (Double.toString(desvio[l][c][0]) + ";").replace('.', ','));
						bwW.write( (Double.toString(desvio[l][c][1]) + ";").replace('.', ','));
						
					}
					
					bwW.write("\r\n");
					
				}
				
				bwW.close();
				
				
			} catch(Exception e){
				
				e.printStackTrace();
				
			}
			
		
	}
	

}
