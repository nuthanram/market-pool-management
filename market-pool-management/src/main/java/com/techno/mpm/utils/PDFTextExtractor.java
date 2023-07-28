package com.techno.mpm.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PDFTextExtractor {

	private PDFTextExtractor() {
	}

	public static String extractTextFromFile(String filePath) {
		try {
			FileInputStream fileInputStream = new FileInputStream(filePath);
			return new PDFTextStripper().getText(PDDocument.load(fileInputStream));
		} catch (Exception e) {
			log.info(e.getMessage());
			throw new IllegalStateException(e.getMessage());
		}
	}

	
	public static String extractTextFromFile2(MultipartFile multipartFile) {
		try {

			PDFTextStripper textStripper = new PDFTextStripper();
			textStripper.setSortByPosition(true);
			textStripper.setShouldSeparateByBeads(false);

			return textStripper.getText(PDDocument.load(multipartFile.getInputStream()));

		} catch (Exception e) {
			log.info(e.getMessage());
			throw new IllegalStateException(e.getMessage());
		}
	}

	public static String extractTextFromMultipartFile(MultipartFile file) {
		try {
			File convertedFile = convertMultipartFileToFile(file);
			PDDocument document = PDDocument.load(convertedFile);
			String text = new PDFTextStripper().getText(document);
			document.close();
			return text;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new IllegalStateException("Failed to extract text from the PDF: " + e.getMessage());
		}
	}

	private static File convertMultipartFileToFile(MultipartFile file) throws IOException {
		File convertedFile = new File(file.getOriginalFilename());
		file.transferTo(convertedFile);
		return convertedFile;
	}

}
