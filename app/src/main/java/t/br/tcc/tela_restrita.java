package t.br.tcc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class tela_restrita extends AppCompatActivity implements View.OnClickListener {

    setget sg = new setget();

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    //ViewDialog vd = new ViewDialog(this);
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_restrita);

         pref = getSharedPreferences("pref", MODE_PRIVATE);
         editor = pref.edit();
        //Intent intent = new Intent(this, activty_login.class);
        //startActivity(intent);
        setContentView(R.layout.tela_restrita);

        CardView cvB = findViewById(R.id.cvBol);
        cvB.setRadius(15);

        CardView cvH = findViewById(R.id.cvH);
        cvH.setRadius(15);

        CardView cvD = findViewById(R.id.cvD);
        cvD.setRadius(15);

        CardView cvF = findViewById(R.id.cvF);
        cvF.setRadius(15);

        CardView cvC = findViewById(R.id.cvC);
        cvC.setRadius(15);




        cvH.setOnClickListener(this);
        cvB.setOnClickListener(this);
        cvF.setOnClickListener(this);
        cvC.setOnClickListener(this);
        cvD.setOnClickListener(this);



        ImageView imgEu = findViewById(R.id.imageView8);

        ImageView imgRobo = findViewById(R.id.imgRobo);
        imgRobo.setOnClickListener(this);

        ImageView imgSair = findViewById(R.id.imgSair);
        imgSair.setOnClickListener(this);

        ImageView imgHome = findViewById(R.id.imgHome);
        imgHome.setOnClickListener(this);

        ImageView imgProfile = findViewById(R.id.imgProfile);
        imgProfile.setOnClickListener(this);



        new Load().execute();

    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cvBol:
                //abre
                Intent itN = new Intent(this, activity_nota.class);
                startActivity(itN);
                break;
            case R.id.cvH:
                //abre
                Intent intent = new Intent(this, Activity_Horario.class);
                startActivity(intent);
                break;
            case R.id.cvC:
                //abre
                Intent intentC = new Intent(this, activity_calendar.class);
                startActivity(intentC);
                break;
            case R.id.cvF:
                //abre
                Intent intentf = new Intent(this, activity_freq.class);
                startActivity(intentf);
                break;
            case R.id.cvD:
                Intent itA = new Intent(this, activity_arquivo.class);
                startActivity(itA);
                break;
            case R.id.imgHome:
                //Intent intent2 = new Intent(this, tela_restrita.class);
                //startActivity(intent2);
                break;
            case R.id.imgSair:
                editor.putString("id","off");
                editor.commit();
                Intent it = new Intent(this, activty_login.class);
                startActivity(it);
                break;
            case R.id.imgProfile:
                //
                Intent P = new Intent(this, activity_inserir.class);
                startActivity(P);
                break;
            case R.id.imgRobo:
                Intent r = new Intent(this, activity_chatbot.class);
                startActivity(r);
        }

    }


    class Load extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //vd.showDialog();
        }

        @Override
        protected String doInBackground(String... strings) {
            //ImageView imgEu = findViewById(R.id.imageView8);
            CircleImageView imgEu = findViewById(R.id.imageView8);
            HttpHandler sh = new HttpHandler();
            String json = sh.makeServiceCall("http://"+sh.ip+":81/pegaFoto.php?id="+sg.getId()+"");
            try {
                JSONObject jsonn = new JSONObject(json);
                JSONArray a = jsonn.getJSONArray("data");
                for (int i = 0; i < a.length(); i++) {
                    JSONObject n = a.getJSONObject(i);
                    //Log.i("teste", n.getString("id"));
                    //txtSen.setText(n.getString("id"));
                    //Anome.add(n.getString("prof"));

                    byte[] byteArray =  Base64.decode(n.getString("img"), Base64.DEFAULT) ;
                    Bitmap bmp1 = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                    sg.setBm(bmp1);
                    imgEu.setImageBitmap(bmp1);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //vd.hideDialog();
        }
    }



    @Override
    protected void onResume()
    {
        super.onResume();
        new Load().execute();

    }



}
