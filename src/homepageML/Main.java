package homepageML;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.FloydWarshallShortestPaths;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.tartarus.snowball.ext.PorterStemmer;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
public class Main {
	static String strLine;
	static char[] strLine2;
	static char[] chararray1; 
	static String str1;
	static ArrayList<String> globalArray=new ArrayList<String>();
	static ArrayList<Integer> globalCount=new ArrayList<Integer>();
	
	//Clustering Requirements
	static UndirectedGraph<String, DefaultEdge> undirectedGraph = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
	static ArrayList<String> arr2= new ArrayList<String>();
	
	static ArrayList<String> cluster1=new ArrayList<String>();
	static ArrayList<String> cluster2=new ArrayList<String>();
 	static ArrayList<String> cluster3=new ArrayList<String>();
 	static ArrayList<String> cluster4=new ArrayList<String>();
 	static ArrayList<String> cluster5=new ArrayList<String>();
 	static ArrayList<String> cluster6=new ArrayList<String>();
 	
 	static ArrayList<String> store=new ArrayList<String>();
 	static ArrayList<Integer> randomNumbers= new ArrayList<Integer>(6);
 	
	
	public static void main(String[] args) throws Exception{
		
		//Retrieving History URLS and storing it in an array historyitems
		ArrayList<String> historyitems= new ArrayList<String>();
		FileInputStream fis=new FileInputStream("ImpureHistory.doc");
		DataInputStream datain=new DataInputStream(fis);
		BufferedReader buf=new BufferedReader(new InputStreamReader(datain));
		int index=0;
		
		while((strLine = buf.readLine()) != null){
			String s1=" ";
			chararray1=strLine.toCharArray();
			for(index=8;chararray1[index]!='/';index++){
				
			}
			
			for(int i=0;i<index;i++){			
				s1=s1+chararray1[i];
			}
			
			if(!historyitems.contains(s1)){
				historyitems.add(s1);
			}
			
		}
		//System.out.println(historyitems);
		datain.close();
		//Call function to get parsed html pages in form of stop words and frequency 
		//for(int i=0;i<historyitems.size();i++){
			HTMLParse(historyitems.get(0));
		//}
		//System.out.println(globalArray);
		//System.out.println(globalCount);
		
		//call function to get a string from ODP, which is input to graph formation and clustering
		getODPString(globalArray.get(0));
		
		ArrayList<String> arr1= new ArrayList<String>();
		 
		Main obj1=new Main();
		for(int i=0;i<globalArray.size();i++){
			if(getODPString(globalArray.get(i))!=null){
				obj1.createGraph(getODPString(globalArray.get(i)), arr1);
			}
		}
		//System.out.println(undirectedGraph.toString());
		 
		 for(int i1=0;i1<arr1.size();i1++){
	        	if(!arr2.contains(arr1.get(i1))){
	        		arr2.add(arr1.get(i1));
	        	}
	        }
		 
		 //Adjacency Matrix Creation
		 int V=arr2.size();
	        Integer[][] adj=new Integer[V][V];
	        
	        for(int j1=0;j1<V;j1++){
	        	for(int j2=0;j2<V;j2++){
	        		if(undirectedGraph.containsEdge(arr2.get(j1), arr2.get(j2))){
	        			adj[j1][j2]=1;
	        			adj[j2][j1]=1;
	        		}
	        		else{
	        			adj[j1][j2]=0;
	        			adj[j2][j1]=0;
	        		}
	        	}
	        }
	        
	        for(int j1=0;j1<V;j1++){
	        	for(int j2=0;j2<V;j2++){
	        		//System.out.print(adj[j1][j2]);
	        	}
	        	//System.out.println();
	        }
	        
	      //*************FLOYD WARSHALL ALGORITHM********************************************
	        FloydWarshallShortestPaths obj3=new FloydWarshallShortestPaths(undirectedGraph);
	   	 	Double[][] fwMatrix=new Double[arr2.size()][arr2.size()];
	   	 	for(int i=0;i<arr2.size();i++){
	   	 		for(int j=0;j<arr2.size();j++){
	   	 			fwMatrix[i][j]=obj3.shortestDistance(arr2.get(i),arr2.get(j));
	   	 		}
	   	 	}
	   	 	for(int j1=0;j1<arr2.size();j1++){
	   	 		for(int j2=0;j2<arr2.size();j2++){
	   	 			//System.out.print(fwMatrix[j1][j2]+"  ");
	   	 		}
	   	 		//System.out.println();
	   	 	}
	   	 	// K-means Clustering
	   	 	//Step:1
	   	 	randomGenerate();
	   	 	System.out.println(randomNumbers);
	   	 	
	   	 	//System.out.println(fwMatrix[randomNumbers.get(1)][5]+arr2.get(5));
	   	 	
	   	 	cluster1.add(arr2.get(randomNumbers.get(0)));
	   	 	cluster2.add(arr2.get(randomNumbers.get(1)));
	   	 	cluster3.add(arr2.get(randomNumbers.get(2)));
	   	 	cluster4.add(arr2.get(randomNumbers.get(3)));
	   	 	cluster5.add(arr2.get(randomNumbers.get(4)));
	   	 	cluster6.add(arr2.get(randomNumbers.get(5)));
	   	 	 	 
	   	 	
	   	 	createClusters(fwMatrix);
	   	 	System.out.println(cluster1);
		 	System.out.println(cluster2);
		 	System.out.println(cluster3);
			System.out.println(cluster4);
			System.out.println(cluster5);
			System.out.println(cluster6);
			for(int i=0;i<5;i++){
			newClusterArrangement(fwMatrix,cluster6);
			//System.out.println(cluster6);
			newClusterArrangement(fwMatrix,cluster5);
			//System.out.println(cluster5);
			newClusterArrangement(fwMatrix,cluster4);
			//System.out.println(cluster4);
			newClusterArrangement(fwMatrix,cluster3);
			//System.out.println(cluster3);
			newClusterArrangement(fwMatrix,cluster2);
			//System.out.println(cluster2);
			newClusterArrangement(fwMatrix,cluster1);
			//System.out.println(cluster1);
			randomNumbers.clear();
			randomNumbers.add(arr2.indexOf(cluster1.get(0)));
			randomNumbers.add(arr2.indexOf(cluster2.get(0)));
			randomNumbers.add(arr2.indexOf(cluster3.get(0)));
			randomNumbers.add(arr2.indexOf(cluster4.get(0)));
			randomNumbers.add(arr2.indexOf(cluster5.get(0)));
			randomNumbers.add(arr2.indexOf(cluster6.get(0)));
			createClusters(fwMatrix);
			System.out.println(i+1);
			System.out.println(cluster1);
		 	System.out.println(cluster2);
		 	System.out.println(cluster3);
			System.out.println(cluster4);
			System.out.println(cluster5);
			System.out.println(cluster6);
	}
		
	        
}
	//Porter's Stemming
	private String stem(String word)
	{ 
       PorterStemmer stem = new PorterStemmer(); 
       stem.setCurrent(word); 
       stem.stem(); 
       return stem.getCurrent(); 
    } 
	private static void HTMLParse(String inputUrl) throws Exception{
		Writer out1=null;
		//URL url = new URL("http://facebook.com");
		URL url = new URL(inputUrl);
		Document doc = Jsoup.parse(url,3000);
 
 		//Element docBody = doc.body();
 		String docTitle=doc.title();
 		String docText=doc.text();
 		
 		//System.out.println(docBody.html());
 		//System.out.println(docTitle);
 		//System.out.println(docText);
 
 		Elements bTag = doc.getElementsByTag("b");
 		//System.out.println(bTag.html());
 		
 		String text=docTitle+"\n\n"+docText;
 		String Text=text.toLowerCase();
 		File fsave1=new File(docTitle+ ".txt");
 		out1=new BufferedWriter(new FileWriter(fsave1));
 		out1.write(Text);
 		out1.close();
 		System.out.println("File Created Successfully");
	
 		File file1=new File(docTitle+".txt");
 		Scanner scanfile=new Scanner(new FileReader(file1));
 		String theWord; 
 		String xline="";
	
 		while (scanfile.hasNext()){ 
 			theWord = scanfile.next();
 			if(theWord.startsWith("(")||theWord.startsWith("?")){
 				theWord=" ";
 			}
 			if(theWord.endsWith(")")){
 				theWord=" ";
 			}
 			xline=xline+theWord+" ";
	} 
	//System.out.println(xline);
	char[] chararray2=xline.toLowerCase().toCharArray();
	int a;
	for(a=0;a<xline.length();a++){
		if((int)chararray2[a]<97||(int)chararray2[a]>122)
		{
			chararray2[a]=' ';				
			
		}
	}
	//System.out.println((int) chararray2[5]);
	String strfinal=new String(chararray2);
	Set<String> wordsToRemove = ImmutableSet.of("a","the","an", "for","from","of","to","or","in","out","by","and","on","s"); 
	 
    // this code will run in a loop reading one line after another from the file 
    // 
    String linex = strfinal.toLowerCase(); 
    StringBuffer outputLine = new StringBuffer(); 
    for (String word : Splitter.on(Pattern.compile(" ")).trimResults().omitEmptyStrings().split(linex)) { 
        if (!wordsToRemove.contains(word)) { 
            if (outputLine.length() > 0) { 
                outputLine.append(' '); 
            } 
            outputLine.append(word); 
        } 
    } 
 
    // 
    System.out.println(outputLine.toString()); 
    Writer out2=null;
    File fsave2=new File("clean.txt");
     try{
    	 out2=new BufferedWriter(new FileWriter(fsave2));
     out2.write(outputLine.toString());
     out2.close();
     }catch(IOException ie){
    	 
     }
   //IMPLEMENTING PORTER'S STEMMING ALGORITHM
     ArrayList<String> stemmedWords=new ArrayList<String>();
     String str3;
     Main stemWords=new Main();
     for (String word1 : Splitter.on(Pattern.compile(" ")).trimResults().omitEmptyStrings().split(outputLine.toString()))
     { 
    	 	str3=stemWords.stem(word1);
    	 	stemmedWords.add(str3);
	        System.out.println(str3);
     } 
   //CALCULATING FREQUENCY OF STOP WORDS, CREATING A HASHMAP AND STORING IT TO A FILE
    String keyWord;
     //HashMap hm=new HashMap();
     Writer outhits=null;
	    File fsave11=new File("stopWordsfreq"+docTitle+".txt");
	    outhits=new BufferedWriter(new FileWriter(fsave11));
	    String newline = System.getProperty("line.separator");
	    int arraysize=stemmedWords.size();
     for(int i=0;i<=arraysize-1;i++)
     {
    	 int key = 0;
    	 keyWord=stemmedWords.get(i);
    	 for(int j=i;j<(stemmedWords.size()-i);j++){
    		 if(keyWord.matches(stemmedWords.get(j))){
    			 key++;
    			 stemmedWords.remove(j);
    			 
    		 }
    		 arraysize=stemmedWords.size();
    		 
    		 //hm.put(keyWord, new Integer(key));
    		 
    	 }
    	 if(key==0){
    		 key=1;
    	 }
    	 if(globalArray.contains(keyWord)){
    		 int cout=globalArray.indexOf(keyWord);
    		 if(globalCount.get(cout)!=null){
    			 int key1=globalCount.get(cout)+key;
    			 globalCount.add(cout, key1);
    		 }
    		 else
    		 {
    			 globalCount.add(cout, key);
    		 }
    		 
    	 }
    	 else{
    		 globalArray.add(keyWord);
    		 globalCount.add(key);
    	 }
    	 outhits.write(keyWord + ": " +key+newline);
     }
     outhits.close();   
	}
	
