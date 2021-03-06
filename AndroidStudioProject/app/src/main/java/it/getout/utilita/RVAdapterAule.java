package it.getout.utilita;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.AutoTransition;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import it.getout.Client;
import it.getout.R;
import it.getout.fragments.FragmentListaAule;
import it.getout.fragments.FragmentOrdinaria;
import it.getout.gestioneposizione.Aula;
import it.getout.gestioneposizione.Piano;
import it.getout.gestioneposizione.Tronco;

import static android.view.View.VISIBLE;

public class RVAdapterAule extends RecyclerView.Adapter<RVAdapterAule.CViewHolder> {

    private int mExpandedPosition = -1;
    private ArrayList<Piano> struttura;
    private Context context;
    FragmentOrdinaria parent;
    //private View v;


    public RVAdapterAule(ArrayList<Piano> c, Context context, FragmentOrdinaria parent) {
        super();
        struttura = c;
        struttura.remove(0);
        this.context = context;
        this.parent=parent;

    }

    class CViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView piano;
        LinearLayout listaAule;
        ArrayList<TextView> textAule;
        FloatingActionButton fab;

        CViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv_aule);
            piano = itemView.findViewById(R.id.txtnome_piano);
            listaAule = itemView.findViewById(R.id.lista_aule);

            textAule = new ArrayList<>();
        }
    }

    @Override
    public int getItemCount() {
        return struttura.size();
    }

    @Override
    public CViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lista_aule_row,viewGroup,false);
        return new CViewHolder(v);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(final RVAdapterAule.CViewHolder cViewHolder, final int position) {

        cViewHolder.piano.setText(struttura.get(position).toString());

        if(struttura.get(position).getAule()!= null && struttura.get(position).getAule().size()>0) {

            ArrayList<Aula> aule = new ArrayList<>(struttura.get(position).getAule());

            for(int i=0; i<aule.size(); i++) {
                final int index = i;
                TextView aulaT = new TextView(context);

                aulaT.setBackground(context.getResources().getDrawable(R.drawable.aula_list_background));

                aulaT.setText(aule.get(i).getNome());
                aulaT.setTextSize(16);
                aulaT.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                aulaT.setGravity(Gravity.CENTER_VERTICAL);
                aulaT.setTextColor(cViewHolder.itemView.getContext().getResources().getColor(R.color.white));

                LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                llp.setMargins(50, 10, 0, 0); // llp.setMargins(left, top, right, bottom);
                aulaT.setLayoutParams(llp);

                aulaT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        parent.setAula(struttura.get(position).getAula(index).getNome());
                        parent.setButton(true);
                        ((Client)context).getSupportFragmentManager().popBackStackImmediate();

                    }
                });
                cViewHolder.textAule.add(aulaT);
                cViewHolder.listaAule.addView(aulaT);
            }

            final boolean isExpanded = position == mExpandedPosition;
            cViewHolder.listaAule.setVisibility(isExpanded ? VISIBLE : View.GONE);
            cViewHolder.itemView.setActivated(isExpanded);
            final int pos = position;
            final RVAdapterAule.CViewHolder holder = cViewHolder;
            cViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mExpandedPosition = isExpanded ? -1 : pos;
                    cViewHolder.listaAule.removeAllViewsInLayout();
                    TransitionManager.beginDelayedTransition((ViewGroup) holder.itemView, new AutoTransition());
                    notifyItemChanged(pos);
                }
            });
        }
    }




    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
