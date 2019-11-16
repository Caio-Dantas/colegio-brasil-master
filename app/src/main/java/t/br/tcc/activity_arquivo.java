package t.br.tcc;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class activity_arquivo extends Activity implements View.OnClickListener {
    ViewDialog viewDialog;
    setget sg = new setget();
    String load ="";
    Click click = new Click();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arquivos);

        //cria o listener das imagens do rodape e o loading
        ImageView imgSair = findViewById(R.id.imgSair);
        imgSair.setOnClickListener(this);

        ImageView imgHome = findViewById(R.id.imgHome);
        imgHome.setOnClickListener(this);

        ImageView imgProf = findViewById(R.id.imgProfile);
        imgProf.setOnClickListener(this);

        viewDialog = new ViewDialog(this);

        //chama o asyncTask para carrregar os aquivos disponiveis
        new Load().execute();

        //criar o click do arquivo selecionado
        ListView lst = findViewById(R.id.list_arq);
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                load =  Apath.get(position);
                //chama o async para fazer o download do arquivo clicado
                    new Download().execute();
            }
        });
    }

    @Override
    public void onClick(View v) {
        click.Clicked(v,this);
    }


    //load utilizado para carregar todos pdf disp
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
            String json = sh.makeServiceCall("http://"+sh.ip+":81/loadPdf.php?id="+sg.getId()+"");
            try {
                JSONObject jsonn = new JSONObject(json);
                JSONArray a = jsonn.getJSONArray("data");
                Anome.clear();

                Amat.clear();
                Apath.clear();
                for (int i = 0; i < a.length(); i++) {
                    JSONObject n = a.getJSONObject(i);
                    Anome.add(n.getString("nome"));
                    Amat.add(n.getString("mat"));
                    Apath.add(n.getString("path"));
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
                ((ListView) findViewById(R.id.list_arq)).setAdapter(new Lista());
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    //lista que adiciona todos os itens recebidos pelo 'Load' em um listView
    class Lista extends BaseAdapter
    {

        @Override
        public int getCount () {
            return (Anome.size());
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
                convertView = inflater.inflate(R.layout.cell_arquivo, null);
                TextView m = convertView.findViewById(R.id.txtMat);
                TextView d = convertView.findViewById(R.id.txtDesc);
                m.setText(Amat.get(position));
                d.setText(Anome.get(position));

            }catch (Exception e){
                e.printStackTrace();
            }
            return convertView;
        }

    }

    //baixar os arquivos passados pela variavel 'load'
    class Download extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            viewDialog.showDialog();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpHandler sh = new HttpHandler();
            //String json = sh.makeServiceCall("http://" + sh.ip + ":81/DownPdf.php?arq="+load+"");
            try {
                DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse("http://" + sh.ip + ":81/pdf/" + load + ".pdf");
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                Long reference = dm.enqueue(request);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            viewDialog.hideDialog();
        }
    }

    ArrayList<String> Anome = new ArrayList<>(), Amat = new ArrayList<>(), Apath = new ArrayList<>();
}


