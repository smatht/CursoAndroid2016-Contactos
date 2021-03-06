package sticchi.matias.practico6;

/**
 * Created by MatiasGui on 10/1/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by MatiasGui on 9/22/2016.
 */
public class MostrarContactoAdapter extends BaseAdapter {
    private Activity activity;
    protected List lista;
    private static LayoutInflater inflater=null;

    public MostrarContactoAdapter(Activity a, List<Contacto> list) {
        activity = a;
        this.lista = list;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return lista.size();
    }

    public List getData(){
        return this.lista;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        Contacto contacto;
        if(convertView==null)
            vi = inflater.inflate(R.layout.fila_lista, null);

        TextView title = (TextView) vi.findViewById(R.id.title); // title
        TextView telefono = (TextView)vi.findViewById(R.id.items); // items de la pizza
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image


        contacto = (Contacto) lista.get(position);

        // Setting all values in listview
        title.setText(contacto.getNombre()+" "+contacto.getApellido());
        telefono.setText(contacto.getTelefono());

        return vi;
    }
}
