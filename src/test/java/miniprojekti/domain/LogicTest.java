package miniprojekti.domain;

import java.util.ArrayList;
import java.util.List;
import miniprojekti.data_access.Dao;
import miniprojekti.data_access.ReadingTipDao;
import org.junit.*;
import org.mockito.Matchers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class LogicTest {
    ReadingTipDao testDao;

    Dao tipDao = new Dao() {
        public List<ReadingTip> testTips = new ArrayList<>();

        @Override
        public List<ReadingTip> findAll() {

            testTips.add(new ReadingTip(1, "testAuthor", "testTitle", "https://www.google.com"));
            testTips.add(new ReadingTip(2, "testAuthor2", "testTitle2", "https://www.wikipedia.org"));
            testTips.add(new ReadingTip(3, "testAuthor3", "testTitle3", "https://www.ohjelmistotuotanto-hy.github.io"));

            return testTips;
        }

        @Override
        public void save(String author, String title, String url) {
            testTips.add(new ReadingTip(0, author, title, url)); // poistettu random-viite, tähän voi keksiä
                                                                 // testiviitteen
        }

    };

    public LogicTest() {
    }

    @Before
    public void setUp() {
        testDao = mock(ReadingTipDao.class);
    }

    @Test
    public void newTipIsSaved() {
        Logic l = new Logic(testDao);
        l.saveNewTip("testAuthor", "testTitle", "testUrl");
        verify(testDao).save(eq("testAuthor"), eq("testTitle"), eq("testUrl"));
    }

    @Test
    public void tipsAreListedDao() {
        List list = tipDao.findAll();
        assertEquals("testTitle by testAuthor: https://www.google.com", list.get(0).toString());
    }
}
