package cn.roy.zlib.module_encrypt;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testJavaAES() {
        String key = "tbalInsuranceBox";
        String encrypt = AESUtils.encrypt("VSHH21031989140", key);
        System.out.println("加密结果：" + encrypt);
        String encrypt2 = AESUtils.encrypt("120101198002010014", key);
        System.out.println("加密结果：" + encrypt2);
    }

}