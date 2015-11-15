package com.HitMode;

import com.infraredgun.R;
import com.uidata.CommonData;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AdapterRecycler  extends  RecyclerView.Adapter<AdapterRecycler.ViewHolder>{

	public String []hitscores;
    public AdapterRecycler(String []hitscore)
    {
    	this.hitscores = hitscore;
    }
    public AdapterRecycler.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.hitresult,viewGroup,false);
    	ViewHolder viewHolder = new ViewHolder(view);
    	return viewHolder;
    }
    @Override
    public void onBindViewHolder(AdapterRecycler.ViewHolder viewHolder, int position) {
            int num = position + 1;
        	viewHolder.tv_hitnum.setText(""+num);
        	viewHolder.tv_hitscore.setText(hitscores[position]);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
    	public TextView tv_hitnum;
    	public TextView tv_hitscore;
    	public ViewHolder(final View view) {
            super(view);
            tv_hitnum = (TextView)view.findViewById(R.id.hitnum);
            tv_hitscore = (TextView)view.findViewById(R.id.hitscore);
        }
    }
	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return CommonData.TARGETNUM;
	}
   
}
