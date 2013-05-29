package cpuid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


interface TableDecoder<T>{
	public T decodeLine(String line);
}

public class DecodeTableLoader {
	public static List<Object> loadFeatureFlags(File tableFile,TableDecoder decoder){
		List<Object> decodedObjects = new ArrayList<Object>();
		BufferedReader br = null;		
		try {
			String line = "";
			br = new BufferedReader(new InputStreamReader(new FileInputStream(tableFile)));
			while((line=br.readLine())!=null){
				Object decoded = decoder.decodeLine(line);
				if(decoded!=null){
					decodedObjects.add(decoded);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(br!=null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return decodedObjects;
	}
}
