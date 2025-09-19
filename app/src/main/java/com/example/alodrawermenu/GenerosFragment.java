package com.example.alodrawermenu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.alodrawermenu.db.dal.GeneroDAL;
import com.example.alodrawermenu.db.dal.MusicaDAL;
import com.example.alodrawermenu.db.entidades.Genero;
import com.example.alodrawermenu.db.entidades.Musica;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GenerosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GenerosFragment extends Fragment {

    private ListView lvGeneros;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MainActivity mainActivity;
    public GenerosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GenerosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GenerosFragment newInstance(String param1, String param2) {
        GenerosFragment fragment = new GenerosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) { // nos permite alternar de um fragmento para a main activity
        super.onAttach(context);
        mainActivity = (MainActivity) context;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_generos, container, false);
        lvGeneros = view.findViewById(R.id.lvGeneros);
        lvGeneros.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog alerta;
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Confirm");
                builder.setMessage("Are You Sure?");
                //define um botão como positivo
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        GeneroDAL dal = new GeneroDAL(view.getContext());
                        Genero genero = (Genero) adapterView.getItemAtPosition(i); // pega o genero selecionado
                        int generoId;
                        generoId = genero.getId();
                        MusicaDAL musicaDAL = new MusicaDAL(view.getContext());
                        List<Musica> musicaList = musicaDAL.get("");
                        int i = 0;
                        while(i < musicaList.size())
                        {
                            Musica musica = musicaList.get(i);
                            if (musica.getGenero().getId() == generoId)
                            {
                                musicaDAL.apagar(musica.getId());
                                musicaList.remove(musica);

                            }
                            i++;
                        }
                        dal.apagar(genero.getId());

                        // atualiza o listview em tempo real
                        carregarGeneros(view);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
                alerta = builder.create();
                alerta.show();


                return true;
            }
        });

        carregarGeneros(view);
        return view;
    }

    private void carregarGeneros(View view)
    {
        GeneroDAL dal = new GeneroDAL(view.getContext());
        List<Genero> generoList = dal.get(""); // filtro vazio, retorna *
        lvGeneros.setAdapter(new ArrayAdapter<Genero>(view.getContext(), android.R.layout.simple_list_item_1, generoList));

    }
}