	private static String getODPString(String searchWord) throws Exception{
		 Writer out3=null;
		  URL url = new URL("http://www.dmoz.org/search?q="+searchWord);
	   
	    Document doc = Jsoup.parse(url,3000);
	     
	     //Element docBody = doc.body();
	     String docTitle=doc.title();
	     String docText=doc.text();
	     //System.out.println(docBody.html());
	     //System.out.println(docTitle);
	     //System.out.println(docText);
	     
	     Elements bTag = doc.getElementsByTag("strong");
	     String dir=bTag.html().toString();
	     String removeStr1="Open Directory Categories";
	     String removeStr2="Open Directory Sites";
	     if(dir.contains(removeStr1)){
	     String dir1=dir.replace(searchWord, "");
	     dir1=dir1.replace(removeStr1,"");
	     dir1=dir1.replace(removeStr2, "");
	     dir1=dir1.trim();
	     //System.out.println(dir1);
	     
	     File fsave3=new File("temporary.doc");
	     out3=new BufferedWriter(new FileWriter(fsave3));
	     out3.write(dir1);
	     out3.close();
	     
	     FileInputStream fstreami=new FileInputStream(fsave3);
	     DataInputStream in1=new DataInputStream(fstreami);
	     BufferedReader brin=new BufferedReader(new InputStreamReader(in1));
	     String strLineOdp=brin.readLine().toLowerCase();
	     if(strLineOdp.contains(searchWord.toLowerCase())){
	    	 //System.out.println(strLineOdp);
	      }
	     String ODPresult="Directory:"+strLineOdp;
	     //System.out.println(ODPresult);
	     
	     return ODPresult;
	    }
	     else{
	    	 return "Directory";
	     }
	}
	
