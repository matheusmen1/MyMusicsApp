package com.example.alodrawermenu;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.alodrawermenu.db.dal.GeneroDAL;
import com.example.alodrawermenu.db.dal.MusicaDAL;
import com.example.alodrawermenu.db.entidades.Genero;
import com.example.alodrawermenu.db.entidades.Musica;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Generated;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NovaMusicaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NovaMusicaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String titulo, interprete, itemSelecionado;
    private int ano, generoId;
    private double duracao;
    private EditText etTitulo, etAno, etDuracao, etInterprete;
    private Button btConfirmar;
    MainActivity mainActivity;
    Spinner spinner;
    List<Genero> generoList = new ArrayList<>();
    List<String> generoNome = new ArrayList<>();

    public NovaMusicaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NovaMusicaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NovaMusicaFragment newInstance(String param1, String param2) {
        NovaMusicaFragment fragment = new NovaMusicaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public void onAttach(@NonNull Context context) { // nos permite alternar de um fragmento para a main activity
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nova_musica, container, false);
        etTitulo = view.findViewById(R.id.etTitulo);
        etAno = view.findViewById(R.id.etAno);
        etDuracao = view.findViewById(R.id.etDuracao);
        etInterprete = view.findViewById(R.id.etInterprete);
        btConfirmar = view.findViewById(R.id.btConfirmar);
        spinner = view.findViewById(R.id.spinner);
        carregarGeneros();
        //Toast.makeText(view.getContext(),""+MainActivity.musicaId,Toast.LENGTH_LONG).show();
        if (MainActivity.musicaId != -1) // modo edicao
        {
            preecherCampos();
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemSelecionado = parent.getItemAtPosition(position).toString();
                //Toast.makeText(getContext(),itemSelecionado,Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (!etAno.getText().toString().isEmpty() && !etTitulo.getText().toString().isEmpty() && !etDuracao.getText().toString().isEmpty() && !etInterprete.getText().toString().isEmpty())
                {
                    ano = Integer.parseInt(etAno.getText().toString());
                    titulo = etTitulo.getText().toString();
                    duracao = Double.parseDouble(etDuracao.getText().toString());
                    interprete = etInterprete.getText().toString();
                    GeneroDAL generoDAL = new GeneroDAL(view.getContext());
                    generoList.clear();
                    generoList = generoDAL.get("gen_nome = '"+itemSelecionado+"'");
                    Musica musica = new Musica(ano, titulo, interprete, generoList.get(0),duracao);
                    MusicaDAL musicaDAL = new MusicaDAL(view.getContext());
                    if (MainActivity.musicaId == -1) // nova musica
                    {
                        musicaDAL.salvar(musica);
                    }
                    else
                    {
                        musica.setId(MainActivity.musicaId);
                        MainActivity.musicaId = -1; // saiu do modo edicao
                        musicaDAL.alterar(musica);
                    }

                    Toast.makeText(getContext(),"Enviado com Sucesso",Toast.LENGTH_LONG).show();
                    clear();
                }
                else
                {
                    Toast.makeText(getContext(),"Erro ao Enviar",Toast.LENGTH_LONG).show();
                }

            }
        });
        return view;
    }
    private void carregarGeneros()
    {
        GeneroDAL generoDAL= new GeneroDAL(getContext());
        generoList = generoDAL.get("");
        int i = 0;
        while (i < generoList.size())
        {
            generoNome.add(generoList.get(i).getNome());
            i++;
        }
        ArrayAdapter<String> generoArrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, generoNome);
        spinner.setAdapter(generoArrayAdapter);
    }
    private void clear()
    {
        etAno.setText("");
        etTitulo.setText("");
        etDuracao.setText("");
        etInterprete.setText("");
        spinner.setSelection(0);
    }
    private void preecherCampos()
    {
        MusicaDAL musicaDAL = new MusicaDAL(getContext());
        Musica musica = musicaDAL.get(MainActivity.musicaId);
        if (musica != null)
        {

            etTitulo.setText(musica.getTitulo());
            etAno.setText(""+musica.getAno());
            etDuracao.setText(""+musica.getDuracao());
            etInterprete.setText(musica.getInterprete());
            int i = 0, pos = -1, flag = 0;
            String generoNome;
            while(i < spinner.getCount() && flag != 1)
            {
                generoNome = spinner.getItemAtPosition(i).toString();
                if (generoNome.equals(musica.getGenero().getNome()))
                {
                    flag = 1;
                    pos = i;
                }
                i++;
            }
            spinner.setSelection(pos);
        }

    }
}