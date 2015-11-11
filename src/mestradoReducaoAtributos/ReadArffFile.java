package mestradoReducaoAtributos;

import java.io.BufferedReader;
import java.io.FileReader;

import weka.core.Instances;

public class ReadArffFile {

	
	
	public static Instances read(String filePath){
		
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
		
		return data;

		
	}

}
