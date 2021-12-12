package br.com.projetoLes.infra;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.Part;

public class FileSaver {
	static Path path = Paths.get("FileSaver.java");
	public static final String SERVER_PATH2 = path.toAbsolutePath().getParent()+ "\\";//+src\\main\\webapp\\assets\\img";
	public static final String SERVER_PATH = "C:/Users/dede-/Desktop/Les 2/g2/Implementação/src/main/webapp/assets/img";
	public String write(Part arquivo, String path){
		String relativePath =  path +"/" + arquivo.getSubmittedFileName();
		if(Paths.get(SERVER_PATH + "/"  + relativePath) != null) {
			try {
				Files.delete(Paths.get(SERVER_PATH + "/"  + relativePath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			
			arquivo.write(SERVER_PATH + "/"  + relativePath);
			
			return relativePath;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}

}

