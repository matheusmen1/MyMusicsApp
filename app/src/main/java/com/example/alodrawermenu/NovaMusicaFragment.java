package com.example.alodrawermenu;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.alodrawermenu.db.dal.GeneroDAL;
import com.example.alodrawermenu.db.entidades.Genero;
import com.example.alodrawermenu.db.entidades.Musica;

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
    private String titulo, interprete;
    private int duracao, genero, ano;
    private EditText etTitulo, etAno, etDuracao, etInterprete;
    private Button btConfirmar;
    MainActivity mainActivity;

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
        btConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ano = Integer.parseInt(etAno.getText().toString());
                titulo = etTitulo.getText().toString();
                duracao = Integer.parseInt(etDuracao.toString());
                interprete = etInterprete.getText().toString();
                GeneroDAL generoDAL = new GeneroDAL(view.getContext());
                Genero g;
                g = generoDAL.get(1);

                Musica musica = new Musica(ano, titulo, interprete, g,duracao);
                mainActivity.cadastrarMusicas(musica);
            }
        });
        return view;
    }
}