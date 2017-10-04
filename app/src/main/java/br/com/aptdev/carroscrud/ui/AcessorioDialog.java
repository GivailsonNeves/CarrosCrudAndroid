package br.com.aptdev.carroscrud.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import br.com.aptdev.carroscrud.FormCarroActivity;
import br.com.aptdev.carroscrud.R;
import br.com.aptdev.carroscrud.model.Acessorio;

/**
 * Created by givailson on 28/06/17.
 */

public class AcessorioDialog extends DialogFragment {

    private View view;
    private Boolean editMode = false;
    private Acessorio acessorio;
    EditText tfNome;
    Switch sObrigatorio;

    public AcessorioDialog(){
        super();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        view = getActivity().getLayoutInflater().inflate(R.layout.acessorio_dialog, null);
        tfNome = (EditText) view.findViewById(R.id.acessorio_nome);
        sObrigatorio = (Switch) view.findViewById(R.id.acessorio_obrigatorio);

        prepareAlert();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.acessorios_titulo);
        builder.setView(view);
        builder.setPositiveButton(R.string.btn_salvar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (isValidForm()) {
                    if (editMode) {
                        ((FormCarroActivity) getActivity()).updateAcessorio(getAcessorio());
                    } else {
                        ((FormCarroActivity) getActivity()).addAcessorio(getAcessorio());
                    }
                } else {
                    int _message = (editMode) ? R.string.campos_invalidos_acessorio_update : R.string.campos_invalidos_acessorio_add;
                    ((FormCarroActivity) getActivity()).feedBack(_message);
                }
            }
        });
        builder.setNegativeButton(R.string.btn_cancelar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ((FormCarroActivity) getActivity()).closeDialog();
            }
        });
        return builder.create();
    }

    private Acessorio getAcessorio()
    {
        acessorio.setNomeAcessorio(tfNome.getText().toString());
        acessorio.setOpcional(sObrigatorio.isChecked());

        return acessorio;
    }

    private void prepareAlert()
    {
        if(acessorio != null && acessorio.getId() != null){
            tfNome.setText(acessorio.getNomeAcessorio());
            sObrigatorio.setChecked(acessorio.getOpcional());
        }else{
            tfNome.setText("");
            sObrigatorio.setChecked(false);
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
        this.acessorio = new Acessorio();
        editMode = false;
    }


    public void show(FragmentManager manager, String tag, Acessorio acessorio) {
        super.show(manager, tag);
        this.acessorio = acessorio;
        editMode = true;
    }

    private boolean isValidForm()
    {
        return !tfNome.getText().toString().equals("");
    }


}
