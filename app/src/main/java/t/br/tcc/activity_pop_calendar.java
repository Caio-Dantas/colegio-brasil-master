package t.br.tcc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class activity_pop_calendar extends AppCompatActivity {
    setget sg = new setget();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_desc_calendar);
        TextView txtD = findViewById(R.id.txtDay);
        TextView txtDesc = findViewById(R.id.txtDescript);

        txtD.setText(sg.getDia());
        txtDesc.setText(sg.getDesc());

        Button btnok = findViewById(R.id.btnOk2);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.3));

        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
