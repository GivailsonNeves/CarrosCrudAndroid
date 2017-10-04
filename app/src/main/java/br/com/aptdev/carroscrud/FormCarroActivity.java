package br.com.aptdev.carroscrud;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.aptdev.carroscrud.dao.CarroDAO;
import br.com.aptdev.carroscrud.model.Acessorio;
import br.com.aptdev.carroscrud.model.Carro;
import br.com.aptdev.carroscrud.ui.AcessorioDialog;

public class FormCarroActivity extends AppCompatActivity {

    Carro carro;
    AcessorioDialog df;
    List<Acessorio> acessorios;
    ArrayAdapter<Acessorio> arrAcessorios;
    ListView listAcessorios;

    EditText tfModelo;
    EditText tfAnoModelo;
    EditText tfAro;

    final int PRIMEIRO_CARRO_EM_SERIE = 1888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_carro);

        tfModelo = (EditText) findViewById(R.id.carro_modelo);
        tfAnoModelo = (EditText) findViewById(R.id.carro_ano);
        tfAro = (EditText) findViewById(R.id.carro_aro);

        listAcessorios = (ListView) findViewById(R.id.lista_acessorios);

        df = new AcessorioDialog();

        Intent intent = getIntent();
        carro = (Carro) intent.getSerializableExtra("carro");

        if (carro != null) {
            CarroDAO carroDAO = new CarroDAO(FormCarroActivity.this);
            acessorios = carroDAO.getListAcessorios(carro.getId());
            carroDAO.close();
            carro.setAcessorios(acessorios);
            prepareForm();

        } else {
            carro = new Carro();
            acessorios = new ArrayList<Acessorio>();
        }

        showAcessorios();

        Button btnAddAcessorio = (Button) findViewById(R.id.btn_add_acessorio);
        btnAddAcessorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FormCarroActivity.this.df.show(getFragmentManager(), "Acessorios");

            }
        });

        registerForContextMenu(listAcessorios);

        listAcessorios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Acessorio acessorio = (Acessorio) parent.getItemAtPosition(position);
                acessorio.setId(id);
                FormCarroActivity.this.df.show(getFragmentManager(), "Acessorios", acessorio);

            }
        });

    }

    private void prepareForm() {

        tfModelo.setText(carro.getNomeModelo());
        tfAnoModelo.setText(carro.getAnoModelo().toString());
        tfAro.setText(carro.getAroRoda().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_form, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_salvar:
                if(isFormValid()) {
                    CarroDAO carroDAO = new CarroDAO(FormCarroActivity.this);
                    carro = getCarro();
                    if (carro.getId() != null) {
                        carroDAO.update(carro);
                    } else {
                        carroDAO.insert(carro);
                    }
                    carroDAO.close();
                    finish();
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isFormValid() {

        if( tfAnoModelo.getText().toString().equals("")
                || tfAro.getText().toString().equals("")
                || tfModelo.getText().toString().equals(""))
        {
            Toast.makeText(this, R.string.campos_invalidos, Toast.LENGTH_SHORT).show();
            return false;
        }



        int proximoAno = Calendar.getInstance().get(Calendar.YEAR) + 1;
        int ano = Integer.parseInt(tfAnoModelo.getText().toString());
        if(ano < PRIMEIRO_CARRO_EM_SERIE || ano > proximoAno)
        {
            Toast.makeText(this, R.string.carro_ano_invalido, Toast.LENGTH_SHORT).show();
            return false;
        }


        int aro = Integer.parseInt(tfAro.getText().toString());
        if(aro == 0){
            Toast.makeText(this, R.string.carro_aro_invalido, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void showAcessorios() {
        arrAcessorios = new ArrayAdapter<Acessorio>(this, android.R.layout.simple_list_item_1, acessorios);
        listAcessorios.setAdapter(arrAcessorios);
    }

    private Carro getCarro() {


        carro.setNomeModelo(tfModelo.getText().toString());
        carro.setAnoModelo(Integer.parseInt(tfAnoModelo.getText().toString()));
        carro.setAroRoda(Integer.parseInt(tfAro.getText().toString()));
        carro.setAcessorios(this.acessorios);

        return carro;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Acessorio acessorio = (Acessorio) listAcessorios.getItemAtPosition(info.position);

        MenuItem itemDeletar = menu.add(R.string.btn_deletar);
        itemDeletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                arrAcessorios.remove(acessorio);
                showAcessorios();
                return false;
            }
        });
    }

    public void closeDialog() {
        df.dismiss();
    }

    public void addAcessorio(Acessorio acessorio) {
        if(validateAcessorio(acessorio)) {
            this.acessorios.add(acessorio);
            showAcessorios();
            df.dismiss();
        }else{
            Toast.makeText(FormCarroActivity.this, R.string.campos_invalidos_acessorio_add, Toast.LENGTH_LONG).show();
        }
    }

    public void updateAcessorio(Acessorio acessorio) {
        if(validateAcessorio(acessorio)) {

            this.acessorios.remove(acessorio.getId().intValue());
            this.acessorios.add(acessorio);
            showAcessorios();

            df.dismiss();
        }else{
            Toast.makeText(FormCarroActivity.this, R.string.campos_invalidos_acessorio_update, Toast.LENGTH_LONG).show();
        }
    }

    private boolean validateAcessorio(Acessorio acessorio) {

        return !acessorio.getNomeAcessorio().isEmpty();
    }

    public void feedBack(int campos_invalidos_acessorio) {
        Toast.makeText(FormCarroActivity.this, campos_invalidos_acessorio, Toast.LENGTH_LONG).show();
    }
}
