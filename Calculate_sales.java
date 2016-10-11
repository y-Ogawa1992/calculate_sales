package jp.co.plusize.ogawa_yuutarou;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Calculate_sales {
	public static void main(String[] args) {
		try {
			File file = new File(args[0],"branch.lst");
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			
			String shi;
			HashMap<String, String> map = new HashMap<String, String>();
					while((shi = br.readLine()) != null) {
						String[] shiten = shi.split(",", 0);
						map.put("支店コード", shiten[0]);
						map.put("支店名", shiten[1]);
						System.out.println(map.entrySet());
					}
		br.close();
		fr.close();
		} catch(IOException e) {
		System.out.println("支店定義ファイルが存在しません");
		  }
	}
}