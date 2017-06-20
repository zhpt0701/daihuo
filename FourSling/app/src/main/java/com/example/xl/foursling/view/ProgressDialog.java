package com.example.xl.foursling.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.example.xl.foursling.R;


public class ProgressDialog extends Dialog {
	private Context context = null;
	private static ProgressDialog ProgressDialog = null;

	public ProgressDialog(Context context) {
		super(context);
		this.context = context;
	}

	public ProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	public static ProgressDialog createDialog(Context context) {
		ProgressDialog = new ProgressDialog(context,
				R.style.CustomProgressDialog);
		ProgressDialog.setContentView(R.layout.progressdialog);
		ProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		ProgressDialog.setCanceledOnTouchOutside(false);
		return null;
	}

	public void onWindowFocusChanged(boolean hasFocus) {

		if (ProgressDialog == null) {
			return;
		}
	}

	/**
	 *
	 * [Summary] setTitile 标题
	 * 
	 * @param strTitle
	 * @return
	 *
	 */
	public ProgressDialog setTitile(String strTitle) {
		return ProgressDialog;
	}

	/**
	 *
	 * [Summary] setMessage 提示内容
	 * 
	 * @param strMessage
	 * @return
	 *
	 */
	public ProgressDialog setMessage(String strMessage) {
		TextView tvMsg = (TextView) ProgressDialog
				.findViewById(R.id.tv_loadingmsg);
		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}
		return ProgressDialog;
	}

}
