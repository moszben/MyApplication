package Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.moszben.alive.R;

import java.util.List;

import Models.Notifications;

/**
 * Created by moszben on 08/06/2017.
 */

public class ListAdapter extends ArrayAdapter<Notifications> {
    Context myContext;
    int layoutResourceId;
    List<Notifications> notifications;

    public ListAdapter(Context context, int resource, List<Notifications> objects) {
        super(context, resource, objects);
        myContext=context;
        layoutResourceId=resource;
        notifications=objects;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position,convertView,parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater = ((Activity) myContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        Notifications current=notifications.get(position);
        TextView title = (TextView) convertView.findViewById(R.id.notifTitle);
        TextView temps = (TextView) convertView.findViewById(R.id.temps);

        title.setText(current.getTitle());
        String t=String.valueOf(current.getTemps());
        temps.setText(t);
        return convertView;
    }
}
