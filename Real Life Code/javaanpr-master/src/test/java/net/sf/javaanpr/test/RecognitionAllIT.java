package net.sf.javaanpr.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import javax.xml.parsers.ParserConfigurationException;
import net.sf.javaanpr.imageanalysis.CarSnapshot;
import net.sf.javaanpr.intelligence.Intelligence;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;

@RunWith(value = Parameterized.class)
public class RecognitionAllIT {

    private static Properties props;
    private static File[] snapshots;
    private static Intelligence intel;
    private static final Logger logger = LoggerFactory.getLogger(RecognitionAllIT.class);

    public static void setUp() throws FileNotFoundException, IOException, ParserConfigurationException, SAXException {
        logger.info("------- RUNNING: setUp() -------");
        intel = new Intelligence();

        String snapshotPath = "src/test/resources/snapshots";
        String resultsPath = "src/test/resources/results.properties";
        try (InputStream resultsStream = new FileInputStream(new File(resultsPath))) {
            props = new Properties();
            props.load(resultsStream);
        }
        File snapshotDir = new File(snapshotPath);
        snapshots = snapshotDir.listFiles();
        assertThat(snapshots, not(nullValue()));
    }
    
    @Parameter(value = 0)
    public String data;

    @Parameters
    public static List<String> data() throws IllegalArgumentException, IllegalArgumentException, FileNotFoundException, IOException, ParserConfigurationException, SAXException {
        setUp();
        List<String> listOfParams = new ArrayList();
        try {
            for (File snap : snapshots) {
                CarSnapshot carSnap = new CarSnapshot(new FileInputStream(snap));
                assertThat("carSnap is null", carSnap, not(nullValue()));
                assertThat("carSnap.image is null", carSnap.getImage(), not(nullValue()));
                String snapName = snap.getName();
                carSnap.close();
                listOfParams.add(intel.recognize(carSnap, false) + "," + props.getProperty(snapName));
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return listOfParams;
    }

    @Test
    public void testFile() {
        logger.info("------- RUNNING: testFile() -------");
        /*
        ** Regex will split on commas and consume any spaces on either side.
        */
        List<String> numberPlates = Arrays.asList(data.split("\\s*,\\s*"));
        assertThat(numberPlates.get(1), is(numberPlates.get(0)));
    }
}