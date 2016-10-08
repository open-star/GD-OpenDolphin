/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package run;

import java.io.File;
import java.util.LinkedList;

/**
 *
 * @author doo
 */
public class FileList {
	private static LinkedList<File> list = new LinkedList<File>();
	
	public  static LinkedList<File> getList(){
		File f = new File(".");
		FileList fd = new FileList();
		fd.recurse(f,0);

		return list;
   }
   void recurse(File dirFile,int depth){
      String contents[] = dirFile.list();
      for(int i = 0; i < contents.length; i++){
         for(int spaces = 0; spaces < depth; spaces++) System.out.print("  ");
         list.add(new File(dirFile, contents[i]));
         File child = new File(dirFile, contents[i]);
//		 System.out.println(dirFile + contents[i]);
         if(child.isDirectory()) {
			 recurse(child,depth+1);
		 }
      }//
   }
}
