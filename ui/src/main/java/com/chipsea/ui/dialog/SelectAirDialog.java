package com.chipsea.ui.dialog;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chipsea.code.listener.SelectAirRecyclerviewCallback;
import com.chipsea.mode.entity.AirModel;
import com.chipsea.ui.R;
import com.chipsea.ui.adapter.SelectAirRecyclerViewAdapter;
import com.chipsea.view.dialog.BaseDialog;
import com.chipsea.view.text.CustomTextView;
import com.chipsea.view.utils.RecyclerItemDecoration;

import java.util.List;


public class SelectAirDialog extends BaseDialog implements View.OnClickListener {
	private Context _context ;
	RecyclerView recyclerView ;
	private CustomTextView resetText ;
	private SelectAirRecyclerViewAdapter adapter;


	public SelectAirDialog(Context context, List<AirModel> models, SelectAirRecyclerviewCallback<AirModel> callback) {
		super(context);
		_context=context;
		View vv = LayoutInflater.from(context).inflate(
				R.layout.dialog_select_air, null);
		addView(vv);
		dialog.setCancelable(false);
		resetText = (CustomTextView) vv.findViewById(R.id.resetText);
		resetText.setOnClickListener(this);
		recyclerView = (RecyclerView) vv.findViewById(R.id.recyclerView);
		adapter = new SelectAirRecyclerViewAdapter(_context,callback) ;
		adapter.setDatas(models) ;
		recyclerView.setAdapter(adapter);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		recyclerView.setLayoutManager(new LinearLayoutManager(_context, LinearLayoutManager.VERTICAL, false));
		recyclerView.addItemDecoration(new RecyclerItemDecoration(_context,LinearLayoutManager.VERTICAL,R.drawable.air_match_divider_bg));
	}


	@Override
	public void onClick(View v) {
		if(l != null){
			l.onClick(v);
		}
		dismiss();
	}
}
