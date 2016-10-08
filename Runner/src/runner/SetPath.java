/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package runner;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.application.Application;

/**
 *
 * @author doo
 */
public class SetPath {
	private String home="";
	Process serverProc = null;

	public SetPath() throws IOException, InterruptedException{
		//PIXME make set HOME directory here
//		home = new File(".").getCanonicalPath();		//C:\Users\doo\Documents\NetBeansProjects\Runner
		home = "C:/Users/doo/Documents/NetBeansProjects";
//		System.out.println(new File(".").getCanonicalPath());
		runClient();
	}
	public void run(){

	}

	public void runBuild() throws IOException, InterruptedException{
		//Process Build
		ProcessBuilder pb = new ProcessBuilder();
		pb.directory(new File(home));
		pb.command("cmd","/c","start",home+"\\ant\\bin\\ant");
		//Process
		Process p = pb.start();
		p.waitFor();
	}//runBuild()
	
	public void runServer() throws InterruptedException, IOException{
		//Process Build
		ProcessBuilder pb = new ProcessBuilder();
		pb.command("cmd","/c",home+"\\jboss-4.2.2.GA\\bin\\run.bat","-b","127.0.0.1");

		// Process
		serverProc = pb.start();
		serverProc.waitFor();
	}//runServer()

	public void exitServer() throws IOException, InterruptedException {
		if (isServerOn()){

			//PIXME Set exact command path IF you need.
			ProcessBuilder pb = new ProcessBuilder();
			pb.command("cmd","/c",home+"\\jboss-4.2.2.GA\\bin\\shutdown.bat -S");
			Process p = pb.start();
			p.waitFor();
		}
		Application.getInstance(RunnerApp.class).exit();
	}

	public void runClient() throws IOException, InterruptedException {
		List cmd = new ArrayList();
		cmd.add("cmd");
		cmd.add("/c");
		cmd.add("start");
		cmd.add("java");
		cmd.add("-Dfile.encoding=UTF-8");
		cmd.add("-Xms128m");
		cmd.add("-Xmx256m");
		cmd.add("-verbose:gc");
		cmd.add("-classpath");
		cmd.add(home+"\\base\\dist\\OpenDolphinBase.jar;"+
				home+"\\lib\\activation.jar;"+
				home+"\\lib\\AppFramework.jar;"+
				home+"\\lib\\clibwrapper_jiio.jar;"+
				home+"\\lib\\commons-codec.jar;"+
				home+"\\lib\\commons-httpclient.jar;"+
				home+"\\lib\\commons-lang.jar;"+
				home+"\\lib\\commons-logging.jar;"+
				home+"\\lib\\dcm4che-imageio-rle-2.0.12.jar;"+
				home+"\\lib\\dcm4che.jar;"+
				home+"\\lib\\dicom.jar;"+
				home+"\\lib\\ejb3-persistence.jar;"+
				home+"\\lib\\hibernate-annotations.jar;"+
				home+"\\lib\\iText-2.0.8.jar;"+
				home+"\\lib\\iTextAsian.jar;"+
				home+"\\lib\\jai_codec.jar;"+
				home+"\\lib\\jai_core.jar;"+
				home+"\\lib\\jai_imageio.jar;"+
				home+"\\lib\\jboss-annotations-ejb3.jar;"+
				home+"\\lib\\jboss-aop-jdk50-client.jar;"+
				home+"\\lib\\jboss-aspect-jdk50-client.jar;"+
				home+"\\lib\\jboss-ejb3x.jar;"+
				home+"\\lib\\jboss-j2ee.jar;"+
				home+"\\lib\\jboss-system.jar;"+
				home+"\\lib\\jbossall-client.jar;"+
				home+"\\lib\\jdom.jar;" +
				home+"\\lib\\log4j.jar;"+
				home+"\\lib\\mail.jar;"+
				home+"\\lib\\postgresql-8.2-504.jdbc3.jar;"+
				home+"\\lib\\swing-layout-1.0.3.jar;"+
				home+"\\lib\\swing-worker-1.1.jar;" +
				home+"\\lib\\velocity-1.4.jar;" +
				home+"\\lib\\velocity-dep-1.4.jar;"+
				home+"\\client\\build\\classes"	);
		cmd.add("open.dolphin.client.Dolphin");
//		List cmd = new ArrayList();
//		cmd.add("cmd /c");
//		cmd.add("ant");
		ProcessBuilder pb = new ProcessBuilder(cmd);

		// Build a Command
//		ProcessBuilder pb = new ProcessBuilder();
//		pb.directory(new File(home+File.separator+"client"));
//		Map<String,String> env = pb.environment();
//		env.put("ANT",home+File.separator+"ant"+File.separator+"bin");
//		env.put("CLASSPATH", System.getenv("JAVA_HOME")+File.separator+"lib");
//		env.put("PATH",
//				System.getenv("PATH")+File.pathSeparator+
//				System.getenv("ANT")+File.pathSeparator+
//				System.getenv("CLASSPATH")+File.pathSeparator+
//				System.getenv("JAVA_HOME")+File.separator+"bin");
//		pb.command("cmd","/c","start",home+"\\ant\\bin\\ant","run");

		// Control the Process
		Process p = pb.start();
		p.waitFor();
	}//runClient()

	public static void printInputStream(InputStream is) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		while(true){
			String line = br.readLine();
			if (line == null) break;
			System.out.println(line);
//			label.setText(line);
		}
		br.close();
	}
	public boolean isServerOn(){
		Socket socket = null;
		try {
			socket = new Socket("127.0.0.1", 8080);
			socket.isBound();
		} catch (UnknownHostException ex) {
			Logger.getLogger(SetPath.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(SetPath.class.getName()).log(Level.SEVERE, null, ex);
		}
		return socket.isBound();
	}
}
