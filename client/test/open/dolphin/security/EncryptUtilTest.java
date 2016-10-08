/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package open.dolphin.security;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author tomohiro
 */
public class EncryptUtilTest {

    /**
     *
     */
    public EncryptUtilTest() {
    }

    /**
     *
     */
    @Before
    public void setUp() {
    }

    /**
     *
     */
    @After
    public void tearDown() {
    }

    /**
     *
     */
    @Test
    public void testEncryptWithBlowfish() {
        System.out.println("Enctypt then Decode with Blowfish algorithm.");
        String originalText = "blowfish";
        byte[] encrypted = EncryptUtil.encryptWithBlowfish(originalText);
        String decodedText = EncryptUtil.decodeWithBlowfish(encrypted);
        assertEquals(originalText, decodedText);
    }

    /**
     *
     */
    @Test
    public void testCreatePasswordHash() {
        System.out.println("createPasswordHash");
        String originalText = "superNyankoTime";
        String encrypted = EncryptUtil.createPasswordHash(originalText);
        assertNotNull(encrypted);
        assert !originalText.equals(encrypted);
        assertEquals(encrypted, EncryptUtil.createPasswordHash(originalText));
    }
}
