package utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class testFile {
	/**
	 * @param args
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// file(�ڴ�)----������---->������----�����---->file(�ڴ�)
		File file = new File("d:\\temp", "addfile.txt");
		try {
			file.createNewFile(); // �����ļ�
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ���ļ�д������(�����)
		String str = "�װ���С�Ϲϣ�";
		byte bt[] = new byte[1024];
		bt = str.getBytes();
		try {
			FileOutputStream in = new FileOutputStream(file);
			try {
				in.write(bt, 0, bt.length);
				in.close();
				// boolean success=true;
				// System.out.println("д���ļ��ɹ�");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			// ��ȡ�ļ����� (������)
			FileInputStream out = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(out);
			int ch = 0;
			while ((ch = isr.read()) != -1) {
				System.out.print((char) ch);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}