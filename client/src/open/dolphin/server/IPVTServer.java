package open.dolphin.server;

import open.dolphin.client.IMainService;

/**
 * @author Kazushi, Minagawa
 */
public interface IPVTServer extends IMainService {
    
    /**
     *
     * @return　ポート番号
     */
    public int getPort();
    
    /**
     *
     * @param port
     */
    public void setPort(int port);
    
    /**
     * 
     * @return　エンコード
     */
    public String getEncoding();
    
    /**
     *
     * @param enc
     */
    public void setEncoding(String enc);
}
