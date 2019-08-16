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

public class UserAdapter extends FirestoreRecyclerAdapter<UserList, UserAdapter.UserHolder> {

    private OnItemClickListener listener;
    public UserAdapter(@NonNull FirestoreRecyclerOptions<UserList> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserAdapter.UserHolder holder, int position, @NonNull UserList model) {
        String name = model.getFname() +' '+ model.getLname();
        String isPaid = model.getIsPaid().toString().trim();

        if(isPaid.equals("true"))
            holder.isPaid_tv.setText("✔");
        else
            holder.isPaid_tv.setText("❌");

        holder.name_tv.setText(name);
        holder.imei_tv.setText(model.getImei());
        holder.mobile_tv.setText(String.valueOf(model.getMob()));
        holder.last_login_tv.setText(String.valueOf(model.getLast_login()));
        holder.created_time_tv.setText(String.valueOf(model.getCreated_time()));
        holder.email_tv.setText(String.valueOf(model.getEmail()));
    }

    @NonNull
    @Override
    public UserAdapter.UserHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_item,viewGroup,false);
        return new UserAdapter.UserHolder(v);
    }

    class UserHolder extends RecyclerView.ViewHolder {

        TextView name_tv;
        TextView imei_tv;
        TextView mobile_tv;
        TextView last_login_tv;
        TextView isPaid_tv;
        TextView created_time_tv;
        TextView email_tv;



        public UserHolder(@NonNull View itemView) {
            super(itemView);

            name_tv=itemView.findViewById(R.id.userList_name);
            imei_tv=itemView.findViewById(R.id.userList_imei);
            mobile_tv=itemView.findViewById(R.id.userList_mobile);
            last_login_tv=itemView.findViewById(R.id.userList_lastLogin);
            isPaid_tv=itemView.findViewById(R.id.userList_isPaid);
            created_time_tv=itemView.findViewById(R.id.userList_createdOn);
            email_tv=itemView.findViewById(R.id.userList_email);

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

    public void setOnClickListener(UserAdapter.OnItemClickListener listener){
        this.listener = listener;
    }
}
