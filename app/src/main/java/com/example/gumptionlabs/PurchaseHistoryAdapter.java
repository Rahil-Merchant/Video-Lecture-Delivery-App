package com.example.gumptionlabs;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class PurchaseHistoryAdapter extends FirestoreRecyclerAdapter<MyCourse, PurchaseHistoryAdapter.PurchaseHistoryHolder> {

    public PurchaseHistoryAdapter(@NonNull FirestoreRecyclerOptions<MyCourse> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PurchaseHistoryHolder holder, int position, @NonNull MyCourse model) {
        holder.name_tv.setText(model.getName());
        holder.timestamp_tv.setText(model.getPurchase_timestamp());
        holder.mode_tv.setText(String.valueOf(model.getPayment_mode()));
        holder.amt_tv.setText(String.valueOf(model.getAmount()));
    }

    @NonNull
    @Override
    public PurchaseHistoryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_course_item,viewGroup,false);
        return new PurchaseHistoryHolder(v);
    }


    class PurchaseHistoryHolder extends RecyclerView.ViewHolder {

        TextView name_tv;
        TextView timestamp_tv;
        TextView mode_tv;
        TextView amt_tv;

        public PurchaseHistoryHolder(@NonNull View itemView) {
            super(itemView);

            name_tv=itemView.findViewById(R.id.MyCourse_name);
            timestamp_tv=itemView.findViewById(R.id.myCourse_timestamp);
            mode_tv=itemView.findViewById(R.id.MyCourse_mode);
            amt_tv=itemView.findViewById(R.id.myCourse_amount);

        }
    }

}