	public  UndirectedGraph<String, DefaultEdge> createGraph(String strLine,ArrayList<String> arr1){
		int x=arr1.size();
		//System.out.println(x);
		for (String word : Splitter.on(Pattern.compile(":")).trimResults().omitEmptyStrings().split(strLine)) { 
   		 	arr1.add(word);
   		 	if(!undirectedGraph.containsVertex(word)){
   		 		undirectedGraph.addVertex(word);
	        }
	      }
   	 	for(int j=x;j<arr1.size()-1;j++){
   	 		if(!undirectedGraph.containsEdge(arr1.get(j), arr1.get(j+1))){
   	 			undirectedGraph.addEdge(arr1.get(j), arr1.get(j+1));
   	 		}
   	 	}
		return undirectedGraph;
		
	}
	
	public static void randomGenerate(){
		Random randomGenerator= new Random();
   	 	for(int i=0;i<6;i++){
   	 		int randomInt=randomGenerator.nextInt(arr2.size());
   	 		//System.out.println(randomInt);
   	 		if(!randomNumbers.contains((Integer)randomInt)){
   	 			randomNumbers.add(randomInt);
   	 		}
   	 		else{
   	 			randomInt=randomGenerator.nextInt(arr2.size());
   	 			randomNumbers.add(randomInt);
   	 		}
   	 	}
	}
	public static void createClusters(Double[][] fwMatrix){
		double min=0.0;
   	 	int index = 0;
   	 	double temp;
   	 	int j=0;
   	 	for(int i=0;i<arr2.size();i++){
   	 		for(j=0;j<6;j++){
   	 			if(j==0){
   	 				min= fwMatrix[randomNumbers.get(j)][i];
   	 				
   	 			}
   	 			temp=fwMatrix[randomNumbers.get(j)][i];
   	 			if(temp!=0 &&temp<min){
   	 				min=temp;
   	 				index=j;
   	 			}
   	 			//System.out.println("hello "+arr2.get(randomNumbers.get(j)));
   	 			
   	 		}
   	 		//System.out.println(index);
   	 		if(!cluster1.contains(arr2.get(i))&&!cluster2.contains(arr2.get(i))&&!cluster3.contains(arr2.get(i))&&!cluster4.contains(arr2.get(i))&&!cluster5.contains(arr2.get(i))&&!cluster6.contains(arr2.get(i))){
   	 			if(cluster1.contains(arr2.get(randomNumbers.get(index)))){
   	 				//System.out.println("a");
   	 				cluster1.add(arr2.get(i));
   	 			}
   	 			else if(cluster2.contains(arr2.get(randomNumbers.get(index)))){
   	 				//System.out.println("b");
   	 				cluster2.add(arr2.get(i));
   	 			}
   	 			else if(cluster3.contains(arr2.get(randomNumbers.get(index)))){
   	 				//System.out.println("c");
   	 				cluster3.add(arr2.get(i));
   	 			}
   	 			else if(cluster4.contains(arr2.get(randomNumbers.get(index)))){
   	 				//System.out.println("d");
   	 				cluster4.add(arr2.get(i));
   	 			}
   	 			else if(cluster5.contains(arr2.get(randomNumbers.get(index)))){
   	 				//System.out.println("e");
   	 				cluster5.add(arr2.get(i));
   	 			}
   	 			else if(cluster6.contains(arr2.get(randomNumbers.get(index)))){
   	 				//System.out.println("f");
   	 				cluster6.add(arr2.get(i));
   	 			}
   	 		}
   	 	}
	}
	
