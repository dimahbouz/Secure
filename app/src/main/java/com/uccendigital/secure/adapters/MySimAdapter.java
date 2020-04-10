package com.uccendigital.secure.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.uccendigital.secure.R;
import com.uccendigital.secure.app.Functions;
import com.uccendigital.secure.elements.Sim;

import java.util.ArrayList;

public class MySimAdapter extends ArrayAdapter<Object> {
    Context context;
    private ArrayList<Object> list;
    int ressource;
    private LayoutInflater inflater;
    String [] options;

    private static final int NORMAL_ITEM = 0;
    private static final int HEADER_ITEM = 1;

    public MySimAdapter(@NonNull Context context, int ressource, ArrayList<Object> list, String [] options) {
        super(context, ressource, list);
        this.context = context;
        this.list = list;
        this.ressource = ressource;
        this.options = options;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
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

    @NonNull
    @Override
    public View getView(final int position, @Nullable View view, @NonNull ViewGroup parent) {

        view = inflater.inflate(ressource, parent, false);

        TextView listname = view.findViewById(R.id.listname);
        TextView listserial = view.findViewById(R.id.listserial);
        ImageView listoptions = view.findViewById(R.id.listoptions);


            int rowType = getItemViewType(position);

            switch (rowType) {
                case NORMAL_ITEM:

                    final Sim sim = (Sim) getItem(position);

                    listname.setText(sim.getName());
                    listserial.setText(sim.getSerial());


                    if (options[0].equals("delete")) {
                        listoptions.setImageResource(R.drawable.close);
                    } else if (options[0].equals("add")) {
                        listoptions.setImageResource(R.drawable.add);
                    }

                    listoptions.setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("WrongConstant")
                        @Override
                        public void onClick(View v) {

                            if (options[0].equals("delete")) {

                                new Functions(context).removeWhiteList(position);
                                list.remove(position);

                            } else if (options[0].equals("add")) {

                                new Functions(context).addWhiteList(sim);
                                list.remove(position);
                                Toast.makeText(context, context.getResources().getString(R.string.addedwhitelist), Toast.LENGTH_SHORT).show();

                            }

                            notifyDataSetChanged();

                        }
                    });

                    break;

                case HEADER_ITEM:

                    Header header = (Header) getItem(position);
                    listname.setText(header.itemText);
                    listname.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    listname.setBackgroundColor(context.getResources().getColor(R.color.colorRed));
                    listserial.setVisibility(View.GONE);
                    listoptions.setVisibility(View.GONE);

                    break;

                default:
                    return view;

            }

        return view;
    }
}
