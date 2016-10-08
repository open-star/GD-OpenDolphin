/*
 * DiagnosisModuleItem.java
 *
 * Created on 2007/09/26, 21:49
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package open.dolphin.message;

import open.dolphin.infomodel.DocInfoModel;
import open.dolphin.infomodel.RegisteredDiagnosisModel;

/**
 *
 * @author kazm
 */
public class DiagnosisModuleItem {

    private DocInfoModel docInfo;
    private RegisteredDiagnosisModel registeredDiagnosisModule;

    /**
     * Creates a new instance of DiagnosisModuleItem  
     */
    public DiagnosisModuleItem() {
    }

    /**
     *
     * @return
     */
    public DocInfoModel getDocInfo() {
        return docInfo;
    }

    /**
     *
     * @param docInfo
     */
    public void setDocInfo(DocInfoModel docInfo) {
        this.docInfo = docInfo;
    }

    /**
     *
     * @return
     */
    public RegisteredDiagnosisModel getRegisteredDiagnosisModule() {
        return registeredDiagnosisModule;
    }

    /**
     *
     * @param registeredDiagnosisModule
     */
    public void setRegisteredDiagnosisModule(RegisteredDiagnosisModel registeredDiagnosisModule) {
        this.registeredDiagnosisModule = registeredDiagnosisModule;
    }
}
