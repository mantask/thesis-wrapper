package lt.kanaporis.thesis.changemodel;

import org.junit.Test;
import static org.junit.Assert.*;
import static lt.kanaporis.thesis.changemodel.NumberUtils.*;

public class NumberUtilsTest {

    @Test
    public void testEq() throws Exception {
        assertTrue(eq(1, 0.999_999_9));
        assertTrue(eq(1.0, 1.1 - 0.1));
        assertFalse(eq(1, 0.999_998));
        assertFalse(eq(0.000_009, 0.000_008));
    }

    @Test
    public void testLt() throws Exception {
        assertFalse(lt(0.999_999_9, 1));
        assertFalse(lt(1.1 - 0.1, 1.0));
        assertFalse(lt(1, 0.999_999_9));
        assertFalse(lt(1.0, 1.1 - 0.1));
        assertTrue(lt(0.999_998, 1));
        assertTrue(lt(0.000_008, 0.000_009));
    }

    @Test
    public void testGt() throws Exception {
        assertFalse(gt(0.999_999_9, 1));
        assertFalse(gt(1.1 - 0.1, 1.0));
        assertFalse(gt(1, 0.999_999_9));
        assertFalse(gt(1.0, 1.1 - 0.1));
        assertTrue(gt(1, 0.999_998));
        assertTrue(gt(0.000_009, 0.000_008));
    }

    @Test
    public void testLe() throws Exception {
        assertTrue(le(0.999_999_9, 1));
        assertTrue(le(1.1 - 0.1, 1.0));
        assertTrue(le(1, 0.999_999_9));
        assertTrue(le(1.0, 1.1 - 0.1));
        assertTrue(le(0.999_998, 1));
        assertTrue(le(0.000_008, 0.000_009));
    }
}
