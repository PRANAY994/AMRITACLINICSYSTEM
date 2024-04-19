import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date());
    }
}