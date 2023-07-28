package com.techno.mpm.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.web.multipart.MultipartFile;

import com.sun.xml.txw2.IllegalAnnotationException;

public class DOCXTextExtractor {

	private DOCXTextExtractor() {
	}

	@SuppressWarnings("resource")
	public static String extractTextFromFile(String filePath) {
		try (FileInputStream fis = new FileInputStream(filePath); XWPFDocument document = new XWPFDocument(fis)) {
			XWPFWordExtractor extractor = new XWPFWordExtractor(document);
			return extractor.getText();
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalAnnotationException(e.getMessage());
		}
	}

	
	@SuppressWarnings("resource")
	public static String extractTextFromMultipartFile(MultipartFile file) {
		try {

			return new XWPFWordExtractor(new XWPFDocument(file.getInputStream())).getText();

		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalAnnotationException(e.getMessage());
		}
	}

	private static File convertMultipartFileToFile(MultipartFile file) throws IOException {
		File convertedFile = new File(file.getOriginalFilename());
		file.transferTo(convertedFile);
		return convertedFile;
	}

}
