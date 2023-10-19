// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.orekit.data.DataContext;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.errors.OrekitException;
import org.orekit.frames.FramesFactory;
import org.orekit.gnss.attitude.GPSBlockIIA;
import org.orekit.orbits.*;
import org.orekit.propagation.analytical.tle.SGP4;
import org.orekit.propagation.analytical.tle.TLE;
import org.orekit.propagation.analytical.tle.TLEPropagator;
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

        // Initiate connection to Celestrak.org's page for ISS TLE data.
        // Attempt to parse the data using the standard convention that Celestrak uses
        // E.G.
        // ISS (ZARYA)
        // 1 25544U 98067A   23291.87631728  .00018713  00000+0  33066-3 0  9999
        // 2 25544  51.6420  75.8545 0004681 115.6786  16.3560 15.50372001420974
        //
        URI uri = null;
        String[] tle = new String[2];
        Scanner in;
        int PRN = 0;
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
        TLE tleObj = null;
        if (TLE.isFormatOK(tle[0], tle[1])) {
            tleObj = new TLE(tle[0], tle[1], TimeScalesFactory.getUTC());
        } else {
            System.out.println("TLE format not accepted");
        }

        if (tleObj != null) {
            TLEPropagator propagator = new SGP4(tleObj, GPSBlockIIA.getDefaultYawRate(PRN), 1000);
        }


   }
}