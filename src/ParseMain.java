
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


import parse.Parse;

public class ParseMain {
	public static void main(String[] args) {
		File file = new File("testunit.txt");
		String str;
		if (file.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				while ((str = br.readLine()) != null) {
					try{
					System.out.println(str);
					Parse p=new Parse();
					p.parse(str);
					}catch(Exception e){
						System.out.println("not well-defined!");
					}
				}
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
