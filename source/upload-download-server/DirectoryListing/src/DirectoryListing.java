import java.io.File;

/**
 * A class to demonstrate simple Java directory listing
 * Cst420/Ser321 Foundations of Distributed Applications
 * see http://pooh.poly.asu.edu/Cst420
 * @author Tim Lindquist (Tim@asu.edu), ASU Polytechnic Engineering
 * @version November 2013
 */
public class DirectoryListing {
   public static void main(String[] args) {
      String path = ".";
      if(args.length >= 1){
         path = args[0];
         System.out.println("Directory path: "+path);
      }
      String file;
      File folder = new File(path);
      File[] listOfFiles = folder.listFiles(); 
      for (int i = 0; i < listOfFiles.length; i++) {
         if (listOfFiles[i].isFile()) {
            file = listOfFiles[i].getName();
            System.out.println(file);
         }
      }
   }
}

