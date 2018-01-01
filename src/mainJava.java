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

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class mainJava {

	private static int digit = 4;

	public static void main(String[] args) {
		File origin = new File(".");
		System.out.println("Directory:" + origin.getAbsolutePath());
		Random r = new Random();
		File[] l = retrieveMp3(origin);
		Map<File, Integer> map = new HashMap<File, Integer>();
		Pattern pattern = Pattern.compile(".*\\d+-.*");
		String format = "%0" + digit + "d";
		for (File x : l) {
			int i = r.nextInt(l.length);
			while (map.containsValue(i)) {
				i = r.nextInt(l.length);
			}
			map.put(x, i);
		}
		for (File x : map.keySet()) {
			File result = createNewFilename(origin, map, pattern, format, x);
			System.out.println(x + "\t\t=> " + result);

			try {
				Files.move(x.toPath(), result.toPath(), REPLACE_EXISTING);
			} catch (IOException e) {
				System.err.println("Error during the renaming");
				e.printStackTrace();
			}

		}
		System.out.println("Done!");
	}

	private static File createNewFilename(File origin, Map<File, Integer> map, Pattern pattern, String format, File x) {
		File result;
		Matcher matcher = pattern.matcher(x.getName());
		if (matcher.matches()) {
			StringBuilder filename = new StringBuilder(x.getName());
			char[] arr = filename.toString().toCharArray();
			if (arr[digit] == '-') {
				// Already transformed
				int i = 0;
				while (arr[i] != '-') {
					i++;
				}
				filename = new StringBuilder();
				i++;
				while (i < arr.length) {
					filename.append(arr[i]);
					i++;
				}
			}
			result = new File(origin, String.format(format, map.get(x)) + "-" + filename);
		} else {
			result = new File(origin, String.format(format, map.get(x)) + "-" + x.getName());
		}
		return result;
	}

	private static File[] retrieveMp3(File f) {
		return f.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".mp3");
			}
		});
	}

}
