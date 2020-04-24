package com.uccendigital.secure.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uccendigital.secure.R;
import com.uccendigital.secure.app.Functions;
import com.uccendigital.secure.elements.Sim;

import java.util.ArrayList;

import static android.widget.Adapter.IGNORE_ITEM_VIEW_TYPE;

public class MyRVAdapter extends RecyclerView.Adapter<MyRVAdapter.ViewHolder> {

    Context context;
    int Ressource;
    private ArrayList<Object> list;

    String[] options;

    private static final int NORMAL_ITEM = 0;
    private static final int HEADER_ITEM = 1;

    public MyRVAdapter (Context context, int Ressoucre, ArrayList<Object> list, String [] options) {
        this.context = context;
        this.Ressource = Ressoucre;
        this.list = list;
        this.options = options;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(Ressource, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

            int rowType = getItemViewType(position);

            switch (rowType) {
                case NORMAL_ITEM:

                    final Sim sim = (Sim) getItem(position);

                    holder.listname.setText(sim.getName());
                    holder.listserial.setText(sim.getSerial());


                    if (options[0].equals("delete")) {
                        holder.listoptions.setImageResource(R.drawable.ic_delete);
                    } else if (options[0].equals("add")) {
                        holder.listoptions.setImageResource(R.drawable.ic_add);
                    }

                    holder.listoptions.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            if (options[0].equals("delete")) {

                                new Functions(context).removeWhiteList((getItemViewType(0) == HEADER_ITEM) ? position - 1 : position);
                                list.remove(position);

                                if (getItemCount() == 1 && getItemViewType(0) == HEADER_ITEM) {
                                    list.remove(0);
                                }

                            } else if (options[0].equals("add")) {

                                new Functions(context).addWhiteList(sim);
                                list.remove(position);

                                if (getItemCount() == 1 && getItemViewType(0) == HEADER_ITEM) {
                                    list.remove(0);
                                }

                                Toast.makeText(context, context.getResources().getString(R.string.addedwhitelist), Toast.LENGTH_SHORT).show();

                            }

                            notifyDataSetChanged();

                        }
                    });

                    break;

                case HEADER_ITEM:

                    Header header = (Header) getItem(position);
                    holder.listname.setText(header.itemText);
                    holder.listname.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    holder.listname.setBackgroundColor(context.getResources().getColor(R.color.colorRed));
                    holder.listserial.setVisibility(View.GONE);
                    holder.listoptions.setVisibility(View.GONE);

                    break;

            }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        Object mObject = getItem(position);
        if (mObject instanceof Sim){
            return NORMAL_ITEM;
        } else if (mObject instanceof Header) {
            return HEADER_ITEM;
        } else {
            return IGNORE_ITEM_VIEW_TYPE;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView listname, listserial;
        public ImageView listoptions;
        public ViewHolder(View view) {
            super(view);
            listname = view.findViewById(R.id.listname);
            listserial = view.findViewById(R.id.listserial);
            listoptions = view.findViewById(R.id.listoptions);

        }
    }
}
