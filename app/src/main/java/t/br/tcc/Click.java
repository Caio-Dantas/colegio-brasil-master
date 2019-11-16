package t.br.tcc;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Click extends AppCompatActivity {



    public void Clicked(View v, Activity a){
        switch (v.getId()) {
            case R.id.imgHome:
                Intent intent = new Intent(a, tela_restrita.class);
                a.startActivity(intent);
                break;
            case R.id.imgSair:
                SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("id","off");
                editor.commit();
                Intent it = new Intent(a, activty_login.class);
                a.startActivity(it);
                break;
            case R.id.imgProfile:
                Intent p = new Intent(a, activity_inserir.class);
                a.startActivity(p);
                break;

        }
    }

}
