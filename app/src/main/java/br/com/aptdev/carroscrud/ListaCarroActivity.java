package br.com.aptdev.carroscrud;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import br.com.aptdev.carroscrud.dao.CarroDAO;
import br.com.aptdev.carroscrud.model.Carro;

public class ListaCarroActivity extends AppCompatActivity {

    ListView listaCarros;
    ArrayAdapter<Carro> listAdapterCarro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_carro);

        listaCarros = (ListView) findViewById(R.id.list_carros);

        FloatingActionButton btnAddCarro = (FloatingActionButton) findViewById(R.id.add_carro);
        btnAddCarro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent formIntent = new Intent(ListaCarroActivity.this,FormCarroActivity.class);
                startActivity(formIntent);
            }
        });

        listaCarros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Carro c = (Carro) parent.getItemAtPosition(position);
                Intent intentEditCarro = new Intent(ListaCarroActivity.this, FormCarroActivity.class);
                intentEditCarro.putExtra("carro", c);
                startActivity(intentEditCarro);
            }
        });
        registerForContextMenu(listaCarros);
    }

    @Override
    protected void onResume() {
        super.onResume();

        carregarLista();

    }

    private void carregarLista() {
        CarroDAO carroDAO = new CarroDAO(this);
        List<Carro> listCarros = carroDAO.list();
        carroDAO.close();
        listAdapterCarro = new ArrayAdapter<Carro>(this,android.R.layout.simple_list_item_1,listCarros);
        listaCarros.setAdapter(listAdapterCarro);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Carro carro = (Carro) listaCarros.getItemAtPosition(info.position);

        MenuItem itemDelete = menu.add(R.string.btn_deletar);
        itemDelete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                CarroDAO carroDAO = new CarroDAO(ListaCarroActivity.this);
                carroDAO.delete(carro);
                carregarLista();
                return false;
            }
        });
    }
}
