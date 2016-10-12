package jp.co.plusize.ogawa_yuutarou;

import java.io.*;


public class Calculate_sales {
	public static void main(String[] args) {

		//HashMap外に

		HashMap<String, String> map = new HashMap<String, String>();

		//branch.lstが無ければエラーメッセージを出す

		//fileを定義していない
		if (!file.exists(args[0]."branch.lst")){
			 System.out.println("支店定義ファイルが存在しません");
		 }

		 	//branch.lst内の文をカンマで区切って紐付けをする
		 	//keyとvalueのフォーマットが違えばエラーメッセージ
		 try {
			File file = new File(args[0],"branch.lst");
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);

			//一行ずつ読み込み、全ての支店コードとそれに対応する
			//支店名を保持する。
			String shi;
			while((shi = br.readLine()) != null) {
				String[] shiten = shi.split(",", 0);


				//エラー処理
				//shiten[0]が3桁の数字で無ければエラーメッセージ
				//shiten[1]がカンマ、改行が無ければエラーメッセージ
				//どちらも通ったらmapへという流れ


				//半角数字3桁にマッチしてる
				//http://java-reference.sakuraweb.com/java_string_regex.html
				if (shiten[0].matches != "^\\d{3}$") {
					System.out.println("支店定義ファイルのフォーマットが不正です");

					//配列の要素数２以外であればエラーメッセージ
				} else if (shiten[1].length != 2) {
					System.out.println("支店定義ファイルのフォーマットが不正です");
				  }
					map.put(shiten[0], shiten[1]);
			}
			br.close();
			fr.close();
		  } catch(IOException e) {
			  }  System.out.println(map.entrySet());


	}
}