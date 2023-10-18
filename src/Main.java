// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.orekit.frames.FramesFactory;
import org.orekit.orbits.*;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScale;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.Constants;
import org.orekit.utils.PVCoordinates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class Main {
    public static void main(String[] args) {
        URI uri = null;
        try {
            uri = new URI("https://celestrak.org/NORAD/elements/gp.php?GROUP=gps-ops&FORMAT=tle");
            URL obj = uri.toURL();
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            int responseCode = con.getResponseCode();
            System.out.println("Response code: " + responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            String html = response.toString();
            System.out.println("HTML: " + html);

        } catch (MalformedURLException | URISyntaxException e) {
            assert uri != null;
            System.out.printf("URL: \"%s\" is invalid.%n", uri);
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
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