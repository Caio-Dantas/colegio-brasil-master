package t.br.tcc;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class activity_freq extends Activity implements View.OnClickListener {
    ViewDialog viewDialog;
    setget sg = new setget();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freq);

        //cria o listener das imagens do rodape e o loading
        ImageView imgSair = findViewById(R.id.imgSair);
        imgSair.setOnClickListener(this);

        ImageView imgHome = findViewById(R.id.imgHome);
        imgHome.setOnClickListener(this);

        ImageView imgProf = findViewById(R.id.imgProfile);
        imgProf.setOnClickListener(this);

        viewDialog = new ViewDialog(this);

        //async que carrega as frequencias do user
        new Load().execute();

    }

    //botoes do rodape
    @Override
    public void onClick(View v) {
        Click click = new Click();
        click.Clicked(v,this);
    }

    //Load carrega todas as frequencias do aluno, separadas por materia, e possui geral e parcial
    class Load extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            viewDialog.showDialog();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpHandler sh = new HttpHandler();
            String json = sh.makeServiceCall("http://"+ HttpHandler.ip +":81/pegaFreq.php?id="+sg.getId()+"");
            try {
                JSONObject jsonn = new JSONObject(json);
                JSONArray a = jsonn.getJSONArray("data");
                Atot.clear();
                Adisc.clear();
                Afalta.clear();
                Adad.clear();
                for (int i = 0; i < a.length(); i++) {
                    JSONObject n = a.getJSONObject(i);
                    //Log.i("teste", n.getString("id"));
                    //txtSen.setText(n.getString("id"));
                    if(n.isNull("total"))
                        Atot.add("0");
                    else {
                        Atot.add(n.getString("total"));
                    }
                    Adisc.add(n.getString("disc"));
                    if(n.isNull("faltas"))
                        Afalta.add("0");
                    else {
                        Afalta.add(n.getString("faltas"));
                    }if(n.isNull("dadas"))
                        Adad.add("0");
                    else {
                        Adad.add(n.getString("dadas"));
                    }
                }

                new LoadDefault().execute();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //viewDialog.hideDialog();
            try{
                //((ListView) findViewById(R.id.lista)).removeAllViews();
                //((ListView) findViewById(R.id.list_freq)).setAdapter(new Lista());
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    //adiciona os itens do Load num listView
    class Lista extends BaseAdapter
    {

        @Override
        public int getCount () {
            return (Adisc.size());
        }

        @Override
        public Object getItem ( int position){
            return null;
        }

        @Override
        public long getItemId ( int position){
            return 0;
        }

        @Override
        public View getView (int position, View convertView, ViewGroup parent){
            try {
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.cell_freq, null);
                ProgressBar pgA = convertView.findViewById(R.id.fp1);
                ProgressBar pgG = convertView.findViewById(R.id.fp2);
                TextView m1 = convertView.findViewById(R.id.fn1);
                TextView m2 = convertView.findViewById(R.id.fn2);
                TextView txtm = convertView.findViewById(R.id.fmateria);
                txtm.setText(Adisc.get(position));
                int falta, pres, total, parcial, geral, dadas;
                falta = Integer.parseInt(Afalta.get(position));
                total = Integer.parseInt(Atot.get(position));
                dadas = Integer.parseInt(Adad.get(position));
                //calcula as presenças
                pres = dadas - falta;
                //porcentagem geral
                if(total!=0)
                geral = (100 * pres)/total;
                else{
                    geral = 0;
                }
                //porcentagem parcial
                if(dadas!=0)
                parcial = (100 * pres)/dadas;
                else{
                    parcial=0;
                }
                pgA.setProgress(parcial);
                pgG.setProgress(geral);
                m1.setText(parcial+"%");
                m2.setText(geral+"%");

            }catch (Exception e){
                e.printStackTrace();
            }
            return convertView;
        }

    }

    class LoadDefault extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            HttpHandler sh = new HttpHandler();
            String json = sh.makeServiceCall("http://"+ HttpHandler.ip +":81/pegaDisc.php?id="+sg.getId()+"");
            try {
                JSONObject jsonn = new JSONObject(json);
                JSONArray a = jsonn.getJSONArray("data");
                for (int i = 0; i < a.length(); i++) {
                    JSONObject n = a.getJSONObject(i);
                    All.add(n.getString("disc"));
                }


                for (int i = 0; i < All.size(); i++) {
                    if(Adisc.indexOf(All.get(i))>-1){
                            break;
                        }
                        else{
                                Adisc.add(All.get(i));
                                Adad.add("0");
                                Afalta.add("0");
                                Atot.add("0");
                        }
                }


            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try{
                ((ListView)findViewById(R.id.list_freq)).setAdapter(new Lista());
            }catch (Exception e){
                e.printStackTrace();
            }
            viewDialog.hideDialog();
        }
    }
    ArrayList<String> Atot = new ArrayList<>(), Adisc = new ArrayList<>(), Afalta = new ArrayList<>(), Adad = new ArrayList<>(), All = new ArrayList<>();
}

