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

public class AddVideoAdapter extends FirestoreRecyclerAdapter<MyVideo, AddVideoAdapter.AddVideoHolder> {

    private OnItemClickListener listener;
    public AddVideoAdapter(@NonNull FirestoreRecyclerOptions<MyVideo> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AddVideoHolder holder, int position, @NonNull MyVideo model) {
        holder.name_tv.setText(model.getVideo_Name());
        holder.timestamp_tv.setText(model.getTimestamp().toString());
        holder.length_tv.setText(String.valueOf(model.getLength()));
        holder.password_tv.setText(String.valueOf(model.getPassword()));
        holder.url_tv.setText(String.valueOf(model.getVideo_URL()));
    }

    @NonNull
    @Override
    public AddVideoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.add_video_item,viewGroup,false);
        return new AddVideoHolder(v);
    }

    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class AddVideoHolder extends RecyclerView.ViewHolder {

        TextView name_tv;
        TextView timestamp_tv;
        TextView length_tv;
        TextView password_tv;
        TextView url_tv;


        public AddVideoHolder(@NonNull View itemView) {
            super(itemView);

            name_tv=itemView.findViewById(R.id.addVideo_name);
            timestamp_tv=itemView.findViewById(R.id.addVideo_timestamp);
            length_tv=itemView.findViewById(R.id.addVideo_length);
            password_tv=itemView.findViewById(R.id.addVideo_pwd);
            url_tv=itemView.findViewById(R.id.addVideo_url);

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
