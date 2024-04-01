package com.app.sunbeam;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class MyTranslator {

	public static void main(String[] args) {
		String pdfPath = "3963 - गहाणखत.pdf";
        List<Integer> rowsToExtract = List.of(3, 6, 7);  // Rows 4, 7, and 8

        try {
            List<String> extractedTexts = extractTextFromRows(pdfPath, rowsToExtract);
            List<String> translatedTexts = translateTexts(extractedTexts, "mr", "en");

            saveToCSV("translated_texts.csv", translatedTexts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> extractTextFromRows(String pdfPath, List<Integer> rows) throws IOException {
        List<String> texts = new ArrayList<>();
        try (PDDocument document = PDDocument.load(new File(pdfPath))) {
            PDFTextStripper stripper = new PDFTextStripper();
            for (int row : rows) {
                stripper.setStartPage(row);
                stripper.setEndPage(row);
                texts.add(stripper.getText(document));
            }
        }
        return texts;
    }

    public static List<String> translateTexts(List<String> texts, String sourceLanguage, String targetLanguage) {
        List<String> translatedTexts = new ArrayList<>();
        Translate translate = TranslateOptions.getDefaultInstance().getService();
        for (String text : texts) {
            Translation translation = translate.translate(text, Translate.TranslateOption.sourceLanguage(sourceLanguage),
                    Translate.TranslateOption.targetLanguage(targetLanguage));
            translatedTexts.add(translation.getTranslatedText());
        }
        return translatedTexts;
    }

    public static void saveToCSV(String filename, List<String> data) {
        try (FileWriter csvWriter = new FileWriter(filename)) {
            csvWriter.append("Translated Text\n");
            for (String item : data) {
                csvWriter.append(item).append("\n");
            }
            csvWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


	

}
