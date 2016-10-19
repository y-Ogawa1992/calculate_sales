package jp.co.plusize.ogawa_yuutarou;

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
	public static void main(String[] args) {


		//処理内容１ branch.lstの処理

		HashMap<String, String> branchMap = new HashMap<String, String>();
		HashMap<String, Long> branchTotalMap = new HashMap<String, Long>();
		File branchFile = new File(args[0], "branch.lst");


		//branch.lstが無ければエラーメッセージを出す
		if(!branchFile.exists()){
			System.out.println("支店定義ファイルが存在しません");
			return;
		 }

		 FileReader fr = null;
		 BufferedReader br =null;
		 //branch.lst内の文をカンマで区切って紐付けをする
		 //keyとvalueのフォーマットが違えばエラーメッセージ
		 try {
			 //一行ずつ読み込み、全ての支店コードとそれに対応する
			 //支店名を保持する。

			 fr = new FileReader(branchFile);
			 br = new BufferedReader(fr);

			 String str;
			 while((str = br.readLine()) != null) {
				 String[] branchData = str.split(",", 0);

				 //エラー処理
				 //半角数字3桁にマッチ＆配列の要素数２個以外であればエラーメッセージ
				 //どちらも通ったらmapへという流れ
				 //http://java-reference.sakuraweb.com/java_string_regex.html
				 if (branchData.length == 2 && branchData[0].matches("^\\d{3}$")) {
					 branchMap.put(branchData[0], branchData[1]);

					 //支店コードと金額０円のマップを作る
					 branchTotalMap.put(branchData[0], (long) 0);

				 } else {
					 System.out.println("支店定義ファイルのフォーマットが不正です");
					 return;
				 }
			}
		 } catch(IOException e) {
			 System.out.println("予期せぬエラーが発生しました");
		 } finally {
			 try {
				 if(fr != null) { fr.close(); }
			 } catch (IOException e) {
				 System.out.println("予期せぬエラーが発生しました");
			 }
		 }


		 //処理内容２ commodity.lstの処理

		 HashMap<String, String> commodityMap = new HashMap<String, String>();
		 HashMap<String, Long> commodityTotalMap = new HashMap<String, Long>();
		 File commodityFile = new File(args[0], "commodity.lst");

		 //commodity.lstが無ければエラーメッセージを出す
		 if(!commodityFile.exists()){
			 System.out.println("商品定義ファイルが存在しません");
			 return;
		 }

		 //commodity.lst内の文をカンマで区切って紐付けをする
		 //keyとvalueのフォーマットが違えばエラーメッセージ
		 try {

			 fr = new FileReader(commodityFile);
			 br = new BufferedReader(fr);

			 //一行ずつ読み込み、全ての商品コードとそれに対応する
			 //商品名を保持する。
			 String str;
			 while((str = br.readLine()) != null) {
				 String[] commodityData = str.split(",", 0);

				//エラー処理
				//8桁固定の英数字、要素数2つ
				//どちらも通ったらmapへという流れ
				if (commodityData.length == 2 && commodityData[0].matches("^[0-9a-zA-Z]{8}")) {
					commodityMap.put(commodityData[0], commodityData[1]);

					//支店コードと金額０円のマップを作る
					commodityTotalMap.put(commodityData[0], (long) 0);

				} else {
					System.out.println("商品定義ファイルのフォーマットが不正です");
					return;
				}
			 }
		 } catch(IOException e) {
			 System.out.println("予期せぬエラーが発生しました");
		 } finally {
			 try {
				 if(fr != null) { fr.close(); }
			 } catch (IOException e) {
				 System.out.println("予期せぬエラーが発生しました");
			 }
		 }


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
				fr = new FileReader(file);
				br = new BufferedReader(fr);

				String str = null;
				ArrayList<String> fileData = new ArrayList<String>();
				while ((str = br.readLine()) != null) {
					fileData.add(str);
				}
				//3行以外ならエラーメッセージ
				if(!(fileData.size() == 3)) {
					System.out.println("<" + fileName.get(i) + ">" + "のフォーマットが不正です");
					return;
				}

				//計算処理 支店
				if(branchTotalMap.containsKey(fileData.get(0))) {	//支店コードの存在確認
					long branchTotalValue = branchTotalMap.get(fileData.get(0)) + Long.parseLong(fileData.get(2));
					branchTotalMap.put(fileData.get(0), branchTotalValue);
				} else {
					System.out.println("<" + fileName.get(i) + ">" + "の支店コードが不正です>");
					return;
				}

				//計算処理 商品
				if(commodityTotalMap.containsKey(fileData.get(1))) {	//商品コードの存在確認
					long commodityTotalValue = commodityTotalMap.get(fileData.get(1)) + Long.parseLong(fileData.get(2));
					commodityTotalMap.put(fileData.get(1), commodityTotalValue);
				} else {
					System.out.println("<" + fileName.get(i) + ">" + "の商品コードが不正です");
					return;
				}

				//合計金額10桁超えたらエラーの処理
				if(branchTotalMap.get(fileData.get(0)) > 999999999 || commodityTotalMap.get(fileData.get(1)) > 999999999) {
					System.out.println("合計金額が10桁を超えました");
					return;
				}
			} catch(IOException e) {
				System.out.println("予期せぬエラーが発生しました");
			} finally {
				try {
					if(fr != null) { fr.close(); }
				} catch (IOException e) {
					System.out.println("予期せぬエラーが発生しました");
				}
			}
		}

		//支店別集計ファイル

		//金額降順ソート
		List<Map.Entry<String,Long>> branchSort =
				new ArrayList<Map.Entry<String,Long>>(branchTotalMap.entrySet());
		Collections.sort(branchSort, new Comparator<Map.Entry<String,Long>>() {
			@Override
			public int compare(
					Entry<String,Long> entry1, Entry<String,Long> entry2) {
				return ((Long)entry2.getValue()).compareTo((Long)entry1.getValue());
			}
		});

		FileWriter fw = null;
		BufferedWriter bw = null;

		//branch.outを作成
		try {
			File file = new File(args[0], "branch.out");
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);

			for(Entry<String,Long> s : branchSort) {
				fw.write(s.getKey() + "," + branchMap.get(s.getKey()) + "," + s.getValue() + "\n");
			}
		} catch(IOException e) {
			System.out.println("予期せぬエラーが発生しました");
		} finally {
			try {
				if(fw != null) { fw.close(); }
			} catch (IOException e) {
				System.out.println("予期せぬエラーが発生しました");
			}
		}

		//商品別売上ファイル

		//金額降順ソート
		List<Map.Entry<String,Long>> commoditySort =
				new ArrayList<Map.Entry<String,Long>>(commodityTotalMap.entrySet());
		Collections.sort(commoditySort, new Comparator<Map.Entry<String,Long>>() {
			@Override
			public int compare(
					Entry<String,Long> entry1, Entry<String,Long> entry2) {
				return ((Long)entry2.getValue()).compareTo((Long)entry1.getValue());
			}
		});

		//commodity.outを作成
		File file = new File(args[0], "commodity.out");

		try {
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);

			for(Entry<String,Long> s : commoditySort) {
				fw.write(s.getKey() + "," + commodityMap.get(s.getKey()) + "," + s.getValue() + "\n");
			}
		} catch(IOException e) {
			System.out.println("予期せぬエラーが発生しました");
		} finally {
			try {
				if(fw != null) { fw.close(); }
			} catch (IOException e) {
				System.out.println("予期せぬエラーが発生しました");
			}
		}
	}
}