package Models;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.view.View;

public class Notifications {
    private String title;
    private double temps;

    public Notifications() {
    }

    public Notifications(String title, double temps) {
        this.title = title;
        this.temps = temps;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getTemps() {
        return temps;
    }

    public void setTemps(double temps) {
        this.temps = temps;
    }


}
