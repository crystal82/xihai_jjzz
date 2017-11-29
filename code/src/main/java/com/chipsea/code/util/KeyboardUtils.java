package com.chipsea.code.util;


import android.content.Context;
import android.os.Handler;
import android.text.Selection;
import android.text.Spannable;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class KeyboardUtils {
	public static void setCursorSeat(CharSequence content ){
		if (content instanceof Spannable) {
			Spannable spanText = (Spannable) content;
			Selection.setSelection(spanText, spanText.length());
		}
	}
	public static void showSoftInput(Context context, EditText editText){
		InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
        	editText.requestFocus();
            inputMethodManager.showSoftInput(editText, 0);
        }
	}
	public static void showDelayedSoftInput(final Context context, final EditText editText){
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		        if (inputMethodManager != null) {
		        	editText.requestFocus();
		            inputMethodManager.showSoftInput(editText, 0);
		        }
			}
		}, 300) ;
	}
	public static void dissJianPan(Context context, EditText editText){
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}
}
