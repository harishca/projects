package cloud5;

//Import required java libraries
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import java.awt.BorderLayout;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.lang.*;

import weka.clusterers.*;
import weka.core.Instances;
import weka.gui.explorer.ClustererPanel;
import weka.gui.visualize.PlotData2D;
import weka.gui.visualize.VisualizePanel;

//Extend HttpServlet class
public class upload extends HttpServlet {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private String message;
Part file;
String filenamepart;

public void init() throws ServletException
{
   // Do required initialization
   message = "Hello World";
}
 
public void doGet(){

}

public void doPost(HttpServletRequest request,
                 HttpServletResponse response)
         throws ServletException, IOException
{
   // Set response content type
   response.setContentType("text/html");
   System.out.println("Inside Get Method");
   System.out.println("File Name :"+file);
   //here
   Collection<String> filename = this.file.getHeaderNames();   
	filenamepart = getFileName(file);
	System.out.println("File Name is :"+filename);
  // Actual logic goes here.
	BufferedReader inputReader = new BufferedReader(new FileReader(filenamepart));
	Instances dataa = new Instances(inputReader);
	inputReader.close();
	// create the model 
	    SimpleKMeans kMeans = new SimpleKMeans();
	    try {
			kMeans.setNumClusters(3);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    try {
			kMeans.buildClusterer(dataa);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	    // print out the cluster centroids
	    Instances centroids = kMeans.getClusterCentroids(); 
	    for (int i = 0; i < centroids.numInstances(); i++) { 
	      System.out.println( "Centroid " + i+1 + ": " + centroids.instance(i)); 
	    } 
	    File fl = new File("mynew_centroids.csv");
       BufferedWriter bw = new BufferedWriter(new FileWriter(fl));
	    // get cluster membership for each instance 
	    for (int i = 0; i < dataa.numInstances(); i++) { 
	      try {
			System.out.println( dataa.instance(i) + " is in cluster " + kMeans.clusterInstance(dataa.instance(i)));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
	      try {
			bw.write(dataa.instance(i) +","+kMeans.clusterInstance(dataa.instance(i))+ "\n");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    }
	    bw.close();
	    System.out.println("Write completed");
	    //evaluate cluster
	    ClusterEvaluation eval=new ClusterEvaluation();
	    eval.setClusterer(kMeans);
	    try {
			eval.evaluateClusterer(dataa);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   
   //there
   PrintWriter out = response.getWriter();
   out.println("<h1>" + message + "</h1>");
}


private String getFileName(final Part part) {
    for (String content : part.getHeader("content-disposition").split(";")) {
        if (content.trim().startsWith("filename")) {
            return content.substring(
                    content.indexOf('=') + 1).trim().replace("\"", "");
        }
    }
    return null;
}

public void destroy()
{
   // do nothing.
}
}
