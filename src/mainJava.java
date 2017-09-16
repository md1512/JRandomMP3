/*
    This file is part of JRandomMP3

    JRandomMP3 is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    JRandomMP3 is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with JRandomMP3.  If not, see <http://www.gnu.org/licenses/>.
  
 */
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class mainJava {

	public static void main(String[] args) {
		File origin = new File(".");
		System.out.println("Directory:" + origin.getAbsolutePath());
		Random r = new Random();
		File[] l = retrieveMp3(origin);
		Map<File, Integer> map = new HashMap<File, Integer>();
		Pattern pattern = Pattern.compile(".*\\d+-.*");
		int digit = (int) (Math.floor(Math.log10(l.length)) + 1);
		String format = "%0" + digit + "d";
		for (File x : l) {
			int i = r.nextInt(l.length);
			while (map.containsValue(i)) {
				i = r.nextInt(l.length);
			}
			map.put(x, i);
		}
		for (File x : map.keySet()) {
			String result = "";
			result = createNewFilename(origin, map, pattern, format, x, result);
			System.out.println(x + "\t\t=> " + result);
			try {
				Files.move(x.toPath(), new File(result).toPath(), REPLACE_EXISTING);
			} catch (IOException e) {
				System.err.println("Error during the renaming");
				e.printStackTrace();
			}
		}		
		System.out.println("Done!");
	}

	private static String createNewFilename(File origin, Map<File, Integer> map, Pattern pattern, String format, File x,
			String result) {
		Matcher matcher = pattern.matcher(x.toString());
		if (matcher.matches()) {
			String y = x.toString();
			String[] part = y.split("(/|-)");
			for (int i = 1; i < part.length; i++) {
				if (i == 1) {
					result += origin.getAbsolutePath() + "/" + String.format(format, map.get(x)) + "-";
				} else {
					result += part[i];
				}
			}
		} else {
			result = origin.getAbsolutePath() + "/" + String.format(format, map.get(x)) + "-" + x.getName();
		}
		return result;
	}

	private static File[] retrieveMp3(File f) {
		File[] l = f.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".mp3");
			}
		});
		return l;
	}

}
