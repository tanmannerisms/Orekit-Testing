// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.orekit.data.DataContext;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.errors.OrekitException;
import org.orekit.frames.FramesFactory;
import org.orekit.orbits.*;
import org.orekit.propagation.analytical.tle.TLE;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScale;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.Constants;
import org.orekit.utils.PVCoordinates;

import java.io.*;
import java.net.*;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Configure Orekit
        final File home       = new File(System.getProperty("user.home"));
        final File orekitData = new File(home, "orekit-data");
        if (!orekitData.exists()) {
            System.err.format(Locale.US, "Failed to find %s folder%n",
                    orekitData.getAbsolutePath());
            System.err.format(Locale.US, "You need to download %s from %s, unzip it in %s and rename it 'orekit-data' for this tutorial to work%n",
                    "orekit-data-master.zip", "https://gitlab.orekit.org/orekit/orekit-data/-/archive/master/orekit-data-master.zip",
                    home.getAbsolutePath());
            System.exit(1);
        }
        final DataProvidersManager manager = DataContext.getDefault().getDataProvidersManager();
        manager.addProvider(new DirectoryCrawler(orekitData));

        // Initiate connection
        URI uri = null;
        String[] tle = new String[2];
        Scanner in;
        try {
            uri = new URI("https://celestrak.org/NORAD/elements/gp.php?CATNR=25544");
            URL url = uri.toURL();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            int responseCode = con.getResponseCode();
            System.out.println("Response code: " + responseCode);
            in = new Scanner(con.getInputStream());
            in.nextLine();
            for (int i = 0; i < 2; i++) {
                tle[i] = in.nextLine();
            }
            in.close();

       } catch (MalformedURLException | URISyntaxException e) {
            assert uri != null;
            System.out.printf("URL: \"%s\" is invalid.%n", uri);
            System.out.println(e.getMessage());

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        // Use TLE data to create TLE object.
        if (TLE.isFormatOK(tle[0], tle[1])) {
            TLE satellite = new TLE(tle[0], tle[1], TimeScalesFactory.getUTC());
        } else {
            System.out.println("TLE format not accepted");
        }

/*        Vector3D position = new Vector3D();
        Vector3D velocity = new Vector3D();
        AbsoluteDate initialDate = new AbsoluteDate(2023, 10, 18, 0, 0, 0, TimeScalesFactory.getUTC());

        KeplerianOrbit orbit = new KeplerianOrbit(
                new PVCoordinates(position, velocity),
                FramesFactory.getEME2000(),
                initialDate,
                Constants.EIGEN5C_EARTH_MU
        );*/
    }
}