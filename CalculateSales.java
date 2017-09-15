package jp.alhinc.inada_daiki.calculate_sales;

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

		if (args.length != 1) {
			System.out.println("予期せぬエラーが発生しました");
			return;
		}
		HashMap<String, String> branchmap = new HashMap<String, String>();
		HashMap<String, String> commoditymap = new HashMap<String, String>();
		HashMap<String, Long> mapa = new HashMap<String, Long>();
		HashMap<String, Long> mapb = new HashMap<String, Long>();
		// 1,支店定義ファイルの読み込み
		BufferedReader br = null;
		if (readFile(args[0], "branch.lst", "支店", branchmap, mapa, "^\\d{3}$")) {
		} else {
			return;
		}
		if (readFile(args[0], "commodity.lst", "商品", commoditymap, mapb, "^[A-Za-z0-9]{8}$")) {
		} else {
			return;
		}

		// 3,集計
		File rcdfile = new File(args[0]);
		// 読み込んだファイルのリストを作る
		File[] files = rcdfile.listFiles();
		// ファイル型でアレイリスト1を作る
		ArrayList<File> array1 = new ArrayList<File>();
		// ファイルの数だけfor文を回す
		for (int i = 0; i < files.length; i++) {
			// 数字8文字、末尾が .rcdかつ、ファイルのみを選択
			if (files[i].getName().matches("\\d{8}.rcd$") && files[i].isFile()) {
				// 条件が合っていれば、アレイリスト1に入れる
				array1.add(files[i]);
			}
		}
		// 連番処理
		if (0 < array1.size()) {
			String[] check = (array1.get(0).getName()).split("\\.");
			int ii = Integer.parseInt(check[0]);
			for (int a = 0; a < array1.size(); a++) {
				String[] number = (array1.get(a).getName()).split("\\.");
				int in = Integer.parseInt(number[0]);
				if (ii++ == in) {
				} else {
					System.out.println("売上ファイル名が連番になっていません");
					return;
				}
			}
		}
		try {
			// アレイリスト1の要素の数だけfor文を回す
			for (int i = 0; i < array1.size(); i++) {
				br = new BufferedReader(new FileReader(array1.get(i)));
				ArrayList<String> array2 = new ArrayList<String>();
				String str;
				// nullになるまで読み込みを繰り返す
				while ((str = br.readLine()) != null) {
					// アレイリスト2にファイルの中身を一行入れる
					array2.add(str);
				}
				// アレイリスト2の中身が3行かを確認
				if (array2.size() == 3) {
				} else {
					System.out.println(array1.get(i).getName() + "のフォーマットが不正です");
					return;
				}
				// アレイリスト2の金額の場所に数字以外が入っていないかを確認
				if (array2.get(2).matches("^[0-9]*$")) {
				} else {
					System.out.println("予期せぬエラーが発生しました");
					return;
				}
				// array2の金額をlong1型に変換する
				long l = Long.parseLong(array2.get(2));
				// 支店コードが正しいかを調べる
				if (mapa.get(array2.get(0)) != null) {
				} else {
					// 支店コードが正しくなかった場合
					System.out.println(array1.get(i).getName() + "の支店コードが不正です");
					return;
				}
				// mapaに支店ごとの売上を合計していく
				mapa.put(array2.get(0), mapa.get(array2.get(0)) + l);

				long j = 9999999999L; // Long型に変換
				if (mapa.get(array2.get(0)) <= j) {
				} else {
					System.out.println("合計金額が10桁を超えました");
					return;
				}
				if (mapb.get(array2.get(1)) != null) {
				} else {
					System.out.println(array1.get(i).getName() + "の商品コードが不正です");
					return;
				}
				// mapbに商品ごとの売上を合計していく
				mapb.put(array2.get(1), mapb.get(array2.get(1)) + l);
				// 商品コードが正しいかを調べる
				if (mapb.get(array2.get(1)) <= j) {
				} else {
					System.out.println("合計金額が10桁を超えました");
					return;
				}
			}
		} catch (IOException e) {
			System.out.println("予期せぬエラーが発生しました");
			return;
		} finally {
			if (br != null)
				try {
					// 読み込みを終了する
					br.close();
				} catch (IOException e) {
					System.out.println("予期せぬエラーが発生しました");
					return;
				}
		}
		if (outPut(args[0], "branch.out", branchmap, mapa)) {
		} else {
			return;
		}
		if (outPut(args[0], "commodity.out", commoditymap, mapb)) {
		} else {
			return;
		}
	}

	// ファイルの読み込み
	public static boolean readFile(String dirPath, String fileName, String filelist, HashMap<String, String> names,
			HashMap<String, Long> sales, String regex) {
		BufferedReader br = null;
		try {
			File file = new File(dirPath, fileName);
			if (file.exists()) {
				br = new BufferedReader(new FileReader(file));
				String s;
				while ((s = br.readLine()) != null) {
					// , で読み込んだ内容を分ける
					String[] items = s.split(",");
					// 支店コードが3桁の数字か確認する
					if (items[0].matches(regex)) {
					} else {
						// 支店コードが不正な場合
						System.out.println(filelist + "定義ファイルのフォーマットが不正です");
						return false;
					}
					// branchitemsの要素数が2つか調べる
					if (items.length == 2) {
					} else {
						// 要素数が2つ以外だった場合
						System.out.println(filelist + "定義ファイルのフォーマットが不正です");
						return false;
					}
					// 分けた内容をbranchmapに持たせる
					names.put(items[0], items[1]);
					// 0をLong型に変換する
					int i = 0;
					long l = i;
					// 支店コードと売上額 0円をmapaに持たせる
					sales.put(items[0], l);
				}
			} else {
				System.out.println(filelist + "定義ファイルが存在しません");
				return false;
			}
		} catch (IOException e) {
			// ファイルが存在しない場合
			System.out.println("予期せぬエラーが発生しました");
			return false;
		} finally {
			if (br != null)
				try {
					br.close(); // 読み込みを終了する
				} catch (IOException e) {
					System.out.println("予期せぬエラーが発生しました");
					return false;
				}
		}
		return true;
	}

	// 4,集計結果結果出力
	public static boolean outPut(String dirPath, String fileName, HashMap<String, String> names,
			HashMap<String, Long> sales) {
		String sep = System.getProperty("line.separator");

		// 支店集計結果結果出力
		// mapaのキーと値をentriesaに入れる
		List<Map.Entry<String, Long>> entries = new ArrayList<Map.Entry<String, Long>>(sales.entrySet());
		BufferedWriter bw = null;
		// 売上額を降順にソートする
		Collections.sort(entries, new Comparator<Map.Entry<String, Long>>() {
			public int compare(Entry<String, Long> entry1, Entry<String, Long> entry2) {
				return ((Long) entry2.getValue()).compareTo((Long) entry1.getValue());
			}
		});
		try {
			File file = new File(dirPath, fileName);
			bw = new BufferedWriter(new FileWriter(file));
			for (Entry<String, Long> s : entries) {
				// 支店コード、支店名、売上額をbranch.outファイルに出力する
				bw.write(s.getKey() + "," + names.get(s.getKey()) + "," + s.getValue() + sep);
			}
			bw.close();
		} catch (IOException e) {
			System.out.println("予期せぬエラーが発生しました");
			return false;
		} finally {
			if (bw != null)
				try {
					bw.close(); // 出力を終了する
				} catch (IOException e) {
					System.out.println("予期せぬエラーが発生しました");
					return false;
				}
		}
		return true;
	}
}
