
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


import parse.Parse;

public class ParseMain {
	public static void main(String[] args) {
		File file = new File("testunit.txt");
		String str;
		int i=0;
		if (file.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				while ((str = br.readLine()) != null) {
					System.out.println(i++);
					Parse p=new Parse();
					p.parse(str);
					
				}
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