	//Formation of new Cluster centroids
	public static ArrayList<String> newClusterArrangement(Double[][] fwMatrix,ArrayList<String> clustertemp){
		//System.out.println(arr2.indexOf(cluster2.get(1)));
		ArrayList<String> temp=new ArrayList<String>();
		int index1=arr2.indexOf(clustertemp.get(0));
		double max=0;
		double min=1;
		int maxindex=0;
		
		if(clustertemp.size()!=1){
		for(int i=1;i<clustertemp.size();i++){
			int x=arr2.indexOf(clustertemp.get(i));
			if(max<fwMatrix[index1][x]){
				max=fwMatrix[index1][x];
				maxindex=x;
			}
			if(min>fwMatrix[index1][x]){
				min=fwMatrix[index1][x];
			}
		}
		
		double dist=0;
		for(int i=0;i<clustertemp.size();i++){
			int index2=arr2.indexOf(clustertemp.get(i));
			double dist1=fwMatrix[index1][index2];
			dist=dist+dist1;
		}
		
		double mean=Math.round(dist/clustertemp.size());
		double mean1=(double)((int)dist/clustertemp.size());
		//System.out.println(mean+" "+min+" "+max+" "+mean1+" "+maxindex);
		
		for(int i=1;i<clustertemp.size();i++){
			int x=arr2.indexOf(clustertemp.get(i));
			if(fwMatrix[index1][x]==mean){
				temp.add(clustertemp.get(i));
			}
			
		}
		if(temp.isEmpty()){
			while(temp.isEmpty()==false||mean1!=0){
				for(int i=1;i<clustertemp.size();i++){
					int x=arr2.indexOf(clustertemp.get(i));
					if(fwMatrix[index1][x]==mean1){
						temp.add(clustertemp.get(i));							
					}	
				}
				mean1--;
			}
		}
		if(temp.isEmpty()){
			while(temp.isEmpty()==false){
				for(int i=1;i<clustertemp.size();i++){
					int x=arr2.indexOf(clustertemp.get(i));
					if(fwMatrix[index1][x]==mean){
						temp.add(clustertemp.get(i));							
					}	
				}
				mean++;
			}
		}
		
		//System.out.println(temp);
		double newmax = 0;
		double disttemp;
		int newCentroidindex = 0;
		for(int i=0;i<temp.size();i++){
			
			disttemp=fwMatrix[arr2.indexOf(temp.get(i))][maxindex];				
			if(disttemp<max){
				newmax=disttemp;
				newCentroidindex=i;
			}
			//System.out.println(disttemp);
		}
		//System.out.println(newmax+" "+newCentroidindex+" "+temp.get(newCentroidindex));
		clustertemp.clear();
	
		clustertemp.add(temp.get(newCentroidindex));
	
		temp.clear();
	}
	else{
		temp.add(clustertemp.get(0));
		//System.out.println(temp);
	}
		return clustertemp;
}


}
