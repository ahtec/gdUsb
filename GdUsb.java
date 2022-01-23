/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gdusb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gerard
 */
public class GdUsb
{

    public static String rootDoelDir, dirsFile;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        FileInputStream fstream = null;
        try
        {
            dirsFile = args[0];
            rootDoelDir = args[1];

            fstream = new FileInputStream(dirsFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;
            //Read File Line By Line
            while ((strLine = br.readLine()) != null)
            {
                // Print the content on the console
                System.out.println(strLine);
                int plekLaatsteSlash = strLine.lastIndexOf("/");
                String laatsteDir = strLine.substring(plekLaatsteSlash);
                System.out.println(laatsteDir);
                String doel = rootDoelDir + "/" + laatsteDir;
                Path path = Paths.get(doel);
                Files.createDirectories(path);
                File bronDir = new File(strLine);
                File[] filesInDir = bronDir.listFiles();
                for (File fileIn : filesInDir)
                {
                    if (isImage(fileIn))
                    {
                        File doelFile = new File(doel, fileIn.getName());
                        copy(fileIn, doelFile);
                    }
                }

            }       //Close the input stream
            fstream.close();
            /*
        parameter rootDoeldir
        parameter dirs file
        open dirs file
        voor elke regel
        lees laatste dir van regel
        maak doelsubdir aan met laatste dir van regel
        voor elke image file
        copieer file  naar doeldir
        creer softlink van file in n rootDoeldir
             */
        } catch (FileNotFoundException ex)
        {
            Logger.getLogger(GdUsb.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            Logger.getLogger(GdUsb.class.getName()).log(Level.SEVERE, null, ex);
        } finally
        {
            try
            {
                fstream.close();
            } catch (IOException ex)
            {
                Logger.getLogger(GdUsb.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public static void copy(File src, File dest) throws IOException
    {
        InputStream nputStream = null;
        OutputStream utputStream = null;
        try
        {
            nputStream = new FileInputStream(src);
            utputStream = new FileOutputStream(dest);
            // Transfer bytes from in to out
            byte[] buf = new byte[10 * 1024];
            int len;
            while ((len = nputStream.read(buf)) > 0)
            {
                utputStream.write(buf, 0, len);
            }
        } catch (FileNotFoundException fnfe)
        {
            //handle it
        } finally
        {
            nputStream.close();
            utputStream.close();
        }
    }

    static boolean isImage(File erin)
    {

        //        System.out.println("gdview.viewClass.isImage()" + erin.getAbsolutePath());
        Boolean eruit = Boolean.FALSE;
        try
        {
//            Path target = Path.of(erin.getCanonicalPath());
//            if (Files.isSymbolicLink(target))
//            {

            String naamFile = erin.getName();
            if (naamFile.lastIndexOf(".") > 0)
            {

//                String extension = erin.getName().substring(erin.getName().lastIndexOf("."));
                String extension = naamFile.substring(naamFile.lastIndexOf("."));
                if (extension.toLowerCase().contains(".png"))
                {
                    eruit = Boolean.TRUE;
                } else
                {
                    if (extension.toLowerCase().contains(".jpg"))
                    {
                        eruit = Boolean.TRUE;
                    } else
                    {
                        if (extension.toLowerCase().contains(".jpeg"))
                        {
                            eruit = Boolean.TRUE;
                        } else
                        {
                            if (extension.toLowerCase().contains(".tiff"))
                            {
                                eruit = Boolean.TRUE;
                            } else
                            {
                                if (extension.toLowerCase().contains(".gif"))
                                {
                                    eruit = Boolean.TRUE;
                                }

                            }
                        }
                    }
                }
            }
//            }
        } catch (java.lang.StringIndexOutOfBoundsException e)
        {
            System.out.println("gdview.viewClass.getFilesInDirectory() out of bounds exception " + e.toString());

        }
//        catch (IOException ex)
//        {
//            System.out.println("gdsamefile.samefile.isImage() io exception " + ex.toString());
//                    Logger.getLogger(viewClass.class.getName()).log(Level.SEVERE, null, ex);
//        }

        return eruit;
    }
}
