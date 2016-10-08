/*
 * SimpleLaboModule.java
 *
 * Created on 2003/07/30, 12:29
 */
package open.dolphin.plugin.labotestdocumentpanel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

/**
 * SimpleLaboModule
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public class SimpleLaboModule {

    private String sampleTime;
    private String registTime;
    private String reportTime;
    private String mmlConfirmDate;
    private String reportStatus;
    private String repMemo;
    private String repMemoCodeName;
    private String repMemoCode;
    private String repMemoCodeId;
    private String repFreeMemo;
    private String testCenterName;
    private String set;
    private List simpleLaboTests;

    /** 
     * Creates a new instance of SimpleLaboModule
     */
    public SimpleLaboModule() {
    }

    /**
     *
     * @return
     */
    public String getSampleTime() {
        return sampleTime;
    }

    /**
     *
     * @param val
     */
    public void setSampleTime(String val) {
        sampleTime = val;
    }

    /**
     *
     * @return
     */
    public String getRegistTime() {
        return registTime;
    }

    /**
     *
     * @param val
     */
    public void setRegistTime(String val) {
        registTime = val;
    }

    /**
     *
     * @return
     */
    public String getReportTime() {
        return reportTime;
    }

    /**
     *
     * @param val
     */
    public void setReportTime(String val) {
        reportTime = val;
    }

    /**
     *
     * @return
     */
    public String getMmlConfirmDate() {
        return mmlConfirmDate;
    }

    /**
     *
     * @param val
     */
    public void setMmlConfirmDate(String val) {
        mmlConfirmDate = val;
    }

    /**
     *
     * @return
     */
    public String getReportStatus() {
        return reportStatus;
    }

    /**
     *
     * @param val
     */
    public void setReportStatus(String val) {
        reportStatus = val;
    }

    /**
     *
     * @return
     */
    public String getRepMemo() {
        return repMemo;
    }

    /**
     *
     * @param val
     */
    public void setRepMemo(String val) {
        repMemo = val;
    }

    /**
     *
     * @return
     */
    public String getRepMemoCodeName() {
        return repMemoCodeName;
    }

    /**
     *
     * @param val
     */
    public void setRepMemoCodeName(String val) {
        repMemoCodeName = val;
    }

    /**
     *
     * @return
     */
    public String getRepMemoCode() {
        return repMemoCode;
    }

    /**
     *
     * @param val
     */
    public void setRepMemoCode(String val) {
        repMemoCode = val;
    }

    /**
     *
     * @return
     */
    public String getRepMemoCodeId() {
        return repMemoCodeId;
    }

    /**
     *
     * @param val
     */
    public void setRepMemoCodeId(String val) {
        repMemoCodeId = val;
    }

    /**
     *
     * @return
     */
    public String getRepFreeMemo() {
        return repFreeMemo;
    }

    /**
     *
     * @param val
     */
    public void setRepFreeMemo(String val) {
        repFreeMemo = val;
    }

    /**
     *
     * @return
     */
    public String getTestCenterName() {
        return testCenterName;
    }

    /**
     *
     * @param val
     */
    public void setTestCenterName(String val) {
        testCenterName = val;
    }

    /**
     *
     * @return
     */
    public String getSet() {
        return set;
    }

    /**
     *
     * @param val
     */
    public void setSet(String val) {
        set = val;
    }

    /**
     *
     * @return
     */
    public List getSimpleLaboTest() {
        return simpleLaboTests;
    }

    /**
     *
     * @param list
     */
    public void setSimpleLaboTest(List list) {
        simpleLaboTests = list;
    }

    /**
     *
     * @param val
     */
    @SuppressWarnings("unchecked")
    public void addSimpleLaboTest(SimpleLaboTest val) {
        if (simpleLaboTests == null) {
            simpleLaboTests = new ArrayList();
        }
        simpleLaboTests.add(val);
    }

    /**
     *
     * @return
     */
    public String getHeader() {
        StringBuilder buf = new StringBuilder("採取: ");
        if (sampleTime != null) {
            int index = sampleTime.indexOf('T');
            if (index > 0) {
                String val = sampleTime.substring(0, index);
                buf.append(val);
            } else {
                buf.append(sampleTime);
            }
        }

        return buf.toString();
    }

    /**
     *
     * @param obj
     * @param col
     * @param allTests
     */
    public void fillNormaliedData(Object[][] obj, int col, AllLaboTest allTests) {

        if (simpleLaboTests == null || simpleLaboTests.size() == 0) {
            return;
        }

        int row = 0;

        /*if (registTime != null) {
        obj[row++][col] = registTime;
        }
        
        if (reportTime != null) {
        obj[row++][col] = reportTime;
        } 
        
        if (reportStatus != null) {
        obj[row++][col] = reportStatus;
        }
        
        if (testCenterName != null) {
        obj[row++][col] = testCenterName;
        }
        
        if (set != null) {
        obj[row++][col] = set;
        }*/


        /*if (reportTime != null) {
        obj[row++][col] = "報告: " + reportTime;
        } else {
        obj[row++][col] = "報告: ";
        }
        
        if (reportStatus != null) {
        obj[row++][col] = "ステータス: " + reportStatus;
        } else {
        obj[row++][col] = "";
        }
        
        if (testCenterName != null) {
        obj[row++][col] = testCenterName;
        } else {
        obj[row++][col] = "";
        }
        
        if (set != null) {
        obj[row++][col] = "セット名: " + set;
        } else {
        obj[row++][col] = "";
        }*/

        // このモジュールに含まれる LaboTest(検体とその検体に対するテスト項目セット）の数
        int size = simpleLaboTests.size();
        SimpleLaboSpecimen specimen = null;
        SimpleLaboTest test = null;
        boolean hasSpecimen = false;
        //StringBuffer buf = null;

        /** この検索の全検査項目 */
        Iterator iter = allTests.getAllTests().keySet().iterator();

        while (iter.hasNext()) {

            specimen = (SimpleLaboSpecimen) iter.next();
            //obj[row++][col] = "";
            //obj[row++][col] = specimen.getSpecimenName();
            obj[row++][col] = specimen;

            hasSpecimen = false;
            for (int i = 0; i < size; i++) {
                test = (SimpleLaboTest) simpleLaboTests.get(i);
                if (test.isSpecimen(specimen)) {
                    hasSpecimen = true;
                    break;
                }
            }

            TreeSet ts = (TreeSet) allTests.getAllTests().get(specimen);
            Iterator i = ts.iterator();
            while (i.hasNext()) {

                LaboTestItemID testID = (LaboTestItemID) i.next();

                if (hasSpecimen) {
                    //obj[row++][col] = test.getTestValue(testID);
                    obj[row++][col] = test.getTestItem(testID);

                } else {
                    //obj[row++][col] = testID.getItemName();
                    obj[row++][col] = null;
                }
            }
        }

        obj[row++][col] = "";

        if (registTime != null) {
            obj[row++][col] = registTime;
        } else {
            obj[row++][col] = "";
        }

        if (reportTime != null) {
            obj[row++][col] = reportTime;
        } else {
            obj[row++][col] = "";
        }

        if (reportStatus != null) {
            obj[row++][col] = reportStatus;
        } else {
            obj[row++][col] = "";
        }

        if (testCenterName != null) {
            obj[row++][col] = testCenterName;
        } else {
            obj[row++][col] = "";
        }

        if (set != null) {
            obj[row++][col] = set;
        } else {
            obj[row++][col] = "";
        }
    }

    /**
     *
     * @param allTests
     */
    public void normalize(AllLaboTest allTests) {

        if (simpleLaboTests == null || simpleLaboTests.size() == 0) {
            return;
        }

        int size = simpleLaboTests.size();
        SimpleLaboSpecimen specimen = null;
        StringBuffer buf = null;
        SimpleLaboTest test = null;
        boolean hasSpecimen = false;

        Iterator iter = allTests.getAllTests().keySet().iterator();

        while (iter.hasNext()) {

            buf = new StringBuffer();

            specimen = (SimpleLaboSpecimen) iter.next();
            buf.append((specimen.getSpecimenName()));
            buf.append(System.getProperty("line.separator"));

            hasSpecimen = false;
            for (int i = 0; i < size; i++) {
                test = (SimpleLaboTest) simpleLaboTests.get(i);
                if (test.isSpecimen(specimen)) {
                    hasSpecimen = true;
                    break;
                }
            }

            TreeSet ts = (TreeSet) allTests.getAllTests().get(specimen);
            Iterator i = ts.iterator();
            while (i.hasNext()) {

                LaboTestItemID testID = (LaboTestItemID) i.next();

                if (hasSpecimen) {
                    buf.append(test.getTestValue(testID));

                } else {
                    buf.append(testID.getItemName());
                }
                buf.append(System.getProperty("line.separator"));
            }

            System.out.println(buf.toString());
        }
    }
    /**
     *
     * @return
     */
    @Override
    public String toString() {

        StringBuffer buf = new StringBuffer();

        if (sampleTime != null) {
            buf.append(sampleTime);
            buf.append(System.getProperty("line.separator"));
        }

        if (registTime != null) {
            buf.append(registTime);
            buf.append(System.getProperty("line.separator"));
        }

        if (reportTime != null) {
            buf.append(reportTime);
            buf.append(System.getProperty("line.separator"));
        }

        if (mmlConfirmDate != null) {
            buf.append(mmlConfirmDate);
            buf.append(System.getProperty("line.separator"));
        }

        if (reportStatus != null) {
            buf.append(reportStatus);
            buf.append(System.getProperty("line.separator"));
        }

        if (testCenterName != null) {
            buf.append(testCenterName);
            buf.append(System.getProperty("line.separator"));
        }

        if (set != null) {
            buf.append(set);
            buf.append(System.getProperty("line.separator"));
        }

        if (simpleLaboTests != null) {
            for (int i = 0; i < simpleLaboTests.size(); i++) {
                SimpleLaboTest test = (SimpleLaboTest) simpleLaboTests.get(i);
                buf.append(test.toString());
            }
        }

        return buf.toString();
    }
}
