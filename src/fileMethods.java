import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;





public class fileMethods {
	
	
public static int[] checkOccurenceOfwordsListInTextFile(String filePath, ArrayList<String> allWordsWithoutRep) throws FileNotFoundException {
		
		LinkedList<String> allFileWordsList = textFileToStringList(filePath);  
		
		return checkOccurenceOfWordsListInLinkedList(allFileWordsList, allWordsWithoutRep);
		
	}
	
	
	public static int[] checkOccurenceOfWordsListInLinkedList(LinkedList<String> allWordsList, ArrayList<String> allWordsWithoutRep) {
		int[] matrixRow = new int[allWordsWithoutRep.size()];
		
		for (int i=0; i<allWordsWithoutRep.size(); i++) {
			String word = allWordsWithoutRep.get(i);
			int count = 0;
			for (String temp: allWordsList) {
				if(temp.equals(word)) count++;
			}
			matrixRow[i] = count;
		}
		
		return matrixRow;
		
	}
	
	//method that read a file and return a Sentence as a string
	public static String fileToSentence(String fileName) throws FileNotFoundException, IOException {
		int fileNameLength = fileName.length();
	  // final List<String> imageTypeList =  Arrays.asList(".png", ".gif", ".jpg", "tiff");
		
		/*if (fileNameLength<5) {
			return null;
			
		} */ if (fileName.substring(fileNameLength - 4).equals(".txt") ){
			
			return textFileToSentence(fileName);
			
		} else if (fileName.substring(fileNameLength - 5).equals(".docx")) {
			
			return docxFileToSentence(fileName);
			
		}
		else if (fileName.substring(fileNameLength - 4).equals(".pdf")) {
			
			return pdfFileToSentence(fileName);
		}
		//else if (imageTypeList.contains(fileName.substring(fileNameLength - 4))){
		//	return imageToSentence(fileName);
		//}
		else {
			return "";
		}    
	}
	
	
	


	private static String textFileToSentence(String fileName) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
	    try {
	        StringBuilder stringBuilder = new StringBuilder();
	        String line = bufferedReader.readLine();

	        while (line != null) {
	            stringBuilder.append(line);
	            stringBuilder.append("\n ");
	            line = bufferedReader.readLine();
	        }
	        return stringBuilder.toString();
	    }
	    finally {
	        bufferedReader.close();
	    }
	}
	
	
	
	public static LinkedList<String> textFileToStringList(String fileName) throws FileNotFoundException {

        String tempString = "";

        
        Scanner scanner = new Scanner(new File(fileName));

        
        LinkedList<String> wordsLinkedList = new LinkedList<String>();

        
        while (scanner.hasNext()) {
            // find next line
            tempString = scanner.next();
            wordsLinkedList.add(tempString);
        }
        scanner.close();
    
       
        return wordsLinkedList;

    }
	
	
	public static void deleteAllFilesFromDirectory(File dir) {
	    for (File file: dir.listFiles()) {
	        if (!file.isDirectory())
	            file.delete();
	    }
	}
	 
	

	
	private static String docxFileToSentence(String fileName) throws IOException,FileNotFoundException {
		 
		 
		 //TODO chech Apachi poi  consider switch project to Maven / Gradle
		 
	   // document.loadFromFile("C:\\Users\\Administrator\\Desktop\\sample.docx");

		 
         return "";
		
	}
	
	private static String pdfFileToSentence(String fileName) {
		String string = " ";
		 try {
			    
			    
	            PDDocument doc = PDDocument.load(new File(fileName));
	            
	            String text = new PDFTextStripper().getText(doc);;
	            string = text;
	            } catch (IOException e) {
	            e.printStackTrace();
	        }
		
		 return string;
		

	}
	/*
	private static String imageToSentence(String input_file) { 
		  //String input_file="C:\\Users\\Smart\\Pictures\\Screenshots\\imageTess.png";
			 String output_file="C:\\Users\\LENOVO\\Desktop\\IR\\packages\\Tesseract-OCR\\temp\\imageOutText";
			 String tesseract_install_path="C:\\Users\\LENOVO\\Desktop\\IR\\packages\\Tesseract-OCR\\tesseract";
			 String[] command =
			    {
			        "cmd",
			    };
			    Process p;
			 try {
			 p = Runtime.getRuntime().exec(command);
			        new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
			        new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
			        PrintWriter stdin = new PrintWriter(p.getOutputStream());
			        System.out.println("**********************************************************");
			        stdin.println("\""+tesseract_install_path+"\" \""+input_file+"\" \""+output_file+"\" -l eng");
			        stdin.close();
			        p.waitFor();
			        System.out.println();
			        
			        return textFileToSentence(output_file+".txt");
			        
			    } catch (Exception e) {
			 e.printStackTrace();
			    }
			 
			 return "";
	  }
*/
}


class SyncPipe  implements Runnable{
	public SyncPipe(InputStream istrm, OutputStream ostrm) {
	      istrm_ = istrm;
	      ostrm_ = ostrm;
	  }
	  public void run() {
	      try
	      {
	          final byte[] buffer = new byte[1024];
	          for (int length = 0; (length = istrm_.read(buffer)) != -1; )
	          {
	              ostrm_.write(buffer, 0, length);
	          }
	      }
	      catch (Exception e)
	      {
	          e.printStackTrace();
	      }
	  }
	  private final OutputStream ostrm_;
	  private final InputStream istrm_;

}


