package ids.androidsong;

import android.test.mock.MockContext;

import org.junit.Test;

import java.util.ArrayList;

import ids.androidsong.help.App;
import ids.androidsong.object.driveStatus;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        App.setContext(new MockContext());
        ArrayList<driveStatus> lista = new driveStatus().getNuevos();
        assertTrue(lista.size()>0);
    }
}