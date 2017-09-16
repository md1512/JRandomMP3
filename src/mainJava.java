import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
public class mainJava {

	public static void main(String[] args) {
		File f= new File(".");
		File d= new File("./new/");
		System.out.println("Directory di origine:"+f.getAbsolutePath());
		System.out.println("Directory di destinazione:"+d.getAbsolutePath());
		Random r=new Random();
		if(!d.exists())
			d.mkdir();		
		List<Integer> used= new ArrayList<Integer>();
		File[] l=f.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".mp3");
			}
		} );
		for(File x:l)		
		{  
			int i=r.nextInt(l.length);
			while(used.contains(i)){
				i=r.nextInt(l.length);
			}
			used.add(i);

			String newPath=d.getAbsolutePath()+"/"+String.format("%04d", i)+"-"+x.getName();
			System.out.println(x+"\t=>\t"+newPath);
			try {
				Files.copy(x.toPath(),new File(newPath).toPath(),REPLACE_EXISTING);
			} catch (IOException e) {
				System.err.println("Errore nella copia");
				e.printStackTrace();
			}
		} 
		Scanner s= new Scanner(System.in);
		System.out.println("Finito!\nPremi invio");
		s.nextLine();
		s.close();
	}

}
