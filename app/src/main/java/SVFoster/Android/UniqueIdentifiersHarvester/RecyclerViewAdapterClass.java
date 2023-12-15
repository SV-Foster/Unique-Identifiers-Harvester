/***

Copyright 2023, SV Foster. All rights reserved.

License:
    This program is free for personal, educational and/or non-profit usage    

Revision History:

***/

package SVFoster.Android.UniqueIdentifiersHarvester;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapterClass extends RecyclerView.Adapter<RecyclerViewAdapterClass.ViewHolderClass> {
    private int NumberOfItemsFixed = 0;
    private List<UniqueIDHarvesterDataConvertorClass.DataHarvestedToArrayClass> DataShowArray = null;

    public RecyclerViewAdapterClass(List<UniqueIDHarvesterDataConvertorClass.DataHarvestedToArrayClass> arr){
        this.NumberOfItemsFixed = arr.size();
        this.DataShowArray = arr;
    }

    @NonNull
    @Override
    public ViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context cnt = parent.getContext();
        LayoutInflater linf = LayoutInflater.from(cnt);
        int recyclerview_item = R.layout.recyclerview_item;

        View v = linf.inflate(recyclerview_item, parent, false);
        ViewHolderClass vhc = new ViewHolderClass(v);

        return vhc;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderClass holder, int position) {
        holder.Line1.setText( this.DataShowArray.get(position).Header );
        holder.Line2.setText( this.DataShowArray.get(position).Value );
    }

    @Override
    public int getItemCount() {
        return NumberOfItemsFixed;
    }

    static class ViewHolderClass extends RecyclerView.ViewHolder {
        private TextView Line1 = null;
        private TextView Line2 = null;

        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);

            this.Line1 = itemView.findViewById(R.id.textView1);
            this.Line2 = itemView.findViewById(R.id.textView2);
        }
    }
}
