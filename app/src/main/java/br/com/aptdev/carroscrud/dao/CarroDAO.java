package br.com.aptdev.carroscrud.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.com.aptdev.carroscrud.model.Acessorio;
import br.com.aptdev.carroscrud.model.Carro;

/**
 * Created by gneves on 27/06/2017.
 */

public class CarroDAO extends SQLiteOpenHelper {

    public CarroDAO(Context context) {
        super(context, "Carros", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlTBCarros = "CREATE TABLE Carros( id INTEGER PRIMARY KEY, modelo TEXT NOT NULL, ano INTEGER NOT NULL, aro INTEGER NOT NULL);";
        String sqlTBAcessorios = "CREATE TABLE Acessorios(id INTEGER PRIMARY KEY, nome TEXT NOT NULL, opcional INTEGER NOT NULL, carro_id INTEGER NOT NULL);";
        db.execSQL(sqlTBCarros);
        db.execSQL(sqlTBAcessorios);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE Carros; DROP TABLE Acessorios;";
        db.execSQL(sql);
        onCreate(db);
    }

    public void insert(Carro carro)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentCarro = getContentCarro(carro);
        long id = db.insert("Carros", null, contentCarro);
        carro.setId(id);
        setAcessorios(carro);
    }

    private ContentValues getContentCarro(Carro carro) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("modelo", carro.getNomeModelo());
        contentValues.put("ano", carro.getAnoModelo());
        contentValues.put("aro",carro.getAroRoda());

        return contentValues;
    }

    public void update(Carro carro)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentCarro = getContentCarro(carro);
        db.update("Carros", contentCarro, "id = ?", new String[] {carro.getId().toString()});
        db.delete("Acessorios", "carro_id = ?",new String[]{carro.getId().toString()});
        setAcessorios(carro);
    }

    public void delete(Carro carro)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentCarro = getContentCarro(carro);
        db.delete("Acessorios", "carro_id = ?",new String[]{carro.getId().toString()});
        db.delete("Carros", "id = ?",new String[]{ carro.getId().toString()});
    }

    public List<Carro> list()
    {
        List<Carro> list = new ArrayList<Carro>();

        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM CARROS";
        Cursor c = db.rawQuery(sql,null);
        while (c.moveToNext())
        {
            Carro carro = new Carro();
            carro.setId(c.getLong(c.getColumnIndex("id")));
            carro.setNomeModelo(c.getString(c.getColumnIndex("modelo")));
            carro.setAnoModelo(c.getInt(c.getColumnIndex("ano")));
            carro.setAroRoda(c.getInt(c.getColumnIndex("aro")));
            list.add(carro);
        }
        c.close();
        return list;
    }

    public List<Acessorio> getListAcessorios(Long carroId)
    {
        List<Acessorio> list = new ArrayList<Acessorio>();

        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM Acessorios WHERE carro_id = ?";
        Cursor c = db.rawQuery(sql, new String[] { carroId.toString() });
        while (c.moveToNext())
        {
            Acessorio acessorio = new Acessorio();
            acessorio.setId(c.getLong(c.getColumnIndex("id")));
            acessorio.setNomeAcessorio(c.getString(c.getColumnIndex("nome")));
            acessorio.setOpcional(c.getInt(c.getColumnIndex("opcional")) == 1);
            list.add(acessorio);
        }
        c.close();
        return list;
    }

    private void setAcessorios(Carro carro)
    {
        if(!carro.getAcessorios().isEmpty())
        {
            SQLiteDatabase db = getWritableDatabase();
            for (Acessorio a : carro.getAcessorios()) {
                ContentValues c = new ContentValues();
                c.put("nome",a.getNomeAcessorio());
                c.put("opcional",a.getOpcional());
                c.put("carro_id", carro.getId());
                db.insert("Acessorios",null,c);
            }
        }
    }
}
