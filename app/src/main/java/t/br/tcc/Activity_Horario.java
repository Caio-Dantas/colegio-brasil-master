package t.br.tcc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class Activity_Horario extends Activity implements View.OnClickListener, OnTouchListener  {
    String dia ="Segunda";
    String[] list = {"Segunda", "Terça","Quarta","Quinta","Sexta"};
    int ind;
    ViewDialog viewDialog;
    setget sg = new setget();
    Button btnSg,btnT, btnQa, btnQi, btnSx;
    private final GestureDetector gestureDetector;
    public Activity_Horario(Context ctx){
        gestureDetector = new GestureDetector(ctx, new GestureListener());
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario);


        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                dia = "Segunda";
                break;
            case Calendar.MONDAY:
                dia = "Segunda";
                break;
            case Calendar.TUESDAY:
                dia = "Terça";
                break;
            case Calendar.WEDNESDAY:
                dia = "Quarta";
                break;
            case Calendar.THURSDAY:
                dia = "Quinta";
                break;
            case Calendar.FRIDAY:
                dia = "Sexta";
                break;
            case Calendar.SATURDAY:
                dia = "Segunda";
                break;
        }


        //cria o listener das imagens do rodape e o loading, e os botoes dos dias
        ImageView imgSair = findViewById(R.id.imgSair);
        imgSair.setOnClickListener(this);

        ImageView imgHome = findViewById(R.id.imgHome);
        imgHome.setOnClickListener(this);

        ImageView imgProf = findViewById(R.id.imgProfile);
        imgProf.setOnClickListener(this);

        btnSg = findViewById(R.id.btnSegunda);
        btnSg.setOnClickListener(this);
        //btnSg.setBackground(ContextCompat.getColor(getBaseContext(),R.color.colorNoite));

        btnT = findViewById(R.id.btnTerca);
        btnT.setOnClickListener(this);

        btnQa = findViewById(R.id.btnQuarta);
        btnQa.setOnClickListener(this);

        btnQi = findViewById(R.id.btnQuinta);
        btnQi.setOnClickListener(this);

        btnSx = findViewById(R.id.btnSexta);
        btnSx.setOnClickListener(this);

        changeColor(btnSg);

        viewDialog = new ViewDialog(this);

        //Load que carrega as aulas
        new Load().execute();

    }



    //todos os onclicks
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSegunda:
                dia="Segunda";
                changeColor(btnSg);
                new Load().execute();
                break;
            case R.id.btnTerca:
                dia="Terça";
                changeColor(btnT);
                new Load().execute();
                break;
            case R.id.btnQuarta:
                dia="Quarta";
                changeColor(btnQa);
                new Load().execute();
                break;
            case R.id.btnQuinta:
                dia="Quinta";
                changeColor(btnQi);
                new Load().execute();
                break;
            case R.id.btnSexta:
                dia="Sexta";
                changeColor(btnSx);
                new Load().execute();
                break;
            case R.id.imgHome:
                Intent intent = new Intent(this, tela_restrita.class);
                startActivity(intent);
                break;
            case R.id.imgSair:
                Intent it = new Intent(this, activty_login.class);
                startActivity(it);
                break;
            case R.id.imgProfile:
                Intent itP = new Intent(this, activity_inserir.class);
                startActivity(itP);
                break;
        }

    }

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
            String json = sh.makeServiceCall("http://"+sh.ip+":81/horario.php?dia="+dia+"&id="+sg.getId()+"");
            try {
                JSONObject jsonn = new JSONObject(json);
                JSONArray a = jsonn.getJSONArray("data");
                Anome.clear();
                Atipo.clear();
                for (int i = 0; i < a.length(); i++) {
                    JSONObject n = a.getJSONObject(i);
                    //Log.i("teste", n.getString("id"));
                    //txtSen.setText(n.getString("id"));
                    Anome.add(n.getString("prof"));
                    Atipo.add(n.getString("mat"));
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
                ((ListView) findViewById(R.id.lista)).setAdapter(new Lista());
        }
        catch(Exception e){
            e.printStackTrace();
            }
        }
    }

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
            public View getView ( int position, View convertView, ViewGroup parent){
                try {
                    LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.cell_layout, null);
                    TextView h = convertView.findViewById(R.id.horario);
                    TextView n = convertView.findViewById(R.id.nome);
                    TextView t = convertView.findViewById(R.id.tipo);
                    h.setText(ho[position]);
                    n.setText(Anome.get(position));
                    t.setText(Atipo.get(position));

                }catch (Exception e){
                    e.printStackTrace();
                }
                return convertView;
        }

    }

    public void changeColor(Button btn){
        btnSg.setBackgroundColor(Color.parseColor("#55a57d"));
        btnT.setBackgroundColor(Color.parseColor("#55a57d"));
        btnQa.setBackgroundColor(Color.parseColor("#55a57d"));
        btnQi.setBackgroundColor(Color.parseColor("#55a57d"));
        btnSx.setBackgroundColor(Color.parseColor("#55a57d"));
        btn.setBackgroundColor(Color.parseColor("#3d7358"));
    }


    private final class GestureListener extends SimpleOnGestureListener {


        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            ind = Arrays.asList(list).indexOf(dia);
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                        result = true;
                    }
                }
                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                    result = true;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    public void onSwipeRight() {
        if(ind!=list.length-1) {
            ind = ind + 1;
            dia = list[ind];
            new Load().execute();
        }
    }

    public void onSwipeLeft() {
        if(ind!=0) {
            ind = ind - 1;
            dia = list[ind];
            new Load().execute();
        }
    }

    public void onSwipeTop() {
    }

    public void onSwipeBottom() {
    }

    ArrayList<String> Anome = new ArrayList<>(), Atipo = new ArrayList<>();
    String[] ho = {"19:10", "19:30", "20:00", "20:30","21:00","21:30"};
}
