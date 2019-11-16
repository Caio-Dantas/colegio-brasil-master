package t.br.tcc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class activty_login extends AppCompatActivity {

    setget sg = new setget();
    ViewDialog viewDialog;

    SharedPreferences pref;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //findViewById(R.id.).setBackground(ContextCompat.getDrawable(context,R.drawable.bg_gradient));
        findViewById(R.id.vLogin).setBackground(ContextCompat.getDrawable(activty_login.this, R.drawable.bgpop));

         pref = getSharedPreferences("pref", MODE_PRIVATE);
         editor = pref.edit();


         String a = pref.getString("id","");
        if(!"off".equals(a)){
            sg.setId(pref.getString("id",""));
            Intent intent = new Intent(getApplicationContext(), tela_restrita.class);
            startActivity(intent);
        }


        viewDialog = new ViewDialog(this);


        final Button btnEnt = (Button)findViewById(R.id.btnEntrar);

        btnEnt.setEnabled(true);

        final TextView txtUsu = (TextView)findViewById(R.id.lblMatricula);
        final TextView txtSen = (TextView)findViewById(R.id.lblSenha);

        btnEnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    new Load().execute();
                    btnEnt.setEnabled(false);
                    //btnEnt.setBackgroundColor(Color.parseColor("#BEBEBE"));
            }
        });

        txtSen.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    new Load().execute();
                    btnEnt.setEnabled(false);
                    //btnEnt.setBackgroundColor(Color.parseColor("#BEBEBE"));
                }
                return false;
            }
        });


    }

    class Load extends AsyncTask<String,String,String>
    {


        private final ProgressDialog dialog = new ProgressDialog(activty_login.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this.dialog.setMessage("Loading");
            //this.dialog.show();
            viewDialog.showDialog();
        }

        @Override
        protected String doInBackground(String... strings) {
            TextView txtUsu = (TextView)findViewById(R.id.lblMatricula);
            final TextView txtSen = (TextView)findViewById(R.id.lblSenha);
            Boolean s = false;
            HttpHandler sh = new HttpHandler();
            String json = sh.makeServiceCall("http://"+sh.ip+":81/login.php?id="+txtUsu.getText()+"&senha="+txtSen.getText()+"");
            try {
                JSONObject jsonn = new JSONObject(json);
                JSONArray a = jsonn.getJSONArray("data");
                //if(a.length()>0) {
                    for (int i = 0; i <= a.length(); i++) {
                        JSONObject n = a.getJSONObject(i);
                        //Log.i("teste", n.getString("id"));
                        //txtSen.setText(n.getString("id"));
                        s=true;
                        editor.putString("id", txtUsu.getText().toString());
                        editor.commit();
                        sg.setId(txtUsu.getText().toString());
                        Intent intent = new Intent(getApplicationContext(), tela_restrita.class);
                        startActivity(intent);

                    }
                //}else{
                    //Toast.makeText(getBaseContext(), "n", Toast.LENGTH_LONG).show();
                  //  Intent intent = new Intent(getApplicationContext(), popLogin.class);
                  //  startActivity(intent);
                //}
            } catch (Exception e) {
                e.printStackTrace();
                if(s==false) {
                    Intent intent = new Intent(getApplicationContext(), popLogin.class);
                    startActivity(intent);
                    //btnEnt.setEnabled(true);
                }
            }
            //Toast.makeText(getBaseContext(), "oi", Toast.LENGTH_LONG).show();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            final Button btnEnt = findViewById(R.id.btnEntrar);
            //this.dialog.dismiss();
            viewDialog.hideDialog();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    //re-enable the button
                    btnEnt.setEnabled(true);

                }
            }, 1000);
        }
    }



    @Override
    public void onBackPressed() {
    }

}
