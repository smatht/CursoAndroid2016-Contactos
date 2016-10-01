package sticchi.matias.practico6;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner filtro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addWidgets();
        addListeners();
        addAdapters();
    }

    private void addWidgets() {
        this.filtro = (Spinner) findViewById(R.id.spinner);
    }

    private void addAdapters() {
        ArrayAdapter<CharSequence> adtFiltro = ArrayAdapter.createFromResource(
                this, R.array.filtro, android.R.layout.simple_spinner_item);
        adtFiltro.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.filtro.setAdapter(adtFiltro);
    }

    private void addListeners() {
        this.filtro.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
