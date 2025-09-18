package com.example.alodrawermenu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
 * Use the {@link MusicasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MusicasFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView lvMusicas;
    private MainActivity mainActivity;
    static public int flag = 0;
    public MusicasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MusicasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MusicasFragment newInstance(String param1, String param2) {
        MusicasFragment fragment = new MusicasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
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
        View view = inflater.inflate(R.layout.fragment_musicas, container, false);
        lvMusicas = view.findViewById(R.id.lvMusicas);
        lvMusicas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog alerta;
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Confirm");
                builder.setMessage("Are You Sure?");
                //define um botão como positivo
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        MusicaDAL musicaDAL = new MusicaDAL(view.getContext());
                        Musica musica;
                        musica = (Musica) parent.getItemAtPosition(position);
                        musicaDAL.apagar(musica.getId());
                        carregarMusicas(view);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
                alerta = builder.create();
                alerta.show();

                return false;
            }

        });
        lvMusicas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Musica musica = (Musica) parent.getItemAtPosition(position);
                mainActivity.cadastrarMusicas(musica);
            }
        });
        carregarMusicas(view);
        return view;
    }

    private void carregarMusicas(View view)
    {
        MusicaDAL musicaDAL = new MusicaDAL(view.getContext());
        List<Musica> musicaList = musicaDAL.get("");
        lvMusicas.setAdapter(new ArrayAdapter<Musica>(view.getContext(), android.R.layout.simple_list_item_1, musicaList));

    }
}