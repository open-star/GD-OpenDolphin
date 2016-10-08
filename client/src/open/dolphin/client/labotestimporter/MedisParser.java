package open.dolphin.client.labotestimporter;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import open.dolphin.log.LogWriter;

/**
 * Medisデータの取り込み
 * @author kenji
 */
public class MedisParser {

    private static final String RESULT_INFO = "A1";
    private static final String AVERAGE_LEVEL = "S1";
    private String recordType = null;
    // 固定長テキストの桁数の配列
    private static final int[] COMMON_HEADER_SIZES = {2, 6, 20, 8, 12, 20};
    private static final int[] RESULT_HEADER_SIZES = {1, 3, 3, 3};
    private static final int[] RESULT_BODY_SIZES = {17, 8, 1, 3, 3};
    private static final int[] RESULT_FOOTER_SIZES = {8, 2, 1, 1, 1, 1, 1, 1, 2};
    private static final int[] AVERAGE_LEVEL_SIZES = {1, 8, 8, 12, 8};
    private static final int[] AVERAGE_FOOTER_SIZES = {3};
    // 改行が CRLF なので 256 + 2 byte になる
    private static final int BUF_SIZE = 258;
    private LaboTestInformation laboTestInformation;

    /**
     * コンストラクタ
     */
    public MedisParser() {
        recordType = RESULT_INFO;
    }

    /**
     * 
     * @param is パーサへの入力
     * @return 検査データの集合
     * @throws IOException
     */
    public List<LaboTestInformation> parse(InputStream is) throws IOException {
        List<LaboTestInformation> result = new ArrayList<LaboTestInformation>();
        byte[] buf = new byte[BUF_SIZE];
        int len = 0;
        int lineNum = 1;//MEMO: unused?

        while ((len = is.read(buf)) != -1 && len == BUF_SIZE) {
            if (parseLine(buf)) {
                result.add(laboTestInformation);
            }
            lineNum++;
        }
        return result;
    }

    /**
     * 一行パース
     * @param ba
     * @return
     */
    private boolean parseLine(final byte[] ba) {
        int pos = 0;
        boolean result = false;
        if (RESULT_INFO.equals(recordType)) {
            laboTestInformation = new LaboTestInformation();
            pos = parseCommonHeader(ba, pos);
            pos = parseResultHeader(ba, pos);
            pos = parseResultBody(ba, pos);
            pos = parseResultFooter(ba, pos);
            recordType = AVERAGE_LEVEL;
            result = false;
        } else if (AVERAGE_LEVEL.equals(recordType)) {
            pos = parseCommonHeader(ba, pos);
            pos = parseAverageLevel(ba, pos);
            pos = parseAverageFooter(ba, pos);
            recordType = RESULT_INFO;
            result = true;
        }
        return result;
    }

    /**
     * Medisデータのヘッダ部パース
     * @param ba
     * @param from
     * @return
     */
    private int parseCommonHeader(final byte[] ba, int from) {
        ListPosition listPosition = getListPosition(ba, COMMON_HEADER_SIZES, from);
        List<String> header = listPosition.getList();
        recordType = header.get(0);
        laboTestInformation.setCenterCode(header.get(1));

        String sampledate = header.get(2);
        laboTestInformation.setSampleTime(sampledate); // 採取日YYYYMMDD + 患者ID

        String confirmeddate = header.get(3);
        if (confirmeddate.trim().isEmpty()) {//confirmeddateが空ならsampledateで置き換える
            confirmeddate = sampledate;
        }
        laboTestInformation.setConfirmedDate(confirmeddate);

        laboTestInformation.setCommitionedKey(header.get(4));
        laboTestInformation.setPatiantName(header.get(5));
        return listPosition.getPosition();
    }

    /**
     * 
     * @param ba
     * @param from
     * @return
     */
    private int parseResultHeader(final byte[] ba, int from) {
        ListPosition listPosition = getListPosition(ba, RESULT_HEADER_SIZES, from);
        List<String> header = listPosition.getList();
        laboTestInformation.setStatus(header.get(0));
        laboTestInformation.setMilk(header.get(1));
        laboTestInformation.setHemolytic(header.get(2));
        laboTestInformation.setBilirubin(header.get(3));
        return listPosition.getPosition();
    }

    /**
     *
     * @param ba
     * @param from
     * @return
     */
    private int parseResultBody(final byte[] ba, int from) {
        ListPosition listPosition = null;
        List<String> body = null;
        for (int n = 0; n < 5; n++) {
            listPosition = getListPosition(ba, RESULT_BODY_SIZES, from);
            body = listPosition.getList();
            from = listPosition.getPosition();
            laboTestInformation.addTestResultInformation(new LaboTestResultInformation(
                    body.get(0),
                    body.get(1),
                    body.get(2),
                    body.get(3),
                    body.get(4)));
        }
        return listPosition.getPosition();
    }

    /**
     *
     * @param ba
     * @param from
     * @return
     */
    private int parseResultFooter(final byte[] ba, int from) {
        ListPosition listPosition = getListPosition(ba, RESULT_FOOTER_SIZES, from);
        List<String> footer = listPosition.getList();
        laboTestInformation.setRegistTime(footer.get(0));
        laboTestInformation.setAbnormalValueFlag(footer.get(2));
        laboTestInformation.addAbnormalValue(footer.get(3));
        laboTestInformation.addAbnormalValue(footer.get(4));
        laboTestInformation.addAbnormalValue(footer.get(5));
        laboTestInformation.addAbnormalValue(footer.get(6));
        laboTestInformation.addAbnormalValue(footer.get(7));
        return listPosition.getPosition();
    }

    /**
     *
     * @param ba
     * @param from
     * @return
     */
    private int parseAverageLevel(final byte[] ba, int from) {
        ListPosition listPosition = null;
        List<String> body = null;
        for (int n = 0; n < 5; n++) {
            listPosition = getListPosition(ba, AVERAGE_LEVEL_SIZES, from);
            body = listPosition.getList();
            from = listPosition.getPosition();
            laboTestInformation.addAverageInformation(new AverageInformation(
                    body.get(0),
                    body.get(1),
                    body.get(2),
                    body.get(3)));
        }

        return listPosition.getPosition();
    }

    /**
     *
     * @param ba
     * @param from
     * @return
     */
    private int parseAverageFooter(final byte[] ba, int from) {
        // 未使用なので取得したデータを使用しない
        ListPosition listPosition = getListPosition(ba, AVERAGE_FOOTER_SIZES, from);
        return listPosition.getPosition();
    }

    /**
     *
     * @param ba
     * @param sizes
     * @param start
     * @return
     */
    private ListPosition getListPosition(final byte[] ba, final int[] sizes, final int start) {
        int from = start;
        int to = start;
        List<String> result = new ArrayList<String>();
        for (int size : sizes) {
            to += size;
            try {
                byte[] a = Arrays.copyOfRange(ba, from, to);
                String str = new String(a, "Windows-31J");
                result.add(str);
            } catch (UnsupportedEncodingException ex) {
                LogWriter.error(getClass(), ex);
            }
            from += size;
        }
        return new ListPosition(result, to);
    }

    /**
     *
     */
    private class ListPosition {

        private List<String> list;
        private int position;

        /**
         *
         * @param list
         * @param position
         */
        public ListPosition(List<String> list, int position) {
            this.list = list;
            this.position = position;
        }

        /**
         *
         * @return
         */
        public List<String> getList() {
            return list;
        }

        /**
         *
         * @return
         */
        public int getPosition() {
            return position;
        }
    }
}
