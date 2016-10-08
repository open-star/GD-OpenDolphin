package open.dolphin.infomodel;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author oda
 */
public class BundleDolphinTest {

    private BundleDolphin bundle;

    /**
     *
     */
    @Before
    public void setUp() {
        bundle = new BundleDolphin();
    }

    /**
     *
     */
    @Test
    public void testGetItemNames() {
        assertNull(bundle.getItemNames());

        ClaimItem item0 = new ClaimItem();
        item0.setName("foo");
        bundle.addClaimItem(item0);
        assertEquals("foo", bundle.getItemNames());
        ClaimItem item1 = new ClaimItem();
        item1.setCode("840000000");
        item1.setName("bar00-00");
        item1.setNumber("33-33");
        bundle.addClaimItem(item1);
        assertEquals("foo, bar00-00 33-33", bundle.getItemNames());
    }
}
