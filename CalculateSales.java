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
		String sep = System.getProperty("line.separator");
		// 1,支店定義ファイルの読み込み
		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			File file = new File(args[0], "branch.lst");
			if (file.exists()) {
				br = new BufferedReader(new FileReader(file));
				String s;
				// nullになるまで読み込みを繰り返す
				while ((s= br.readLine()) != null) {
					// , で読み込んだ内容を分ける
					String[] branchitems = s.split(",");
					// 支店コードが3桁の数字か確認する
					if (branchitems[0].matches("^\\d{3}$")) {
					} else {
						// 支店コードが不正な場合
						System.out.println("支店定義ファイルのフォーマット不正です");
						return;
					}
					// branchitemsの要素数が2つか調べる
					if (branchitems.length == 2) {
					} else {
						// 要素数が2つ以外だった場合
						System.out.println("支店定義ファイルのフォーマット不正です");
						return;
					}
					// 分けた内容をbranchmapに持たせる
					branchmap.put(branchitems[0], branchitems[1]);
					// 0をLong型に変換する
					int i = 0;
					long l = i;
					// 支店コードと売上額 0円をmapaに持たせる
					mapa.put(branchitems[0], l);
				}
			} else {
				System.out.println("支店定義ファイルが存在しません");
				return;
			}
		} catch (IOException e) {
			// ファイルが存在しない場合
			System.out.println("予期せぬエラーが発生しました");
			return;
		} finally {
			if (br != null)
				try {
					br.close(); // 読み込みを終了する
				} catch (IOException e) {
					System.out.println("予期せぬエラーが発生しました");
				}
		}

		// 2,商品定義ファイルの読み込み
		try {
			File file = new File(args[0], "commodity.lst");
			if (file.exists()) {
			br = new BufferedReader(new FileReader(file));
			String s;
			// nullになるまで読み込みを繰り返す
			while ((s = br.readLine()) != null) {
				// , で読み込んだ内容を分ける
				String[] commodityitems = s.split(",");
				if (commodityitems[0].matches("^[A-Za-z0-9]{8}$")) {
				} else {
					// 商品コードが不正な場合
					System.out.println("商品定義ファイルのフォーマットが不正です");
					return;
				}
				// commodityitemsの要素数が2つか調べる
				if (commodityitems.length == 2) {
				} else {
					// 要素数が2つ以外の場合
					System.out.println("商品定義ファイルのフォーマットが不正です");
					return;
				}
				// 分けた内容をcommoditymapに持たせる
				commoditymap.put(commodityitems[0], commodityitems[1]);
				// 0をLong型に変換する
				int i = 0;
				long l = i;
				// 商品コードと売上額 0円をmapbに持たせる
				mapb.put(commodityitems[0], l);
				}
			} else {
				System.out.println("商品定義ファイルが存在しません");
				return;
			}
		} catch (IOException e) {
			// ファイルが存在しない場合の処理
			System.out.println("予期せぬエラーが発生しました");
			return;
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					System.out.println("予期せぬエラーが発生しました");
					return;
				}
		}
		// 3,集計
		// ファイルを読み込む
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
				if (array2.size() == 3) {
				} else {
					System.out.println(array1.get(i).getName() + "のファイルのフォーマットが不正です");
					return;
				}
				// array2の金額をlong1型に変換する
				long l = Long.parseLong(array2.get(2));
				// 支店コードが正しいかを調べる
				if (mapa.get(array2.get(0)) != null) {
				} else {
					// 支店コードが正しくなかった場合
					System.out.println(array1.get(i).getName() + "のファイルの支店コードが不正です");
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
					System.out.println(array1.get(i).getName() + "のファイルの商品コードが不正です");
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
		// 4,集計結果結果出力

		// 支店集計結果結果出力
		// mapaのキーと値をentriesaに入れる
		List<Map.Entry<String, Long>> entriesa = new ArrayList<Map.Entry<String, Long>>(mapa.entrySet());
		// 売上額を降順にソートする
		Collections.sort(entriesa, new Comparator<Map.Entry<String, Long>>() {
			public int compare(Entry<String, Long> entry1, Entry<String, Long> entry2) {
				return ((Long) entry2.getValue()).compareTo((Long) entry1.getValue());
			}
		});
		try {
			File file = new File(args[0], "branch.out");
			bw = new BufferedWriter(new FileWriter(file));
			for (Entry<String, Long> s : entriesa) {
				// 支店コード、支店名、売上額をbranch.outファイルに出力する
				bw.write(s.getKey() + "," + branchmap.get(s.getKey()) + "," + s.getValue() + sep);
			}
			bw.close();
		} catch (IOException e) {
			System.out.println("予期せぬエラーが発生しました");
			return;
		} finally {
			if (bw != null)
				try {
					bw.close(); // 出力を終了する
				} catch (IOException e) {
					System.out.println("予期せぬエラーが発生しました");
					return;
				}
		}
		// 商品集計結果結果出力
		List<Map.Entry<String, Long>> entriesb = new ArrayList<Map.Entry<String, Long>>(mapb.entrySet());
		// mapbのキーと値をentriesbに入れる
		Collections.sort(entriesb, new Comparator<Map.Entry<String, Long>>() { // 売上額を降順にソートする
			public int compare(Entry<String, Long> entry1, Entry<String, Long> entry2) {
				return ((Long) entry2.getValue()).compareTo((Long) entry1.getValue());
			}
		});

		try {
			File file = new File(args[0], "commodity.out");
			bw = new BufferedWriter(new FileWriter(file));
			for (Entry<String, Long> s : entriesb) {
				// 商品コード、商品名、売上額をcommodity.outファイルに出力する
				bw.write(s.getKey() + "," + commoditymap.get(s.getKey()) + "," + s.getValue() + sep);
			}
			bw.close();
		} catch (IOException e) {
			System.out.println("予期せぬエラーが発生しました");
			return;
		} finally {
			if (bw != null)
				try {
					// 出力を終了する
					bw.close();
				} catch (IOException e) {
					System.out.println("予期せぬエラーが発生しました");
					return;
				}
		}
	}
}
