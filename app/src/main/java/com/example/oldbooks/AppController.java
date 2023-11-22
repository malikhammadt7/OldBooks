package com.example.oldbooks;

import com.example.oldbooks.manager.CoinManager;
import com.example.oldbooks.manager.FirebaseManager;
import com.example.oldbooks.manager.UserManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AppController {
    private static AppController instance;
    public static synchronized AppController getInstance() {
        if (instance == null) {
            instance = new AppController();
        }
        return instance;
    }
    public static String userId;
    private AppController() {
        addManager(FirebaseManager.class, new FirebaseManager());
        addManager(CoinManager.class, new CoinManager());
        addManager(UserManager.class, new UserManager());
    }

    private Map<Class<?>, Manager> managerMap = new HashMap<>();

    private void addManager(Class<?> managerClass, Manager manager) {
        managerMap.put(managerClass, manager);
    }
    @SuppressWarnings("unchecked")
    public <T extends Manager> T getManager(Class<T> managerClass) {
        return (T) managerMap.get(managerClass);
    }

//region Extra
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }
    public static String convertTimestampToDateTime(long timestamp) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = new Date(timestamp);
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error converting timestamp";
        }
    }
    public static String getRelativeTime(long timestamp) {
        try {
            long now = System.currentTimeMillis();
            long differenceMillis = now - timestamp;

            // Convert milliseconds to minutes, hours, and days
            long differenceMinutes = differenceMillis / (60 * 1000);
            long differenceHours = differenceMillis / (60 * 60 * 1000);
            long differenceDays = differenceMillis / (24 * 60 * 60 * 1000);

            if (differenceMinutes < 1) {
                return "just now";
            } else if (differenceMinutes == 1) {
                return "a minute ago";
            } else if (differenceHours < 1) {
                return differenceMinutes + " minutes ago";
            } else if (differenceHours == 1) {
                return "an hour ago";
            } else if (differenceDays < 1) {
                return differenceHours + " hours ago";
            } else if (differenceDays == 1) {
                return "yesterday";
            } else {
                // Format the date and time for older posts
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                Date date = new Date(timestamp);
                return sdf.format(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error calculating relative time";
        }
    }
//endregion Extra

    //region change Intent after verification
    public static boolean IsGuestView(){
        try {
            return AppController.getInstance().getManager(UserManager.class).isInitialized();
        } catch (Exception e) {
            return false;
        }
    }
    //endregion change Intent after verification
}
