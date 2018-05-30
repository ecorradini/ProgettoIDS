package it.getout.utilita;

import android.content.Context;
import android.support.transition.AutoTransition;
import android.support.transition.TransitionManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import it.getout.Client;
import it.getout.R;
import it.getout.gestioneposizione.Aula;
import it.getout.gestioneposizione.Piano;
import it.getout.gestioneposizione.Tronco;

public class RVAdapterAule extends RecyclerView.Adapter<RVAdapterAule.CViewHolder> {

    private int mExpandedPosition = -1;
    private ArrayList<Piano> struttura;
    private Context context;


    public RVAdapterAule(ArrayList<Piano> c, Context context) {
        super();
        struttura = c;
        struttura.remove(0);
        this.context = context;

    }

    class CViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView piano;
        LinearLayout listaAule;
        ArrayList<TextView> textAule;

        CViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv_aule);
            piano = (TextView)itemView.findViewById(R.id.txtnome_piano);
            listaAule = (LinearLayout)itemView.findViewById(R.id.lista_aule);

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

    @Override
    public void onBindViewHolder(final RVAdapterAule.CViewHolder cViewHolder, final int position) {

        cViewHolder.piano.setText(struttura.get(position).toString());

        if(struttura.get(position).getAule()!= null && struttura.get(position).getAule().size()>0) {

            ArrayList<Aula> aule = new ArrayList<>(struttura.get(position).getAule());

            for(int i=0; i<aule.size(); i++) {
                final int index = i;
                TextView aulaT = new TextView(context);
                aulaT.setText(aule.get(i).getNome());
                aulaT.setTextSize(16);
                aulaT.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                aulaT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ArrayList<Tronco> percorso = ((Client)view.getContext()).getGestore().scaricaPercorso(struttura.get(position).getAula(index).getNome());
                        new Thread() {
                            public void run() {
                                ((Client)context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                    }
                                });
                            }
                        }.start();
                    }
                });
                cViewHolder.textAule.add(aulaT);
                cViewHolder.listaAule.addView(aulaT);
            }

            final boolean isExpanded = position == mExpandedPosition;
            cViewHolder.listaAule.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
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
