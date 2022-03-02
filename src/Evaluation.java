public class Evaluation  {
	
	
	
	public static double[] calcualtePricision(Pair[] cosinIndexValuePairs,int[] relevantDoc) throws ArrayIndexOutOfBoundsException{
		
		double[] precisionDoc = new double[cosinIndexValuePairs.length];
		int relevantRetrived = 0;
		
		// Precision is the RelevantRetrived / Retrieved
		System.out.println("Precision is the fraction of the documents retrieved that are relevant to the user's information need.");
	       
        for(int p=0; p<cosinIndexValuePairs.length;p++) {
        	
        	if(cosinIndexValuePairs[p].value>0.0) {
        		if(relevantDoc[p]==1)++relevantRetrived;
        	precisionDoc[p] = relevantRetrived/Double.valueOf(p+1);
        	
        	
        	}else {
        		precisionDoc[p] = relevantRetrived/Double.valueOf(p+1);

        	}
        	System.out.println(relevantDoc[p]+"  Precesion "+ (double)(Math.round(precisionDoc[p]*100))/100 );

        }
		
        System.out.println(" ------------------------------------------------------------------- ");

		return precisionDoc;
		
	}
	
	
	public static double[] calculateRecall(Pair[] cosinIndexValuePairs,int[] relevantDoc, int relevantCount) throws ArrayIndexOutOfBoundsException {
		
		double[] recallDoc = new double[cosinIndexValuePairs.length];
        int relevantRetrived =0;
        
        // Recall is the RelevantRetrived / Relevant
        System.out.println("Recall is the fraction of the documents that are relevant to the query that are successfully retrieved.");
        
        for(int p=0; p<cosinIndexValuePairs.length;p++) {

        	if(cosinIndexValuePairs[p].value>0.0) {
        		if(relevantDoc[p]==1)++relevantRetrived;
        		recallDoc[p] = relevantRetrived/Double.valueOf(relevantCount);
        		
        		
        	}else {
        		recallDoc[p] = recallDoc[p-1];
        	}
    		System.out.println(relevantDoc[p]+"  Recall "+(double)(Math.round(recallDoc[p]*100))/100);

        }
        
        System.out.println(" ------------------------------------------------------------------- ");
        
		return recallDoc;
	}
	
	public static double[] Fmeasure(double[] precisionDoc,double[] recallDoc, int retrivedDocs) {
		double[] fMeasure = new double[precisionDoc.length];
		
		/*measure that trades off precision versus recall is the F measure 
		  Beta = 1 ==> equally weights precision and recall 
		  F = 2PR/ P+R (default)
		*/
		System.out.println("The weighted harmonic mean of precision and recall *100");
		
		for(int p=0;p< retrivedDocs;p++) {
			fMeasure[p] = 100*2 * precisionDoc[p]*recallDoc[p]/Double.valueOf(precisionDoc[p]+recallDoc[p]);
			System.out.println(" F measure "+(int)fMeasure[p]+" %");
		}
		

		return fMeasure;
		
		
		
	}
}