package t.br.tcc;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
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

public class activity_nota extends Activity implements View.OnClickListener {
    setget sg = new setget();
    ViewDialog vd = new ViewDialog(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota);

        ImageView imgSair = findViewById(R.id.imgSair);
        imgSair.setOnClickListener(this);

        ImageView imgHome = findViewById(R.id.imgHome);
        imgHome.setOnClickListener(this);

        ImageView imgProf = findViewById(R.id.imgProfile);
        imgProf.setOnClickListener(this);

        new Load().execute();
    }


    @Override
    public void onClick(View v) {
        Click click = new Click();
        click.Clicked(v,this);

    }


    class Load extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            vd.showDialog();
        }

        @Override
        protected String doInBackground(String... strings) {
            //preencheArray();
            HttpHandler sh = new HttpHandler();
            String json = sh.makeServiceCall("http://"+ HttpHandler.ip +":81/pegaNota.php?id="+sg.getId()+"");
            try {
                JSONObject jsonn = new JSONObject(json);
                JSONArray a = jsonn.getJSONArray("data");
                for (int i = 0; i < a.length(); i++) {
                    cb1=false;
                    cb2=false;
                    cb3=false;
                    cb4=false;

                    JSONObject n = a.getJSONObject(i);
                    //Log.i("teste", n.getString("id"));
                    //txtSen.setText(n.getString("id"));
                    if(Amat.indexOf(n.getString("mat"))>-1){
                    }else{
                        Amat.add(n.getString("mat"));
                    }
                    String bim = n.getString("bim");
                    if(bim.equals("1")){
                        An1.add(n.getString("nota"));
                        cb1=true;
                    }
                    else if(bim.equals("2")){
                        An2.add(n.getString("nota"));
                        cb2=true;
                    }else if(bim.equals("3")){
                        An3.add(n.getString("nota"));
                        cb3=true;
                    }else if(bim.equals("4")){
                        An4.add(n.getString("nota"));
                        cb4=true;
                    }
                    /*if(!cb1)
                        An1.add("0");
                    if(!cb2)
                        An2.add("0");
                    if(!cb3)
                        An3.add("0");
                    if(!cb4)
                        An4.add("0");*/

                    //An2.add("0");
                    //An3.add("0");
                    //An4.add("0");

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try{
                //preencheArray();
                //((ListView)findViewById(R.id.listaN)).setAdapter(new Lista());
                new LoadDefault().execute();

            }
            catch(Exception e){
                e.printStackTrace();
            }
            vd.hideDialog();
        }
    }

    class Lista extends BaseAdapter
    {

        @Override
        public int getCount () {
            return (Amat.size());
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
                convertView = inflater.inflate(R.layout.cell_nota, null);
                TextView m = convertView.findViewById(R.id.materia);
                TextView n1 = convertView.findViewById(R.id.n1);
                TextView n2 = convertView.findViewById(R.id.n2);
                TextView n3 = convertView.findViewById(R.id.n3);
                TextView n4 = convertView.findViewById(R.id.n4);
                ProgressBar pb1 = convertView.findViewById(R.id.p1);
                ProgressBar pb2 = convertView.findViewById(R.id.p2);
                ProgressBar pb3 = convertView.findViewById(R.id.p3);
                ProgressBar pb4 = convertView.findViewById(R.id.p4);
                //n.setText(Anome.get(position));
                //t.setText(Atipo.get(position));
                m.setText(Amat.get(position));
                if(Float.parseFloat(An1.get(position))<6){
                    pb1.setProgressTintList(ColorStateList.valueOf(Color.RED));
                }
                pb1.setProgress(Math.round(Float.parseFloat(An1.get(position))*10));
                //pb1.setProgress(89);
                if(Float.parseFloat(An2.get(position))<6){
                    pb2.setProgressTintList(ColorStateList.valueOf(Color.RED));
                }
                pb2.setProgress((Math.round(Float.parseFloat(An2.get(position))))*10);
                if(Float.parseFloat(An3.get(position))<6){
                    pb3.setProgressTintList(ColorStateList.valueOf(Color.RED));
                }
                pb3.setProgress((Math.round(Float.parseFloat(An3.get(position))*10)));
                if(Float.parseFloat(An4.get(position))<6){
                    pb4.setProgressTintList(ColorStateList.valueOf(Color.RED));
                }
                pb4.setProgress((Math.round(Float.parseFloat(An4.get(position))*10)));


                n1.setText(An1.get(position));
                n2.setText(An2.get(position));
                n3.setText(An3.get(position));
                n4.setText(An4.get(position));

                //n1.setText((An1.get(position)+"00").substring(0,4));
                //n2.setText((An2.get(position)+"00").substring(0,4));
                //n3.setText((An3.get(position)+"00").substring(0,4));
                //n4.setText((An4.get(position)+"00").substring(0,4));

            }catch (Exception e){
                e.printStackTrace();
            }
            return convertView;
        }

    }

    public void preencheArray(){
        for(int i = 0; i<Amat.size();i++){
            An1.add("0");
            An2.add("0");
            An3.add("0");
            An4.add("0");
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

                int si = Amat.size();

                for (int i = 0; i < All.size(); i++) {
                    for (int p = 0; p < si; p++) {
                         if(All.get(i).equals(Amat.get(p))){
                            break;
                        }
                        else{
                            if(!All.get(i).equals(Amat.get(Amat.size()-1))) {
                                ToAdd.add(All.get(i));
                                if(Amat.indexOf(All.get(i))>-1){
                                }else{
                                    Amat.add(All.get(i));
                                }
                                An1.add("0");
                                An2.add("0");
                                An3.add("0");
                                An4.add("0");
                            }
                        }
                    }
                }
                   // Log.e("count", i+"");


            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try{
                preencheArray();
                ((ListView)findViewById(R.id.listaN)).setAdapter(new Lista());
            }catch (Exception e){
                e.printStackTrace();
            }
            vd.hideDialog();
        }
    }

    ArrayList<String> Amat = new ArrayList<>(), An1 = new ArrayList<>(), An2 = new ArrayList<>(), An3 = new ArrayList<>(), An4 = new ArrayList<>(), All = new ArrayList<>(), ToAdd = new ArrayList<>();
    Boolean cb1, cb2, cb3, cb4;
}
