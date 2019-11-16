package t.br.tcc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class activity_calendar extends Activity implements View.OnClickListener {
    ViewDialog viewDialog;
    setget sg = new setget();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        //instancia todos os objetos
        CalendarView cv = findViewById(R.id.calendarView);

        //cria o listener das imagens do rodape e o loading
        ImageView imgSair = findViewById(R.id.imgSair);
        imgSair.setOnClickListener(this);

        ImageView imgHome = findViewById(R.id.imgHome);
        imgHome.setOnClickListener(this);

        ImageView imgProf = findViewById(R.id.imgProfile);
        imgProf.setOnClickListener(this);

        viewDialog = new ViewDialog(this);



        //pega a data selecionado pelo user
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange( CalendarView view, int year, int month, int dayOfMonth) {
                if(month<9){
                    String date = dayOfMonth + "/0" + (month + 1) + "/" + year;
                    //txtD.setText(date);
                }else {
                    String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                    //txtD.setText(date);
                }

            }
        });

        //chama o async Load que carrega todos os eventos disp pro user
        new Load().execute();


        //onClick do list com todos os eventos, pega o clicado
        ListView lstC = findViewById(R.id.listC);
        lstC.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //salva as informações do evento da posição
                sg.setDesc(Adesc.get(position));
                sg.setDia(Adia.get(position));
                //abre um popUp para exibir os detalhes do evento
                Intent it = new Intent(activity_calendar.this, activity_pop_calendar.class);
                startActivity(it);
            }
        });

    }

    //botoes do rodape
    @Override
    public void onClick(View v) {
        Click click = new Click();
        click.Clicked(v,this);
    }

    //Load para carregar todos eventos disp
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
            String json = sh.makeServiceCall("http://"+sh.ip+":81/pegaData.php?id="+sg.getId()+"");
            try {
                JSONObject jsonn = new JSONObject(json);
                JSONArray a = jsonn.getJSONArray("data");
                Atit.clear();
                Adia.clear();
                Adesc.clear();
                for (int i = 0; i < a.length(); i++) {
                    JSONObject n = a.getJSONObject(i);
                    //Log.i("teste", n.getString("id"));
                    //txtSen.setText(n.getString("id"));
                    Adia.add(n.getString("dia"));
                    Atit.add(n.getString("tit"));
                    Adesc.add(n.getString("desc"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            viewDialog.hideDialog();
            try{
                //((ListView) findViewById(R.id.lista)).removeAllViews();
                ((ListView) findViewById(R.id.listC)).setAdapter(new Lista());
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    //classe que adiciona os itens recolhidos no Load para um listView
    class Lista extends BaseAdapter
    {

        @Override
        public int getCount () {
            return (Adia.size());
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
        public View getView ( int position, View convertView, ViewGroup parent){
            try {
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.cell_evento, null);
                TextView d = convertView.findViewById(R.id.diaC);
                TextView dc = convertView.findViewById(R.id.descC);
                ImageView iv = convertView.findViewById(R.id.imageView9);
                ImageView iv2 = convertView.findViewById(R.id.imageView12);
                iv.setImageResource(R.mipmap.ic_m);
                iv2.setImageResource(R.color.colorAccent);
                d.setText(Adia.get(position).substring(0,5));
                dc.setText(Atit.get(position));

            }catch (Exception e){
                e.printStackTrace();
            }
            return convertView;
        }

    }

    ArrayList<String> Adia = new ArrayList<>(), Atit = new ArrayList<>(), Adesc = new ArrayList<>();

}