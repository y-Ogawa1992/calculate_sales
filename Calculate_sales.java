package jp.co.plusize.ogawa_yuutarou;

import java.io.File;
import java.util.ArrayList;


public class Calculate_sales {
	public static void main(String[] args) {

		 File uriageArgs = new File(args[0]);
		 File uriageFile[] = uriageArgs.listFiles();

	 	//for文でディレクトリ内を１つずつ見て
	 	//if文で8桁数字.rcdのみを取り出す。
		//ArrayListで取り出した拡張子抜きの文字列を格納
		ArrayList string = new ArrayList();
		for (int i = 0; i < uriageFile.length; i++) {
			if (uriageFile[i].getName().matches("^[0-9]{8}.rcd$")) {
				string.add(uriageFile[i].getName());;
				String hoge = uriageFile[i].getName().substring(0,8);
				System.out.println(hoge);
            }
		}


	}
}