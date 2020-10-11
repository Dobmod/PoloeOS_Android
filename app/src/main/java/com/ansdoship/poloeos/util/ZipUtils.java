package com.ansdoship.poloeos.util;
import android.content.Context;
import java.util.zip.ZipEntry;
import java.io.File;
import java.util.zip.ZipInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ZipUtils
{
	/**
	 * 从assets中解压zip文件的方法
	 *
	 * @param zipName    压缩文件的名称
	 * @param targetPath 解压的路径
	 */
	public static void unzipFileFromAssets(Context context, String zipName, String targetPath)
	{
		try
		{
			int bufferSize = 1024 * 10;
			// 建立zip输入流
			ZipInputStream zipInputStream = new ZipInputStream(context.getAssets().open(zipName));
			// 每一个zip文件的实例对象
			ZipEntry zipEntry;
			// 文件路径名称
			String fileName = "";
			// 从输入流中得到实例
			while ((zipEntry = zipInputStream.getNextEntry()) != null)
			{
				fileName = zipEntry.getName();
				//如果为文件夹,则在目标目录创建文件夹
				if (zipEntry.isDirectory())
				{
					// 截取字符串,去掉末尾的分隔符
					fileName = fileName.substring(0, fileName.length() - 1);
					// 创建文件夹
					File folder = new File(targetPath + File.separator + fileName);
					if (!folder.exists())
					{
						folder.mkdirs();
					}
				}
				else
				{
					File file = new File(targetPath + File.separator + fileName);
					if (!file.exists())
					{
						file.createNewFile();
						// 得到目标路径的输出流
						FileOutputStream fos = new FileOutputStream(file);
						// 开始读写数据
						int length = 0;
						byte[] buffer = new byte[bufferSize];
						while ((length = zipInputStream.read(buffer)) != -1)
						{
							fos.write(buffer, 0, length);
						}
						fos.close();
					}
				}
			}
			zipInputStream.close();

		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
