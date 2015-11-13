package com.HitMode;

import com.uidata.CommonData;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AdapterRecycler  extends  RecyclerView.Adapter<AdapterRecycler.ViewHolder>{

	public int []hitscores;
    public AdapterRecycler(int []hitscore)
    {
    	this.hitscores = hitscore;
    }
    public AdapterRecycler.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
    //	View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.hitresult,viewGroup,false);
    	//ViewHolder viewHolder = new ViewHolder(view);
    	//return viewHolder;  
    	return null;
    }
    @Override
    public void onBindViewHolder(AdapterRecycler.ViewHolder viewHolder, int position) {
        for(int i = 0; i < CommonData.TARGETNUM; i++)
        {
        	viewHolder.tv_hitnum.setText(i);
        	viewHolder.tv_hitscore.setText(0);
        }
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
    	public TextView tv_hitnum;
    	public TextView tv_hitscore;
    	public ViewHolder(final View view) {
            super(view);
          //  tv_hitnum = (TextView)view.findViewById(R.id.hitnum);
          //  tv_hitscore = (TextView)view.findViewById(R.id.hitscore);         
        }
    }
	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return 0;
	}
   
}
