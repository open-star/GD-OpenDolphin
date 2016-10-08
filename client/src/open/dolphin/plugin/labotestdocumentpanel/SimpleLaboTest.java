/*
 * SimpleLaboTest.java
 *
 * Copyright (C) 2003-2004 Digital Globe, Inc. All rights reserved.
 */
package open.dolphin.plugin.labotestdocumentpanel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

/**
 * １検体につき、テストされた項目の TreeSet を保持するクラス。
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public class SimpleLaboTest implements Comparable {

    /** 検体 */
    private SimpleLaboSpecimen specimen;
    /** テスト項目の TreeSet 要素は SimpleLaboTestItem */
    private TreeSet testItemTreeSet;

    /** Creates a new instance of SimpleLaboTest */
    public SimpleLaboTest() {
    }

    /**
     *
     * @return
     */
    public SimpleLaboSpecimen getSimpleSpecimen() {
        return specimen;
    }

    /**
     *
     * @param val
     */
    public void setSimpleSpecimen(SimpleLaboSpecimen val) {
        specimen = val;
    }

    /**
     *
     * @return　
     */
    public TreeSet getTestItemTreeSet() {
        return testItemTreeSet;
    }

    /**
     *
     * @param val
     */
    public void setTestItemTreeSet(TreeSet val) {
        testItemTreeSet = val;
    }

    /**
     *
     * @param val
     */
    @SuppressWarnings("unchecked")
    public void addSimpleLaboTestItem(SimpleLaboTestItem val) {

        if (testItemTreeSet == null) {
            testItemTreeSet = new TreeSet();
        }

        testItemTreeSet.add(val);
    }

    @Override
    public int compareTo(Object obj) {

        SimpleLaboTest other = (SimpleLaboTest) obj;

        String str1 = specimen.getSpecimenName();
        String str2 = other.getSimpleSpecimen().getSpecimenName();

        return str1.compareTo(str2);
    }

    /**
     *
     * @param other
     * @return 検体か
     */
    public boolean isSpecimen(SimpleLaboSpecimen other) {

        return specimen.equals(other);
    }

    /**
     *
     * @param testID
     * @return
     */
    public Object getTestItem(LaboTestItemID testID) {

        Object ret = null;

        if (testItemTreeSet == null) {
            return ret;
        }

        Iterator iter = testItemTreeSet.iterator();
        SimpleLaboTestItem testItem;

        while (iter.hasNext()) {

            testItem = (SimpleLaboTestItem) iter.next();
            if (testItem.isTest(testID)) {
                ret = (Object) testItem;
                break;
            }
        }

        //if (ret == null) {
        //ret = testID.getItemName();
        //}

        return ret;
    }

    /**
     *
     * @param testID
     * @return　テスト値
     */
    public String getTestValue(LaboTestItemID testID) {

        String ret = null;

        if (testItemTreeSet == null) {
            return ret;
        }

        Iterator iter = testItemTreeSet.iterator();
        boolean hasTest = false;
        SimpleLaboTestItem testItem = null;

        while (iter.hasNext()) {

            testItem = (SimpleLaboTestItem) iter.next();
            if (testItem.isTest(testID)) {
                hasTest = true;
                break;
            }
        }

        if (hasTest) {
            ret = testItem.toString();
        } else {
            ret = testID.getItemName();
        }

        return ret;
    }

    /**
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public List getSimpleLaboTestItem() {

        List ret = null;

        if (testItemTreeSet == null) {
            return ret;
        }

        Iterator iter = testItemTreeSet.iterator();
        //SimpleLaboTestItem testItem;
        ret = new ArrayList();

        while (iter.hasNext()) {

            ret.add((SimpleLaboTestItem) iter.next());
        }

        return ret;
    }

    @Override
    public String toString() {

        StringBuffer buf = new StringBuffer();

        buf.append(" ");
        buf.append(System.getProperty("line.separator"));
        buf.append(specimen.toString());
        buf.append(System.getProperty("line.separator"));

        if (testItemTreeSet != null) {

            Iterator iter = testItemTreeSet.iterator();
            while (iter.hasNext()) {

                SimpleLaboTestItem item = (SimpleLaboTestItem) iter.next();

                buf.append(item.toString());

                buf.append(System.getProperty("line.separator"));
            }
        }

        return buf.toString();

    }
}
