package open.dolphin.infomodel;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author
 */
public class SinryoCodeTest {

    /**
     *
     */
    @Test
    public void testIsComment() {
        assertTrue(SinryoCode.isComment("810000001"));
        assertTrue(SinryoCode.isComment("820000000"));
        assertTrue(SinryoCode.isComment("830000000"));
        assertTrue(SinryoCode.isComment("840000000"));

        assertFalse(SinryoCode.isComment("950000000"));
    }

    /**
     *
     */
    @Test
    public void testIsNoFormatComment() {
        assertTrue(SinryoCode.isNoFormatComment("810000001"));
        assertTrue(SinryoCode.isNoFormatComment("820000000"));

        assertFalse(SinryoCode.isNoFormatComment("810000002"));
        assertFalse(SinryoCode.isNoFormatComment("830000000"));
        assertFalse(SinryoCode.isNoFormatComment("840000000"));
    }

    /**
     *
     */
    @Test
    public void testIsPrefixComment() {
        assertTrue(SinryoCode.isPrefixComment("830000000"));

        assertFalse(SinryoCode.isPrefixComment("840000000"));
        assertFalse(SinryoCode.isPrefixComment("810000001"));
        assertFalse(SinryoCode.isPrefixComment("820000000"));
    }

    /**
     *
     */
    @Test
    public void testIsComplexComment() {
        assertTrue(SinryoCode.isComplexComment("840000000"));

        assertFalse(SinryoCode.isComplexComment("810000001"));
        assertFalse(SinryoCode.isComplexComment("820000000"));
        assertFalse(SinryoCode.isComplexComment("830000000"));
    }

    /**
     *
     */
    @Test
    public void testIsJihi() {
        assertTrue(SinryoCode.isJihi("096000000"));
        assertTrue(SinryoCode.isJihi("960000000"));
        assertTrue(SinryoCode.isJihi("095000000"));
        assertTrue(SinryoCode.isJihi("950000000"));

        assertFalse(SinryoCode.isJihi(null));
        assertFalse(SinryoCode.isJihi("95000"));
        assertFalse(SinryoCode.isJihi("990000000"));
        assertFalse(SinryoCode.isJihi("099000000"));
    }

    /**
     *
     */
    @Test
    public void testIsJihiHikazei() {
        assertTrue(SinryoCode.isJihiHikazei("095000000"));
        assertTrue(SinryoCode.isJihiHikazei("950000000"));

        assertFalse(SinryoCode.isJihiHikazei(null));
        assertFalse(SinryoCode.isJihiHikazei("95000"));
        assertFalse(SinryoCode.isJihiHikazei("990000000"));
        assertFalse(SinryoCode.isJihiHikazei("099000000"));
    }

    /**
     *
     */
    @Test
    public void testIsJihiKazei() {
        assertTrue(SinryoCode.isJihiKazei("096000000"));
        assertTrue(SinryoCode.isJihiKazei("960000000"));

        assertFalse(SinryoCode.isJihiKazei(null));
        assertFalse(SinryoCode.isJihiKazei("96000"));
        assertFalse(SinryoCode.isJihiKazei("990000000"));
        assertFalse(SinryoCode.isJihiKazei("099000000"));
    }

    /**
     *
     */
    @Test
    public void testIsZanryoHaiki() {
        assertTrue(SinryoCode.isZanryoHaiki("099309901"));

        assertFalse(SinryoCode.isZanryoHaiki(null));
        assertFalse(SinryoCode.isZanryoHaiki("09930"));
        assertFalse(SinryoCode.isZanryoHaiki("099409901"));
    }

    /**
     *
     */
    @Test
    public void testIsValid() {
        assertTrue(SinryoCode.isValid("000000000"));

        assertFalse(SinryoCode.isValid("00000000"));
        assertFalse(SinryoCode.isValid("0000000000"));
    }
}
