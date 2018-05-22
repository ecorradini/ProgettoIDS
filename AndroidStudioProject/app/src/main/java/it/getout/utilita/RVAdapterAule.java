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

import org.w3c.dom.Text;

import java.util.ArrayList;

import it.getout.R;
import it.getout.gestioneposizione.Aula;
import it.getout.gestioneposizione.Piano;

public class RVAdapterAule extends RecyclerView.Adapter<RVAdapterAule.CViewHolder> {

    private int mExpandedPosition = -1;
    private ArrayList<Piano> struttura;
    private Context context;

    public RVAdapterAule(ArrayList<Piano> c, Context context) {
        super();
        struttura = c;
        this.context = context;
    }

    class CViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView piano;
        LinearLayout listaAule;

        CViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv_aule);
            piano = (TextView)itemView.findViewById(R.id.txtnome_piano);
            listaAule = (LinearLayout)itemView.findViewById(R.id.lista_aule);
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
    public void onBindViewHolder(RVAdapterAule.CViewHolder cViewHolder,int position) {

        cViewHolder.piano.setText(struttura.get(position).toString());

        if(struttura.get(position).getAule().size()>0) {
            ArrayList<Aula> aule = struttura.get(position).getAule();
            ArrayList<TextView> textAule = new ArrayList<>();

            for(int i=0; i<aule.size(); i++) {
                TextView aulaT = new TextView(context);
                aulaT.setText(aule.get(i).getNome());
                aulaT.setTextSize(16);
                textAule.add(aulaT);
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
