package com.softwaredepotonline.iPocketDiary;

		import android.app.Activity;
		import android.app.AlertDialog;
		import android.content.DialogInterface;
		import android.content.Intent;
		import android.content.SharedPreferences;
		import android.os.Bundle;
		import android.widget.Button;
		import android.widget.EditText;
		import android.widget.TextView;
		import android.view.View;
		import android.view.Window;
		import android.view.View.OnClickListener;
		import android.content.Context;
		import android.content.SharedPreferences.Editor;

public class ChangePasswordView extends Activity implements OnClickListener
{
	Button btnUpdatePassword;
	Button btnHelpPassword;
	private EditText oldPass;
	private EditText newPass;
	private EditText confirmPass;
	private AlertDialog alertDialog;
	private SharedPreferences sharedPreferences;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle saveInstanceState)
	{
		super.onCreate(saveInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.change_password);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
		final TextView centertext = (TextView) findViewById(R.id.center_text);
		centertext.setText("Change Password");
		oldPass=(EditText)findViewById(R.id.old_password);
		newPass=(EditText)findViewById(R.id.new_password);
		confirmPass=(EditText)findViewById(R.id.confpassword);
		btnUpdatePassword=(Button)findViewById(R.id.update);
		btnUpdatePassword.setOnClickListener(this);
		btnHelpPassword=(Button)findViewById(R.id.changepasswordhelp);
		btnHelpPassword.setOnClickListener(this);
	}
	public void onClick(View v)
	{

		if(v.getId()==R.id.changepasswordhelp)
		{
			Intent changePassIntent= new Intent(this,HelpPassword.class);
			startActivity(changePassIntent);
		}
		else if(v.getId()==R.id.update)
		{
			String p1 = oldPass.getText().toString();
			String p2 = newPass.getText().toString();
			String p3 = confirmPass.getText().toString();
			if(p2.equals(p3))
			{
				if(p2.length()>=6 || p3.length()>=6)
				{
					sharedPreferences = getSharedPreferences(LoginView.PASSWORD_PREF_KEY,  Context.MODE_WORLD_WRITEABLE);
					Editor prefsPrivateEditor = sharedPreferences.edit();
					String md5hash = LoginView.getMd5Hash(p2);
					prefsPrivateEditor.putString(LoginView.PASSWORD_PREF_KEY, md5hash);
					prefsPrivateEditor.commit();
					alertDialog = new AlertDialog.Builder(this).create();
					alertDialog.setTitle("Password");
					alertDialog.setMessage("Password Changed!!!!!!");
					alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							Intent categoryIntent= new Intent(ChangePasswordView.this,ItemList.class);
							startActivity(categoryIntent);
							finish();
							return;
						}
					});
				}
				else
				{
					if(p1.length()==0 || p2.length()==0)
					{
						alertDialog = new AlertDialog.Builder(this).create();
						alertDialog.setTitle("Password");
						alertDialog.setMessage("Please enter old password.");
						//alertDialog.setIcon(icon)
						alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								return;
							}});
					}
					else
					{
						alertDialog = new AlertDialog.Builder(this).create();
						alertDialog.setTitle("Password");
						alertDialog.setMessage("Passwords must be at least 6 characters.");
						alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								return;
							}
						});
					}
				}

			}
			else
			{
				oldPass.setText("");
				newPass.setText("");
				alertDialog = new AlertDialog.Builder(this).create();
				alertDialog.setTitle("Password");
				alertDialog.setMessage("Passwords do not match.");
				alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				});
			}

			alertDialog.show();
		}







	}
}
