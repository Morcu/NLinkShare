package com.example.linkshare.adapters;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linkshare.Models.Enlaces;
import com.example.linkshare.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EnlaceAdapter extends RecyclerView.Adapter<EnlaceAdapter.ViewHolder>{

    private int resource;
    private ArrayList<Enlaces> enlacesList;
    private Context eContext;

    public EnlaceAdapter(Context context, ArrayList<Enlaces> enlacesList, int resource){
        this.enlacesList = enlacesList;
        this.resource = resource;
        this.eContext = context;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Enlaces enlace = enlacesList.get(position);
        holder.textViewTitulo.setText(Html.fromHtml("<a href="+ enlace.getUrl()+">"+ enlace.getTitulo() +"</a>"));
        holder.textViewDescripcion.setText(enlace.getDescripcion());

        Log.i("TAG", "____AAA___");
        Log.i("TAG", enlace.getDescripcion().toString() + enlace.getImg().toString() + enlace.getTitulo().toString());
        Picasso.with(eContext)
                .load(enlace.getImg())
                .error(R.mipmap.ic_launcher)
                .into(holder.imgListView);



    }

    @Override
    public int getItemCount() {
        return enlacesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitulo;
        private TextView textViewDescripcion;
        private ImageView imgListView;

        public View view;

        public  ViewHolder(View view) {
            super(view);

            this.view = view;
            this.textViewTitulo = (TextView) view.findViewById(R.id.txtTitulo);
            this.textViewTitulo.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
            this.textViewDescripcion = (TextView) view.findViewById(R.id.txtDescripcion);
            this.imgListView = (ImageView) view.findViewById(R.id.imgListImg);

        }
    }
}
