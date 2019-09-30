package com.example.gumptionlabs;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
        //Boolean isDis = model.getIsDisabled();
        /*String isDisabled;
        if(isDis!=null)
        {
            isDisabled = isDis.toString().trim();
        }
        else {
            isDisabled="false";
        }*/

        if(isPaid.equals("true"))
            holder.isPaid_tv.setText("✔");
        else {
            holder.isPaid_tv.setText("❌");
            holder.viewPurchasedTv.setVisibility(View.GONE);
        }

       /* if(isDisabled.equals("true"))
            holder.disableTv.setVisibility(View.GONE);
        else {
            holder.enableTv.setVisibility(View.GONE);
        }*/

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
        TextView assignTv;
        TextView viewPurchasedTv;
        /*TextView disableTv;
        TextView enableTv;
        TextView deleteTv;*/


        public UserHolder(@NonNull View itemView) {
            super(itemView);

            name_tv=itemView.findViewById(R.id.userList_name);
            imei_tv=itemView.findViewById(R.id.userList_imei);
            mobile_tv=itemView.findViewById(R.id.userList_mobile);
            last_login_tv=itemView.findViewById(R.id.userList_lastLogin);
            isPaid_tv=itemView.findViewById(R.id.userList_isPaid);
            created_time_tv=itemView.findViewById(R.id.userList_createdOn);
            email_tv=itemView.findViewById(R.id.userList_email);
            assignTv=itemView.findViewById(R.id.userList_assign);
            viewPurchasedTv=itemView.findViewById(R.id.userList_viewPurchased);
            /*disableTv=itemView.findViewById(R.id.userList_disable);
            deleteTv=itemView.findViewById(R.id.userList_delete);
            enableTv=itemView.findViewById(R.id.userList_enable);*/

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });

            assignTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null){
                        listener.onAssignClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });

            viewPurchasedTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null){
                        listener.onViewPurchasedClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });

           /* disableTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null){
                        listener.onDisableClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });

            enableTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null){
                        listener.onEnableClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });

            deleteTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null){
                        listener.onDeleteClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });*/


            assignTv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(v.getContext(), "Assign a course to a user", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

            viewPurchasedTv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(v.getContext(), "View purchase history of a user", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

            /*disableTv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(v.getContext(), "Lock user account", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

            deleteTv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(v.getContext(), "Delete user", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

            enableTv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(v.getContext(), "Unlock user account", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });*/

        }
    }

    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
        void onAssignClick(DocumentSnapshot documentSnapshot, int position);
        void onViewPurchasedClick(DocumentSnapshot documentSnapshot, int position);
        /*void onDisableClick(DocumentSnapshot documentSnapshot, int position);
        void onEnableClick(DocumentSnapshot documentSnapshot, int position);
        void onDeleteClick(DocumentSnapshot documentSnapshot, int position);*/
    }

    public void setOnClickListener(UserAdapter.OnItemClickListener listener){
        this.listener = listener;
    }
}
