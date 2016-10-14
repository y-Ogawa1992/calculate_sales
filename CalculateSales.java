package jp.co.plusize.ogawa_yuutarou;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class CalculateSales {
	public static void main(String[] args) {
		//支店、売上合計の箱を作る。
		//HashMapで作り、処理内容1,2と同じような感じ


//branch.lstの処理

		HashMap<String, String> branchMap = new HashMap<String, String>();
		File branchFile = new File(args[0], "branch.lst");


		//branch.lstが無ければエラーメッセージを出す
		if(!branchFile.exists()){
			 System.out.println("支店定義ファイルが存在しません");
			 return;
		 }

		 //branch.lst内の文をカンマで区切って紐付けをする
		 //keyとvalueのフォーマットが違えばエラーメッセージ
		 try {
			FileReader fr = new FileReader(branchFile);
			BufferedReader br = new BufferedReader(fr);

			//一行ずつ読み込み、全ての支店コードとそれに対応する
			//支店名を保持する。
			String shi;
			while((shi = br.readLine()) != null) {
				String[] branchData = shi.split(",", 0);

				//エラー処理
				//半角数字3桁にマッチ＆配列の要素数２個以外であればエラーメッセージ
				//どちらも通ったらmapへという流れ
				//http://java-reference.sakuraweb.com/java_string_regex.html
				if (branchData.length == 2 && branchData[0].matches("^\\d{3}$")) {
					branchMap.put(branchData[0], branchData[1]);
				} else {
					System.out.println("支店定義ファイルのフォーマットが不正です");
					return;
				  }	System.out.println(branchMap.entrySet());
			}
			br.close();
			fr.close();
		 } catch(IOException e) {
			}


//commodity.lstの処理

		 HashMap<String, String> commodityMap = new HashMap<String, String>();
		 File commodityFile = new File(args[0], "commodity.lst");

		//commodity.lstが無ければエラーメッセージを出す
			if(!commodityFile.exists()){
				 System.out.println("商品定義ファイルが存在しません");
				 return;
			 }

		 	//commodity.lst内の文をカンマで区切って紐付けをする
		 	//keyとvalueのフォーマットが違えばエラーメッセージ
		 try {
			FileReader fr = new FileReader(commodityFile);
			BufferedReader br = new BufferedReader(fr);

			//一行ずつ読み込み、全ての商品コードとそれに対応する
			//商品名を保持する。
			String shi;
			while((shi = br.readLine()) != null) {
				String[] commodityData = shi.split(",", 0);

				//エラー処理
				//8桁固定の英数字、要素数2つ
				//どちらも通ったらmapへという流れ
				if (commodityData.length == 2 && commodityData[0].matches("^[0-9a-zA-Z]{8}")) {
					commodityMap.put(commodityData[0], commodityData[1]);
				} else {
					System.out.println("商品定義ファイルのフォーマットが不正です");
					return;
				  }	System.out.println(commodityMap.entrySet());
			}
			br.close();
			fr.close();
		 } catch(IOException e) {
			}

//集計処理①

		 File uriageArgs = new File(args[0]);
		 File uriageFile[] = uriageArgs.listFiles();

	 	//for文でディレクトリ内を１つずつ見て
	 	//if文で8桁数字.rcdのみを取り出す。
		//ArrayListで取り出した拡張子抜きの文字列を格納
		//104行目で昇順にソート
		ArrayList<String> fileName = new ArrayList<String>();
		for (int i = 0; i < uriageFile.length; i++) {
			if (uriageFile[i].getName().matches("^[0-9]{8}.rcd$")) {
				fileName.add(uriageFile[i].getName());
				Collections.sort(fileName);
            }
		}
		//拡張子手前で取出し
		//for文でfileNameを全てチェックし、最小1最大3で当てはまらなければエラー
		int max = Integer.parseInt(fileName.get(0).split("\\.")[0]);
		int min = Integer.parseInt(fileName.get(1).split("\\.")[0]);
		for(int i = 0; i < fileName.size(); i++) {
			int temp = Integer.parseInt(fileName.get(i).split("\\.")[0]);
			if(max < temp) max = temp;
			if(min > temp) min = temp;
		}
			if(!(min == 1 && max == 3)) {
				System.out.println("売上ファイル名が連番になっていません");
			}
//集計処理②

			//.rcdファイルの中を1行ずつ取り出す。
			HashMap<String, String> shitenMap = new HashMap<String, String>();
			 File shitenFile = new File(args[0], ".rcd");

				//.rcd内の文を一行ずつ読み込み、保持する
			 	//ここで指定するkeyと保持してあった(.lst２種)のkeyを照らし合わせ
			 	//一時的に保存＝バッファ
			 	//shitenMapはkeyにコード、valueには金額を設定。
			 try {
				FileReader fr = new FileReader(shitenFile);
				BufferedReader br = new BufferedReader(fr);

				String shi;
				while((shi = br.readLine()) != null) {
					String[] shitenData = shi.split(shi);
						System.out.println(shitenData);
			 }
				br.close();
				fr.close();
		} catch(IOException e) {
			System.out.println("x");
		}
	}
}
