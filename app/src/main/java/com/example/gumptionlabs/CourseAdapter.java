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

public class CourseAdapter extends FirestoreRecyclerAdapter<Course, CourseAdapter.CourseHolder> {

    private OnItemClickListener listener;
    public CourseAdapter(@NonNull FirestoreRecyclerOptions<Course> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CourseHolder holder, int position, @NonNull Course model) {
        holder.name_tv.setText(model.getName());
        holder.desc_tv.setText(model.getDescription());
        holder.count_tv.setText(String.valueOf(model.getVideo_count()));
        holder.amt_tv.setText(String.valueOf(model.getAmount()));
    }

    @NonNull
    @Override
    public CourseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.add_course_item,viewGroup,false);
        return new CourseHolder(v);
    }

    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class CourseHolder extends RecyclerView.ViewHolder {

        TextView name_tv;
        TextView desc_tv;
        TextView count_tv;
        TextView amt_tv;

        public CourseHolder(@NonNull View itemView) {
            super(itemView);

            name_tv=itemView.findViewById(R.id.course_name);
            desc_tv=itemView.findViewById(R.id.course_desc);
            count_tv=itemView.findViewById(R.id.course_video_count);
            amt_tv=itemView.findViewById(R.id.course_amount);

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
