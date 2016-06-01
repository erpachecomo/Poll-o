package mx.edu.ittepic.poll_o;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Neto on 29/05/2016.
 */
public class ItemCompraAdapter extends BaseAdapter {
    protected Activity activity;
    protected ArrayList<Encuesta_detalle> items;

    public ItemCompraAdapter(Activity activity, ArrayList<Encuesta_detalle> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    public String getItemNombre(int position){
        return items.get(position).getNombre();
    }

    public String getItemCompania(int position){
        return items.get(position).getCompania();
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        View vi=contentView;

        if(contentView == null) {
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.list_item_layout, null);
        }

        Encuesta_detalle item = items.get(position);

        TextView nombre = (TextView) vi.findViewById(R.id.nombre);
        nombre.setText(item.getCompania());

        TextView tipo = (TextView) vi.findViewById(R.id.tipo);
        tipo.setText(item.getNombre());

        return vi;
    }
}
