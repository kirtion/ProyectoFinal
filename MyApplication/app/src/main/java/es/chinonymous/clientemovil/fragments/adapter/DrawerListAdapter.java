package es.chinonymous.clientemovil.fragments.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import es.chinonymous.clientemovil.R;

public class DrawerListAdapter extends BaseAdapter {

    private Context context;
    private List<DrawerOption> options;

    public DrawerListAdapter(Context context, List<DrawerOption> options) {
        this.context = context;
        this.options = options;
    }

    @Override
    public int getCount() {
        return options.size();
    }

    @Override
    public DrawerOption getItem(int position) {
        return options.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)  {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_drawer, null);
        }

        DrawerOption opcion = options.get(position);
        ImageView iv = (ImageView) convertView.findViewById(R.id.iconDrawer);
        iv.setImageDrawable(opcion.getImage());

        TextView tv = (TextView) convertView.findViewById(R.id.drawerOptionText);
        tv.setText(opcion.getNombre());

        return convertView;
    }
}
