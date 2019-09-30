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

public class MyCourseAdapter extends FirestoreRecyclerAdapter<MyCourse, MyCourseAdapter.MyCourseHolder> {

    private OnItemClickListener listener;
    public MyCourseAdapter(@NonNull FirestoreRecyclerOptions<MyCourse> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyCourseHolder holder, int position, @NonNull MyCourse model) {
        holder.name_tv.setText(model.getName());
        holder.timestamp_tv.setText(model.getPurchase_timestamp());
        holder.mode_tv.setText(String.valueOf(model.getPayment_mode()));
        holder.amt_tv.setText(String.valueOf(model.getAmount()));
    }

    @NonNull
    @Override
    public MyCourseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_course_item,viewGroup,false);
        return new MyCourseHolder(v);
    }

    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class MyCourseHolder extends RecyclerView.ViewHolder {

        TextView name_tv;
        TextView timestamp_tv;
        TextView mode_tv;
        TextView amt_tv;

        public MyCourseHolder(@NonNull View itemView) {
            super(itemView);

            name_tv=itemView.findViewById(R.id.MyCourse_name);
            timestamp_tv=itemView.findViewById(R.id.myCourse_timestamp);
            mode_tv=itemView.findViewById(R.id.MyCourse_mode);
            amt_tv=itemView.findViewById(R.id.myCourse_amount);
            amt_tv.setVisibility(View.GONE);
            mode_tv.setVisibility(View.GONE);
            timestamp_tv.setVisibility(View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });

        }
    }

    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
