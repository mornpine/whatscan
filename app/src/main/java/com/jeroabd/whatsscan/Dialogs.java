package com.jeroabd.whatsscan;

 
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class Dialogs extends Dialog   {

public Activity c;
public Dialog d;
public Button yes, no;

public Dialogs(Activity a) {
super(a);

this.c = a;
}

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
requestWindowFeature(Window.FEATURE_NO_TITLE);
setContentView(R.layout.dialog);
yes = (Button) findViewById(R.id.yes);
no = (Button) findViewById(R.id.no);
 

}
 
}