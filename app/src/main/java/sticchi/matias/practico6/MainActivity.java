package sticchi.matias.practico6;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private final Context context = this;

    private final String nombreArchivo = "contactos.json";
    private Spinner filtro;
    private List<Contacto> listaContactos;
    private List<Contacto> listaFiltrada;
    private int filtroSelected = 0;
    private ListView list;
    private MostrarContactoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createFile();
        addWidgets();
        readContacts();
        addListeners();
        addAdapters();
        addContextMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
//                Toast.makeText(MainActivity.this, "Funcion no implementada", Toast.LENGTH_SHORT).show();
                LayoutInflater li = LayoutInflater.from(context);
                View addContactView = li.inflate(R.layout.new_contact, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);
                alertDialogBuilder.setView(addContactView);
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {

                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                alertDialogBuilder.create();
                alertDialogBuilder.show();
                return true;
            case R.id.menu_close:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    private void addContextMenu() {
        registerForContextMenu(list);
    }

    // TODO: 9/30/2016
    // FIXME: 9/30/2016
    ///////////////////////////////////////////////////////////////////////////
    // Error: El titulo del cuadro de menu no coloca correctamente el nombre
    // del contacto cuando la lista se filtra (por familia por ej).
    ///////////////////////////////////////////////////////////////////////////
    // FIXED: 9/30/2016
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_ctx, menu);
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo)menuInfo;

        int contactoID = Integer.parseInt(list.getAdapter().getItem(info.position).toString());
        Contacto c = listaFiltrada.get(contactoID);

        menu.setHeaderTitle(c.getNombre()+" "+c.getApellido());

        inflater.inflate(R.menu.menu_ctx, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int contactoID = Integer.parseInt(list.getAdapter().getItem(info.position).toString());
        Contacto c = listaFiltrada.get(contactoID);

        switch (item.getItemId()) {
            case R.id.CtxOpc1:
                String uri = "tel:"+c.getTelefono();
                call(uri);
                return true;
            case R.id.CtxOpc2:
                Toast.makeText(MainActivity.this, c.toString(), Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void call(String uri) {
        Intent in=new Intent(Intent.ACTION_CALL, Uri.parse(uri));
        try{
            startActivity(in);
        }

        catch (android.content.ActivityNotFoundException ex){
            Toast.makeText(getApplicationContext(),"No se puede realizar la llamada",Toast.LENGTH_SHORT).show();
        }
    }

    private void readContacts() {
        try
        {
            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    openFileInput(nombreArchivo)));

            String texto = fin.readLine();
            fin.close();

            deserializarContactosJson(texto);
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al leer fichero desde memoria interna");
        }
    }

    private void deserializarContactosJson(String contactosJson) {
        Gson gson = new Gson();
        Type tipoListaContactos = new TypeToken<List<Contacto>>(){}.getType();
        this.listaContactos = gson.fromJson(contactosJson, tipoListaContactos);
        this.listaFiltrada = gson.fromJson(contactosJson, tipoListaContactos);

//        assertNotNull(this.listaContactos);
//        assertEquals(5,this.listaContactos.size());
    }

    private void createFile() {
        File f;
        Gson gson = new Gson();
        try
        {
            f = new File(nombreArchivo);
            if (f.exists())
                System.out.println("///////////////////////////////////////////////////////////\n" +
                        "El fichero existe\n" +
                        "//////////////////////////////////////////////////////////////////////\n");
            else {
                System.out.println("/////////////////////////////////////////////////////////////\n" +
                        "Pues va a ser que no\n" +
                        "/////////////////////////////////////////////////////////////////////////\n");
                final Contacto c1 = new Contacto("Miguel", "Alvarez","1234567898", "Amigos");
                final Contacto c2 = new Contacto("Marcelo", "Revel", "666666666", "Amigos");
                final Contacto c3 = new Contacto("Florencia", "Perez", "3794568978", "Familia");
                final Contacto c4 = new Contacto("Rolando", "Larania", "707070707070");
                final Contacto c5 = new Contacto("Matias", "Sticchi", "379-4589388", "Familia");

                final String json = "[" + gson.toJson(c1) + "," + gson.toJson(c2) + "," +
                        gson.toJson(c3) + "," + gson.toJson(c4) + "," + gson.toJson(c5) + "]";

                OutputStreamWriter fileJson= new OutputStreamWriter(
                        openFileOutput(nombreArchivo, Context.MODE_PRIVATE));
                fileJson.write(json);
                fileJson.close();
            }
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Algun error por aji");
        }
    }

    private void addWidgets() {
        this.filtro = (Spinner) findViewById(R.id.spinner);
        this.list=(ListView)findViewById(R.id.list);
    }

    private void addAdapters() {
        ArrayAdapter<CharSequence> adtFiltro = ArrayAdapter.createFromResource(
                this, R.array.filtro, android.R.layout.simple_spinner_item);
        adtFiltro.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.filtro.setAdapter(adtFiltro);

        adapter=new MostrarContactoAdapter(this, listaFiltrada);
        list.setAdapter(adapter);
    }

    private void addListeners() {
        this.filtro.setOnItemSelectedListener(this);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /////////////////////////////////////////////////////////////////////////////
                // Si el elemento clickeado no esta seleccionado lo selecciona,
                // caso contrario lo deselecciona.
                /////////////////////////////////////////////////////////////////////////////
                for (int i = 0; i < list.getChildCount(); i++) {
                    if(position == i ){
                        if (list.getChildAt(position).getTag() == null) {
                            list.getChildAt(position).setBackground(getResources().getDrawable(
                                    R.drawable.gradient_bg_hover));
                            list.getChildAt(position).setTag(R.drawable.gradient_bg_hover);
                        }
                        else
                        {
                            list.getChildAt(position).setBackground(getResources().getDrawable(
                                    R.drawable.gradient_bg));
                            list.getChildAt(position).setTag(null);
                        }
                    }else{
                        list.getChildAt(i).setBackground(getResources().getDrawable(
                                R.drawable.gradient_bg));
                        list.getChildAt(i).setTag(null);
                    }

                }

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        reloadData(i);
    }

    private void reloadData(int filtro) {
        listaFiltrada = new ArrayList<Contacto>();
        for (Contacto c : this.listaContactos) {
            if (filtro == 0) {
                listaFiltrada.addAll(this.listaContactos);
                break;
            }
            else
            if (c.getGrupo() == filtro)
                listaFiltrada.add(c);
        }
        this.adapter.getData().clear();
        this.adapter.getData().addAll(listaFiltrada);
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
