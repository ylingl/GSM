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
		// ����ļ��в������򴴽�
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
		int count = 1;// д�ļ�����
		try {
			// ��һ��д�ļ���ʽ 7M 45����
//			out = new FileOutputStream(new File("d:/add.txt"));
//			long begin = System.currentTimeMillis();
//			for (int i = 0; i < count; i++) {
//				out.write(text.getBytes());
//			}
//			out.close();
//			long end = System.currentTimeMillis();
//			System.out.println("FileOutputStreamִ�к�ʱ:" + (end - begin) + "����");

			File file = new File(filePath);
			new File(file.getParent()).mkdirs();
			file.createNewFile();
			
			// �ڶ���д�ļ���ʽ  7M 39����
			outStr = new FileOutputStream(new File(filePath));
			Buff = new BufferedOutputStream(outStr);
			long begin0 = System.currentTimeMillis();
			for (int i = 0; i < count; i++) {
				Buff.write(text.getBytes());
			}
			Buff.flush();
			Buff.close();
			long end0 = System.currentTimeMillis();
			System.out.println("BufferedOutputStreamִ�к�ʱ:" + (end0 - begin0) + "���룬�ļ�·����" + filePath);

			// ������д�ļ���ʽ  7M 473����
//			fw = new FileWriter("d:/add2.txt");
//			long begin3 = System.currentTimeMillis();
//			for (int i = 0; i < count; i++) {
//				fw.write(text);
//			}
//			fw.close();
//			long end3 = System.currentTimeMillis();
//			System.out.println("FileWriterִ�к�ʱ:" + (end3 - begin3) + "����");
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
