package core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFBoxSample {

	public static void main(String[] args) throws InvalidPasswordException, IOException {

		String path = "src/main/resources/test.pdf";
		String outputPath = "src/main/resources/output.jpg";
		extractTextFromPDF(path);
		extractImagesFromPDF(path, outputPath, "JPEG");
	}

	public static void extractTextFromPDF(String path) throws InvalidPasswordException, IOException {

		PDDocument document = PDDocument.load(new File(path));
		AccessPermission ap = document.getCurrentAccessPermission();
		if (!ap.canExtractContent())
		{
			throw new IOException("You do not have permission to extract text");
		}

		PDFTextStripper stripper = new PDFTextStripper();

		// This example uses sorting, but in some cases it is more useful to switch it off,
		// e.g. in some files with columns where the PDF content stream respects the
		// column order.
		stripper.setSortByPosition(false);

		for (int p = 1; p <= document.getNumberOfPages(); ++p)
		{
			// Set the page interval to extract. If you don't, then all pages would be extracted.
			stripper.setStartPage(p);
			stripper.setEndPage(p);

			// let the magic happen
			String text = stripper.getText(document);

			// do some nice output with a header
			String pageStr = String.format("page %d:", p);
			System.out.println(pageStr);
			for (int i = 0; i < pageStr.length(); ++i)
			{
				System.out.print("-");
			}
			System.out.println();
			System.out.println(text.trim());
			System.out.println();
		}

		document.close();
	}

	public static void extractImagesFromPDF(String path, String outputPath, String outputFormat) throws InvalidPasswordException, IOException {
		//Loading an existing PDF document
		File file = new File(path);
		PDDocument document = PDDocument.load(file);

		//Getting all pages
		Iterator<PDPage> iter = document.getPages().iterator();

		//Iterating over each page
		while( iter.hasNext() )
		{
			PDPage page = iter.next();
			PDResources resources = page.getResources();

			//Iterating over each objects
			for (COSName name : resources.getXObjectNames()) {
				PDXObject object = resources.getXObject(name);

				//If its image object then save it
				if(object instanceof PDImageXObject) {
					PDImageXObject imageObject = (PDImageXObject) object;
					BufferedImage image = imageObject.getImage();
					ImageIO.write(image, outputFormat, new File(outputPath));
				}
			}
		}	

	}
}