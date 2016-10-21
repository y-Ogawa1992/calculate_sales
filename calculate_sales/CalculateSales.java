package jp.co.plusize.ogawa_yuutarou.calculate_sales;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;




public class CalculateSales {
	public static void main(String[] args) throws IOException {

//		if(args.length != 1) {
//			System.out.println("予期せぬエラーが発生しました");
//			return;
//		}

		//処理内容１ branch.lstの処理

		HashMap<String, String> branchMap = new HashMap<String, String>();
		HashMap<String, Long> branchTotalMap = new HashMap<String, Long>();
		BufferedReader br = null;



		//メソッド分け
		if (!fileRead("branch.lst", "^\\d{3}$", "支店", branchMap, branchTotalMap, args[0])) {
			return;
		}
		fileRead("branch.lst", "^\\d{3}$", "支店", branchMap, branchTotalMap, args[0]);

		 //処理内容２ commodity.lstの処理

		 HashMap<String, String> commodityMap = new HashMap<String, String>();
		 HashMap<String, Long> commodityTotalMap = new HashMap<String, Long>();

		 //メソッド分け
		 if (!fileRead("commodity.lst", "^[0-9a-zA-Z]{8}", "商品", commodityMap, commodityTotalMap, args[0])) {
			 return;
		 }
		 fileRead("commodity.lst", "^[0-9a-zA-Z]{8}", "商品", commodityMap, commodityTotalMap, args[0]);



		 //処理内容３－１ 集計処理①


		 File salesDir = new File(args[0]);
		 File salesFile[] = salesDir.listFiles();
		 ArrayList<String> fileName = new ArrayList<String>();

		 //for文でディレクトリ内を１つずつ見て
		 //if文で8桁数字.rcdのみを取り出す。
		 //ArrayListで取り出した文字列を格納
		 //104行目で昇順にソート
		 for (int i = 0; i < salesFile.length; i++) {
			 if (salesFile[i].getName().matches("^[0-9]{8}.rcd$") && salesFile[i].isFile()) {
				 fileName.add(salesFile[i].getName());
			 }
		 }
		Collections.sort(fileName);
		//拡張子手前で取出し
		//for文でfileNameを全てチェックし、最小1最大3で当てはまらなければエラー
		int max = Integer.parseInt(fileName.get(0).split("\\.")[0]);
		int min = Integer.parseInt(fileName.get(1).split("\\.")[0]);
		for(int i = 0; i < fileName.size(); i++) {
			int temp = Integer.parseInt(fileName.get(i).split("\\.")[0]);
			if(max < temp) max = temp;
			if(min > temp) min = temp;
		}
		if(!(min == 1 && fileName.size() == max)) {	//ArrayList内の要素数 = 最大値の式が成り立てば
			System.out.println("売上ファイル名が連番になっていません");
			return;
		}


		//処理内容３－２ 集計処理②-支店合計

		for(int i = 0; i < fileName.size(); i++) {

			//■格納した「8桁.rcd」のファイルを１つずつ、１行ずつ読み取る■
			try {

				//ファイル型に変換
				File file = new File(args[0], fileName.get(i));
				br = new BufferedReader(new FileReader(file));

				String str = null;
				ArrayList<String> fileData = new ArrayList<String>();
				while ((str = br.readLine()) != null) {
					fileData.add(str);
				}
				//3行以外ならエラーメッセージ
				if(!(fileData.size() == 3)) {
					System.out.println(fileName.get(i) + "のフォーマットが不正です");
					return;
				}


				//計算処理 支店
				if(branchTotalMap.containsKey(fileData.get(0))) {	//支店コードの存在確認
					long branchTotalValue = branchTotalMap.get(fileData.get(0)) + Long.parseLong(fileData.get(2));
					branchTotalMap.put(fileData.get(0), branchTotalValue);
				} else {
					System.out.println(fileName.get(i) + "の支店コードが不正です");
					return;
				}

				//計算処理 商品
				if(commodityTotalMap.containsKey(fileData.get(1))) {	//商品コードの存在確認
					long commodityTotalValue = commodityTotalMap.get(fileData.get(1)) + Long.parseLong(fileData.get(2));
					commodityTotalMap.put(fileData.get(1), commodityTotalValue);
				} else {
					System.out.println(fileName.get(i) + "の商品コードが不正です");
					return;
				}

				//合計金額10桁超えたらエラーの処理
				if(branchTotalMap.get(fileData.get(0)) > 999999999 || commodityTotalMap.get(fileData.get(1)) > 999999999) {
					System.out.println("合計金額が10桁を超えました");
					return;
				}
			} catch(IOException e) {
				System.out.println("予期せぬエラーが発生しました");
				return;
			} finally {
				try {
					if(br != null) { br.close(); }
				} catch (IOException e) {
					System.out.println("予期せぬエラーが発生しました");
					return;
				}
			}
		}

		//支店別集計ファイル

		//メソッド分け　エラララー
		if (!fileWrite(branchTotalMap, branchMap, "branch.out", args[0])) {
			System.out.println("予期せぬエラーが発生しました");
			return;
		}
		fileWrite(branchTotalMap, branchMap, "branch.out", args[0]);

		//商品別売上ファイル

		//ファイル書き込みメソッド分け
		if (!fileWrite(commodityTotalMap, commodityMap, "commodity.out", args[0])) {
			System.out.println("予期せぬエラーが発生しました");	//エラー出てるよ
			return;
		}
		fileWrite(commodityTotalMap, commodityMap, "commodity.out", args[0]);

	}

	static boolean fileWrite(HashMap<String, Long> totalMap, HashMap<String, String> codeNameMap,
			String fileName, String args) throws IOException {

		List<Map.Entry<String,Long>> nameSort =
				new ArrayList<Map.Entry<String,Long>>(totalMap.entrySet());
		Collections.sort(nameSort, new Comparator<Map.Entry<String,Long>>() {
			@Override
			public int compare(
					Entry<String,Long> entry1, Entry<String,Long> entry2) {
				return ((Long)entry2.getValue()).compareTo((Long)entry1.getValue());
			}
		});

		File file = new File(args, fileName);
		BufferedWriter bw = null;
		bw = new BufferedWriter(new FileWriter(file));

		try {
			for(Entry<String,Long> s : nameSort) {
				bw.write(s.getKey() + "," + codeNameMap.get(s.getKey()) + "," + s.getValue() + "\n");
			}
		} catch(IOException e) {
			return false;
		} finally {
			bw.close();
		}
		return true;
	}

	static boolean fileRead(String fileName, String format, String twoName,
			HashMap<String, String> codeNameMap, HashMap<String, Long> totalMap, String args) throws IOException {
		File twoFile = new File(args, fileName);

		if(!twoFile.exists()){
			System.out.println(twoName + "定義ファイルが存在しません");
			return false;
		}

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(twoFile));

			String str;
			while((str = br.readLine()) != null) {
				String[] twoData = str.split(",", 0);

				if (twoData.length != 2 || !twoData[0].matches(format)) {
					System.out.println(twoName + "定義ファイルのフォーマットが不正です");
					return false;
				}
				codeNameMap.put(twoData[0], twoData[1]);
				//支店コードと金額０円のマップを作る
				totalMap.put(twoData[0], 0L);
			}
		} catch(IOException e) {
			System.out.println("予期せぬエラーが発生しました");
			return false;
		} finally {
			br.close();
		}
		return true;
	}

}