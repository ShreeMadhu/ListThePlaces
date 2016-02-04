package com.mad.placesdisplay;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.TextView;

public class CustomProgressDialog {

	private Context context;
	private ProgressDialog customDialog;
	private TextView customDialogTitle;

	public CustomProgressDialog(Context context) {
		this.context = context;
	}

	public void showDialog() {
		customDialog = new ProgressDialog(context);
		customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		customDialog.show();
		customDialog.setCancelable(false);
		customDialog.setCanceledOnTouchOutside(false);
		customDialog.setContentView(R.layout.custom_progress_dialog);
		customDialogTitle = (TextView) customDialog
				.findViewById(R.id.dialogHeading);
		customDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
	}

	public void dismissDialog() {
		if (customDialog != null && customDialog.isShowing()) {
			customDialog.dismiss();
		}
	}

	public void setCustomDialogTitle(String title) {
		customDialogTitle.setText(title);
	}
}
