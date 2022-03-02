
import java.io.File;
import java.io.FileNotFoundException;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;



public class InfoRetrieval {

public static void main(String[] args) throws FileNotFoundException, IOException, SecurityException{
	
		

        String filePathString, result = null;

        PrintWriter printWriter;
        String  stpFileName = "";  

        File dataFolder = new File("C:\\Users\\MSI\\Desktop\\IR\\Data");
        File stpFolder = new File("C:\\Users\\MSI\\Desktop\\IR\\dataStpResults");
        File sfxFolder = new File("C:\\Users\\MSI\\Desktop\\IR\\dataSfxResults");
        
        
        
        File[] dataFiles = dataFolder.listFiles();
        
        
       
        fileMethods.deleteAllFilesFromDirectory(stpFolder);
        
        for (File f : dataFiles) {
        	
            System.out.println(f.getName());
            
             stpFileName =  f.getName().split("[.]")[0]  + ".stp";


            filePathString = fileMethods.fileToSentence("C:\\Users\\MSI\\Desktop\\IR\\Data\\" + f.getName());
            
            
            result = StringStopWordsMethods.sentenceWithoutStopListWords(filePathString);
            
            
            File myObj = new File("C:\\Users\\MSI\\Desktop\\IR\\dataStpResults\\"+ stpFileName);
            myObj.createNewFile();
            
            printWriter = new PrintWriter("C:\\Users\\MSI\\Desktop\\IR\\dataStpResults\\" +  stpFileName, "UTF-8");
            printWriter.print(result.replaceAll("\\s+", System.getProperty("line.separator")));      
            printWriter.close();
            
            
            
                            }
        
        System.out.println("------------------------Phase 1-------------------");
        System.out.println("**************************************************");
        
        
        
        
       
        File[] stpFiles = stpFolder.listFiles();
        
        fileMethods.deleteAllFilesFromDirectory(sfxFolder);
        
        for(File f : stpFiles ) {
        	
        	
            String sfxFileName = f.getName().split("[.]")[0] + ".sfx";

        	
        	//System.out.println("*/**/*// "+ f.getName()+" **////");
        	LinkedList<String> stpFileStringLinkedList = fileMethods.textFileToStringList("C:\\Users\\MSI\\Desktop\\IR\\dataStpResults\\"+f.getName());
        	//System.out.println("@@"+ stpFileStringLinkedList.toString());
        	
        	Stemmer proterStemmer = new Stemmer();
            String sfxResult = proterStemmer.completeStem(stpFileStringLinkedList);
            
            File myObj = new File("C:\\Users\\MSI\\Desktop\\IR\\dataSfxResults\\"+ sfxFileName);
            myObj.createNewFile();
            
            printWriter = new PrintWriter("C:\\Users\\MSI\\Desktop\\IR\\dataSfxResults\\" +  sfxFileName , "UTF-8");

            printWriter.print(sfxResult);
           
            printWriter.close();
        }
        
        
        System.out.println("------------------------Phase 2-------------------");
        
        
        
        
        File[] sfxFiles = sfxFolder.listFiles();
        
        HashSet<String> allWordsHashSet = new HashSet<>();
        
        for(File f: sfxFiles) {
        	LinkedList<String> allFileWords = fileMethods.textFileToStringList("C:\\Users\\MSI\\Desktop\\IR\\dataSfxResults\\"+f.getName());
        	
        	for(String word : allFileWords) {
        		allWordsHashSet.add(word);
        	}
        
        }
        
        ArrayList<String> allWordsWithoutRepList = new ArrayList<>(allWordsHashSet);
        Collections.sort(allWordsWithoutRepList);
        
        
        double[][] matrix = new double[sfxFiles.length][allWordsWithoutRepList.size()];
        Arrays.stream(matrix).forEach(a -> Arrays.fill(a, 0));
        
       /*Fill the matrix row by row each iteration*/
        
        for (int x=0; x < sfxFiles.length; x++) {
        	int[] tempRow = fileMethods.checkOccurenceOfwordsListInTextFile("C:\\Users\\MSI\\Desktop\\IR\\dataSfxResults\\"+sfxFiles[x].getName(),allWordsWithoutRepList);
        	
        	for (int i=0; i <allWordsWithoutRepList.size();i++) {
        		matrix[x][i] = tempRow[i];
        	}
        }
        
        int[] docFreq = new int[allWordsWithoutRepList.size()];
        
        for(int w =0; w < allWordsWithoutRepList.size(); w++) {
        	int dFcount =0;
        	for(int f =0; f<sfxFiles.length; f++) {
        		if(matrix[f][w]!=0)dFcount++;
        	}
        	docFreq[w]= dFcount;
        }
        
        // detect new stop words
        List<String> newStopWordsList = new ArrayList<>();
        
        System.out.println(" ");
        for (int i: docFreq) {
        	if(i==sfxFiles.length) {
        		newStopWordsList.add(allWordsWithoutRepList.get(i));
        		System.out.println("STP word found : "+allWordsWithoutRepList.get(i));
        	}
        }
        
        
        // Calculate TFIDF = TF (term freq.) * IDF (inverse doc freq)
        
        for(int w=0; w < allWordsWithoutRepList.size(); w++) {
        	for(int f=0; f < sfxFiles.length; f++) {
        		matrix[f][w] = matrix[f][w] * Math.log10(sfxFiles.length/ Double.valueOf(docFreq[w]));
        	}
        }
        
        
        
        
        System.out.println("------------------------Phase 3-------------------");
        System.out.println("**************************************************");
        
        
        
        System.out.println(" Search query: ");
        Scanner inputScanner =new  Scanner(System.in);
        String query = StringStopWordsMethods.sentenceWithoutStopListWords(inputScanner.nextLine()); 
        inputScanner.close();
        long startTime = System.currentTimeMillis();
        
        LinkedList<String> queryLinkedList = new LinkedList<>(Arrays.asList(query.split("\\s+")));
        
        
        String[] queryWords = new Stemmer().completeStem(queryLinkedList).split("[\\n\\s+]");
      
        HashSet<String> queryHashSet = new HashSet<>();
        
        for(String q : queryWords) {
        	if(!q.isEmpty())
        	 queryHashSet.add(q);
        }
        
        // user query Term Frequency table (word : queryWordsWithoutRep , count : queryTermFreq)
        ArrayList<String> queryWordsWithoutRep = new ArrayList<String>(queryHashSet);
        int[] queryTermFreq = fileMethods.checkOccurenceOfWordsListInLinkedList(new LinkedList<>(Arrays.asList(queryWords)),queryWordsWithoutRep);
        
         
        double[] queryWordsInAllWordsWithoutRepTfidf = new double[allWordsWithoutRepList.size()];
        
        for(int w = 0; w<allWordsWithoutRepList.size(); w++) {
        	for(int q=0; q<queryWordsWithoutRep.size(); q++) {
        		
        		if(allWordsWithoutRepList.get(w).equals(queryWordsWithoutRep.get(q))) {
        			
        			queryWordsInAllWordsWithoutRepTfidf[w] = queryTermFreq[q] * Math.log10(sfxFiles.length/ Double.valueOf(docFreq[w]));
        			break;
        			
        		}
        	}
        }
        // query count in all words table ( word : allWordsWithoutRepList, count : queryWordsInAllWordsWithoutRepTfidf)
        
        
        /*for(int x=0; x< queryWordsInAllWordsWithoutRepTfidf.length;x++) {
        	System.out.println(" word "+allWordsWithoutRepList.get(x)+" : "+queryWordsInAllWordsWithoutRepTfidf[x]);
        }*/
        
       
        	
        // cosin ( Doc & querty )
        
        double[] cosinDocRank = new double[sfxFiles.length];
        
        
        double cosinNumerator;
        double cosinDenominator;
        double ti = 0;
        double tj = 0;
        
        
        
        for (int f=0; f< sfxFiles.length; f++) {
        	
        	cosinNumerator = 0;
        	cosinDenominator = 1;
        	
        	ti=0;tj=0;
        	
        	for(int w=0;w<allWordsWithoutRepList.size();w++) {
        		
        		cosinNumerator += matrix[f][w] * queryWordsInAllWordsWithoutRepTfidf[w];
        		ti += Math.pow( matrix[f][w] , 2);
        		tj += Math.pow( queryWordsInAllWordsWithoutRepTfidf[w] , 2);
        		
        	}
        	cosinDenominator *= tj+ti==0 ? 1 : Math.sqrt(tj)*Math.sqrt(ti);
        	//System.out.println("file "+f+" Numa "+cosinNumerator+" Deno "+cosinDenominator);
        	cosinDocRank[f] = Double.isNaN(cosinNumerator/cosinDenominator)? 0.0 : cosinNumerator/cosinDenominator;
        	
        	
        }
        
      /*  for(int f=0;f<sfxFiles.length;f++) {
        	System.out.println("file "+sfxFiles[f].getName()+" cosin "+cosinDocRank[f]);
        }*/
        
        Pair[] cosinIndexValuePairs = new Pair[cosinDocRank.length];

		for(int i=0; i<cosinDocRank.length; i++){
			cosinIndexValuePairs[i]= new Pair(i, cosinDocRank[i]);
		}
		
		int count = 0;
		double c = 1;
		Arrays.sort(cosinIndexValuePairs);
		
		System.out.println("-------------------------------------");
		System.out.println("      Sorted Cosine  ");
		for(Pair pair : cosinIndexValuePairs) {
			//if(pair.value!=0.0) {
				count++;
				System.out.println("Rank "+count+" "+sfxFiles[pair.index].getName()+" Cosine : "+(Math.round(pair.value*10000))/10000.0);
				//				}
			}
		
		
		long endTime = System.currentTimeMillis();
		 System.out.println("  ----------------------------------------------  ");
		System.out.println("    "+count+" Results Found in "+(endTime-startTime)+" milliseconds");
        
        System.out.println("------------------------Phase 4-------------------");
        System.out.println("**************************************************");
        
        for(Pair pair: cosinIndexValuePairs) {
            System.out.println(" Rank : "+c+++" "+sfxFiles[pair.index].getName());
            	
            }
           
            //static input //TODO convert to dynamic
            
            int[] relevantDoc = new int[] {1,0,1,0,1};//Given ,this should have the same size as the sfx files and in the same order as "cosinIndexValuePairs"
            int relevantCount = 3;
            
            double[] precision =Evaluation.calcualtePricision(cosinIndexValuePairs, relevantDoc);
            double[] recall = Evaluation.calculateRecall(cosinIndexValuePairs, relevantDoc, relevantCount);
            Evaluation.Fmeasure(precision, recall,count);
            
            System.out.println("------------------------Phase 5-------------------");
            System.out.println("**************************************************");
	}
	



}
    





