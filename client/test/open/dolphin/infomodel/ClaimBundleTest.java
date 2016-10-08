package open.dolphin.infomodel;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author
 */
public class ClaimBundleTest {

    private ClaimBundle bundle;

    /**
     *
     */
    @Before
    public void setUp() {
        bundle = new ClaimBundle();
    }

    /**
     *
     */
    @Test
    public void testBundleNumber() {
        assertEquals(ClaimBundle.DEFAULT_BUNDLE_NUMBER, bundle.getBundleNumber());
        bundle.setBundleNumber("2");
        assertEquals("2", bundle.getBundleNumber());
        bundle.setBundleNumber(3);
        assertEquals("3", bundle.getBundleNumber());
        bundle.setBundleNumber(0); /* ignored */
        assertEquals("3", bundle.getBundleNumber());
    }
}
