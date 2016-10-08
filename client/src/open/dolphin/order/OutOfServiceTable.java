/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package open.dolphin.order;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author
 */
public class OutOfServiceTable
{
  /*
   * 診療行為と時間外診療の関係
   *
   * timeClass
   * 4 乳幼児時間外特例
   * 5 乳幼児夜間加算
   * 6 乳幼児休日加算
   * 7 乳幼児深夜加算
   * 8 夜間早朝加算
   *
   * TODO 時間外診療コード？4,8は未実装。
   */
  private Map<String, String> outOfServiceTimes;

  /**
   *
   */
  public OutOfServiceTable()
  {
     outOfServiceTimes = new HashMap<String ,String>();
     outOfServiceTimes.put("113007070", "5");
     outOfServiceTimes.put("113007370", "5");
     outOfServiceTimes.put("113007170", "6");
     outOfServiceTimes.put("113007470", "6");
     outOfServiceTimes.put("113007270", "7");
     outOfServiceTimes.put("113007570", "7");
  }

  /*
   * 時間外診療コード？を返す。
   */
  /**
   *
   * @param code
   * @return
   */
  public String timeClass(String code)
  {
     return outOfServiceTimes.get(code);
  }

}