/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package open.dolphin.plugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import jcifs.Config;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFileOutputStream;

import open.dolphin.log.LogWriter;
import open.dolphin.project.GlobalVariables;

/**
 * 電子カルテからRS_Baseへのファイルの書き出しを提供するためのクラスです。
 * @author 星野 雅昭
 */
public abstract class WriteFileToRSB {
    protected String patientID; //患者ID
    protected String address;
    protected String folder; //RS_Baseが動作しているPCの共有フォルダ
    protected String username; //共有フォルダにアクセスできるユーザ名
    protected String password; //上記ユーザのパスワード
    //protected static final String BR = System.getProperty("line.separator");
    protected static final String BR = "\r\n"; //WindowsのCRLF

    public  WriteFileToRSB(String patientID) {
        this.patientID = patientID; //患者ID

        this.address = GlobalVariables.getRsbAddress();
        this.folder = GlobalVariables.getRsbFolder();
        this.username = GlobalVariables.getRsbUserName();
        this.password = GlobalVariables.getRsbPassword();
        if (GlobalVariables.getRsbAcsCtrl()) {
            Properties prop = new Properties();
            prop.setProperty("jcifs.smb.client.username", this.username);
            prop.setProperty("jcifs.smb.client.password", this.password);
            Config.setProperties(prop);
        }
    }

    /**
     * 
     * @return SMB URL
     */
    protected abstract String editSmbUrl();

    /**
     *
     * @param text
     * @return コンテンツ
     */
    protected abstract String editContent(String text);

    /**
     * Windowsの共有フォルダに指定した内容でファイルを書き出す
     * @param text　内容
     * @return
     */
    public boolean witeFileToShare(String text){
        boolean blRet = true;
        String content = editContent(text); //内容
        String url = editSmbUrl(); //SMB URL

        try {
            SmbFileOutputStream sfos = new SmbFileOutputStream(url);
            OutputStreamWriter osw = new OutputStreamWriter(sfos);
            BufferedWriter bw = new BufferedWriter(osw);
            PrintWriter pr = new PrintWriter(bw);
            pr.println(content);
            pr.close();
        } catch (SmbException e) {
            LogWriter.error(getClass(), e);
            blRet = false;
        } catch (MalformedURLException e) {
            LogWriter.error(getClass(), e);
            blRet = false;
        } catch (UnknownHostException e) {
            LogWriter.error(getClass(), e);
            blRet = false;
        } catch (Exception e) {
            LogWriter.error(getClass(), e);
            blRet = false;
        }

        return blRet;
    }
}

/**
 * RS_Base側の共有フォルダの所見ファイルに指定した患者の所見を書き出す。
 * @author 星野 雅昭
 */
class WriteShokenFile extends WriteFileToRSB {
    private final String SHOKEN_FILE_NAME = "_shoken.dat"; //所見ファイル名

    /**
     *
     * @param patientID 患者ID
     */
    WriteShokenFile(String patientID) {
        super(patientID);
    }

    /**
     * 所見ファイルへのURLを編集する
     * @return URL
     */
    @Override
    protected String editSmbUrl() {
        StringBuilder sb = new StringBuilder();
        sb.append("smb:");
        //sb.append(File.separator + File.separator + this.address);
        //sb.append(File.separator +this.folder);
        //sb.append(File.separator + this.patientID + SHOKEN_FILE_NAME);

        sb.append("//" + this.address);
        sb.append("/" +this.folder);
        sb.append("/" + this.patientID + SHOKEN_FILE_NAME);

        return sb.toString();
    }

    /**
     * ファイルに書き出す内容を編集する
     * @param text 所見の内容
     * @return ファイルに書き出す内容
     */
    @Override
    protected String editContent(String text) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.patientID + BR); //ID
   
        //日付
        Date date1 = new Date();  //Dateオブジェクトを生成

        //フォーマットパターンを指定して、SimpleDateFormatオブジェクトsdf1を生成します。
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
        sb.append(sdf1.format(date1));
        
        //同日再診の判定
        
        
        sb.append(BR);

        sb.append(text);

        return sb.toString();
    }
}

