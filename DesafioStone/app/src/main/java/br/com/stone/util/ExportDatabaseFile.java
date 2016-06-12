package br.com.stone.util;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.widget.Toast.*;

/**
 * Created by tiago.casemiro on 18/01/2016.
 */
public class ExportDatabaseFile {

    public static void export(Context context){
        FileInputStream fis=null;
        FileOutputStream fos=null;
        try {
            File dbpath = context.getDatabasePath("stone-pagamentos.db");
            File f=new File(dbpath.getAbsolutePath());
            fis=new FileInputStream(f);
            File fo =new File("/mnt/sdcard/Download/db_dump.db");
            fos=new FileOutputStream(fo);
            while(true){
                int i=fis.read();
                if(i!=-1)
                {fos.write(i);}
                else
                {break;}
            }
            fos.flush();
            makeText(context, "DB dump OK", LENGTH_LONG).show();
        } catch(Exception e) {
            e.printStackTrace();
            makeText(context, "DB dump ERROR", LENGTH_LONG).show();
        } finally {
            try {
                fos.close();
                fis.close();
            }catch(IOException ioe){
                ioe.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
