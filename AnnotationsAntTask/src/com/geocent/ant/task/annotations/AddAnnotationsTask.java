/**
 * 
 */
package com.geocent.ant.task.annotations;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Vector;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * @author jladner
 * 
 */
public class AddAnnotationsTask extends Task {

	private Vector<Annotation> annotations = new Vector<Annotation>();
	private String srcDir;

	@Override
	public void execute() throws BuildException {
		if (annotations != null)
			log("Number of annotations to process: " + annotations.size());
		for (Iterator<Annotation> it = annotations.iterator(); it.hasNext();) { // 4
			Annotation annotation = (Annotation) it.next();

			StringBuffer sb = new StringBuffer();
			try {
				// Open the file that is the first
				// command line parameter
				FileInputStream fstream = new FileInputStream(srcDir + "/" + annotation.getClassName());
				// Get the object of DataInputStream
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(
						new InputStreamReader(in));
				String strLine;
				// Read File Line By Line
				int lineNum = 0;
				boolean importAdded = false;
				while ((strLine = br.readLine()) != null) {
					lineNum++;
					if (!importAdded && annotation.importVal != null && strLine.startsWith("package")) {
						sb.append(strLine).append("\n").append("\n").append("import ").append(annotation.importVal).append(";").append("\n");
						importAdded = true;
					} else if (lineNum == annotation.line) {
						sb.append(annotation.annotationVal).append("\n").append(strLine).append("\n");
					} else {
						sb.append(strLine).append("\n");
					}
				}
				// Close the input stream
				in.close();
			} catch (Exception e) {// Catch exception if any
				System.err.println("Error: " + e.getMessage());
				System.err.println(e);
			}
			
			// TODO: instead of print to console; replace file contents.
			try{
			    PrintWriter ostream = new PrintWriter(new FileOutputStream(srcDir + "/" + annotation.getClassName()));
			    ostream.println(sb);
			    ostream.close();
			} catch (Exception e){
			    System.err.println("Error: " + e.getMessage());
                System.err.println(e);
			}
		}

	}

	public String getSrcDir() {
		return srcDir;
	}

	public void setSrcDir(String srcDir) {
		this.srcDir = srcDir;
	}

	public Annotation createAnnotation() {
		Annotation msg = new Annotation();
		annotations.add(msg);
		return msg;
	}

	public class Annotation {
		String importVal;
		String className;
		String annotationVal;
		int line;

		
		
		public String getAnnotationVal() {
			return annotationVal;
		}

		public void setAnnotationVal(String annotationVal) {
			this.annotationVal = annotationVal;
		}

		public String getImportVal() {
			return importVal;
		}

		public void setImportVal(String importVal) {
			this.importVal = importVal;
		}

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}

		public int getLine() {
			return line;
		}

		public void setLine(int line) {
			this.line = line;
		}
	}
}
