package com.java.steams;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;

/**
 * File reader class
 * 
 *
 */
public class FileReaders {

	public static void main(String[] args) {
		try {
			FileWriter outPutFile = new FileWriter(args[0]);
			File folder = new File(args[1]);
			for (File file : folder.listFiles()) {
				if (file.isDirectory()) {
					getFileFromFolder(file, outPutFile);
				} else {
					writeToFile(file.getPath(), outPutFile, file.getName());
				}
			}
			outPutFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get file from current folder
	 * 
	 * @param file
	 * @param outPutFile
	 */
	private static void getFileFromFolder(File file, FileWriter outPutFile) {
		for (File childFile : file.listFiles()) {
			if (childFile.isDirectory()) {
				getFileFromFolder(childFile, outPutFile);
			} else {
				writeToFile(childFile.getPath(), outPutFile, childFile.getName());
			}
		}

	}

	/**
	 * Write content of current file to output file
	 * 
	 * @param currentFilepath
	 * @param outPutFile
	 * @param parentFileName
	 */
	private static void writeToFile(String currentFilepath, FileWriter outPutFile, String parentFileName) {
		try {

			String s;
			File currentFile = new File(currentFilepath);
			FileReader fr = new FileReader(currentFile);
			BufferedReader br = new BufferedReader(fr);
			LineNumberReader lr = new LineNumberReader(br);
			int count = 0;
			while ((s = lr.readLine()) != null) {
				if (s.startsWith("*require") && s.endsWith("*") && count == 0) {
					System.out.println("Getting child file content");
					if (s.substring(10, s.length() - 2).contains(parentFileName)) {
						throw new IOException("Cyclic Dependency Exist");
					}
					writeToFile(s.substring(10, s.length() - 2), outPutFile, currentFile.getName());
					count++;
				} else if (s.startsWith("*require") && s.endsWith("*") && count != 0) {
					continue;
				} else {
					outPutFile.write(s);
					outPutFile.write(System.getProperty("line.separator"));
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}
