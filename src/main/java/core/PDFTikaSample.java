package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

public class PDFTikaSample {

	public static void main(String[] args) throws IOException, SAXException, TikaException {
		String path = "src/main/resources/test.pdf";
		extractText(path);
	}
	
	public static void extractText(String path) throws IOException, SAXException, TikaException {

		BodyContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		FileInputStream inputstream = new FileInputStream(new File(path));
		ParseContext pcontext = new ParseContext();

		//parsing the document using PDF parser
		PDFParser pdfparser = new PDFParser();
		pdfparser.parse(inputstream, handler, metadata,pcontext);

		//getting the content of the document
		System.out.println("Contents of the PDF :" + handler.toString());

		//getting metadata of the document
		System.out.println("Metadata of the PDF:");
		String[] metadataNames = metadata.names();

		for(String name : metadataNames) {
			System.out.println(name+ " : " + metadata.get(name));
		}
	}
}