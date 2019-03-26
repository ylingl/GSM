package utils.file;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {

	public static void isFileExists(String path, String fileName) {
		File file = new File(path + fileName);
		if (!file.exists()) {
			try {
				file.getParentFile().mkdirs();
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void isDirExists(String path, String fileName) {
		File file = new File(path + fileName);
		// 如果文件夹不存在则创建
		if (!file.exists()) {
			file.mkdir();
		}
	}

	public static void main(String[] args) {
		saveFile("d:\\asdf.txt","asd;jffksdajdsjkadddddddddddddddddd");
	}

	public static void saveFile(String filePath, String text) {

		//FileOutputStream out = null;
		FileOutputStream outStr = null;
		BufferedOutputStream Buff = null;
		//FileWriter fw = null;
		int count = 1;// 写文件行数
		try {
			// 第一种写文件方式 7M 45豪秒
//			out = new FileOutputStream(new File("d:/add.txt"));
//			long begin = System.currentTimeMillis();
//			for (int i = 0; i < count; i++) {
//				out.write(text.getBytes());
//			}
//			out.close();
//			long end = System.currentTimeMillis();
//			System.out.println("FileOutputStream执行耗时:" + (end - begin) + "豪秒");

			File file = new File(filePath);
			new File(file.getParent()).mkdirs();
			file.createNewFile();
			
			// 第二种写文件方式  7M 39豪秒
			outStr = new FileOutputStream(new File(filePath));
			Buff = new BufferedOutputStream(outStr);
			long begin0 = System.currentTimeMillis();
			for (int i = 0; i < count; i++) {
				Buff.write(text.getBytes());
			}
			Buff.flush();
			Buff.close();
			long end0 = System.currentTimeMillis();
			System.out.println("BufferedOutputStream执行耗时:" + (end0 - begin0) + "豪秒，文件路径：" + filePath);

			// 第三种写文件方式  7M 473豪秒
//			fw = new FileWriter("d:/add2.txt");
//			long begin3 = System.currentTimeMillis();
//			for (int i = 0; i < count; i++) {
//				fw.write(text);
//			}
//			fw.close();
//			long end3 = System.currentTimeMillis();
//			System.out.println("FileWriter执行耗时:" + (end3 - begin3) + "豪秒");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				//fw.close();
				Buff.close();
				outStr.close();
				//out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
