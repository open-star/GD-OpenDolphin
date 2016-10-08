package open.dolphin.client;

import open.dolphin.infomodel.ModuleModel;

/**
 * OrderList
 * 
 * @author  Kazushi Minagawa
 */
public final class OrderList implements java.io.Serializable {

    private static final long serialVersionUID = -6049175115811888229L;
    private ModuleModel[] orderList;

    /**
     * Creates new OrderList
     */
    public OrderList() {
    }

    /**
     * 
     * @param stamp
     */
    public OrderList(ModuleModel[] stamp) {
        this();
        setOrderStamp(stamp);
    }

    /**
     * 
     * @return
     */
    public ModuleModel[] getOrderList() {
        return orderList;
    }

    /**
     * 
     * @param stamp
     */
    public void setOrderStamp(ModuleModel[] stamp) {
        orderList = stamp;
    }
}
