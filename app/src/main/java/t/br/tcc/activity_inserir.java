package t.br.tcc;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

public class activity_inserir extends Activity implements View.OnClickListener  {
    String id;
    setget sg = new setget();
    ViewDialog vd = new ViewDialog(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

       /* TextView txtA, txtTurma, txtI, txtD, txtMat, txtN;
         txtN = findViewById(R.id.txtNome);
         txtMat = findViewById(R.id.txtId);
         txtD = findViewById(R.id.txtData);
         txtI = findViewById(R.id.txtIdade);
         txtTurma = findViewById(R.id.txtTurma);
         txtA = findViewById(R.id.txtAno);*/

        // txtN.setTextSize(TypedValue.COMPLEX_UNIT_PX,0.4%);

        new LoadInfo().execute();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_inserir);

        Button btnI = findViewById(R.id.button);
    verifyPermissions();
        btnI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromGallery();
            }
        });
        setget sg = new setget();
        id=sg.getId();

        ImageView imgSair = findViewById(R.id.imgSair);
        imgSair.setOnClickListener(this);

        ImageView imgHome = findViewById(R.id.imgHome);
        imgHome.setOnClickListener(this);

        ImageView imageView = findViewById(R.id.imgFt);
        imageView.setImageBitmap(sg.getBm());
    }
    @Override
    public void onClick(View v) {
            Click click = new Click();
            click.Clicked(v,this);

    }
    private void pickFromGallery(){
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent, 0);
    }
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        // Result code is RESULT_OK only if the user selects an Image
        try {
            if (resultCode == Activity.RESULT_OK)
                switch (requestCode) {
                    case 0:
                        //data.getData return the content URI for the selected Image
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        // Get the cursor
                        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                        // Move to first row
                        cursor.moveToFirst();
                        //Get the column index of MediaStore.Images.Media.DATA
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        //Gets the String value in the column
                        String imgDecodableString = cursor.getString(columnIndex);
                        cursor.close();
                        // Set the Image in ImageView after decoding the String
                        ImageView imageView = findViewById(R.id.imgFt);
                        Bitmap bm = BitmapFactory.decodeFile(imgDecodableString);


                        if(bm.getWidth()>=1024||bm.getHeight()>=1024) {
                           // Toast.makeText(this, "A resolução da imagem excede o limite", Toast.LENGTH_LONG).show();
                            bm = getResizedBitmap(bm, 500);
                        }


                        ExifInterface ei = null;
                        try {
                            ei = new ExifInterface(imgDecodableString);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                ExifInterface.ORIENTATION_UNDEFINED);

                        Bitmap rotatedBitmap = null;
                        try {
                            switch (orientation) {

                                case ExifInterface.ORIENTATION_ROTATE_90:
                                    rotatedBitmap = rotateImage(bm, 90);
                                    break;

                                case ExifInterface.ORIENTATION_ROTATE_180:
                                    rotatedBitmap = rotateImage(bm, 180);
                                    break;

                                case ExifInterface.ORIENTATION_ROTATE_270:
                                    rotatedBitmap = rotateImage(bm, 270);
                                    break;

                                case ExifInterface.ORIENTATION_NORMAL:
                                default:
                                    rotatedBitmap = bm;
                            }

                        } catch (Exception e) {
                            Toast.makeText(this, "Não foi possível carregar a imagem", Toast.LENGTH_SHORT);
                        }
                        imageView.setImageBitmap(rotatedBitmap);


                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        rotatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream.toByteArray();

                        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        foto = encoded;
                        path = imgDecodableString;
                        new Load().execute();

                        break;

                }
        }catch (Exception e){
            Toast.makeText(this, "Não foi possível carregar a imagem", Toast.LENGTH_SHORT).show();
        }
    }

    private void verifyPermissions(){
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[1]) == PackageManager.PERMISSION_GRANTED){
            //recreate();
        }else{
            ActivityCompat.requestPermissions(activity_inserir.this,
                    permissions,
                    0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        verifyPermissions();
    }

    class Load extends AsyncTask<String,String,String>
    {
        @Override
        protected String doInBackground(String... strings) {

            try {
                HTTPServer HTTPServer = new HTTPServer(id, foto);
                HTTPServer.execute();
            }catch (Exception e){
                Toast.makeText(activity_inserir.this, "Não foi possível inserir", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

    }
    class LoadInfo extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            vd.showDialog();

        }

        @Override
        protected String doInBackground(String... strings) {
            //chamar pegaInfo e atribiuir todos campos
            //no campo ano atual e data nasc usar codigo
            HttpHandler sh = new HttpHandler();
            String json = sh.makeServiceCall("http://"+sh.ip+":81/pegaInfo.php?id="+sg.getId()+"");
            try {
                JSONObject jsonn = new JSONObject(json);
                JSONArray a = jsonn.getJSONArray("data");
                for (int i = 0; i <= a.length(); i++) {
                    JSONObject n = a.getJSONObject(i);
                    //Log.i("teste", n.getString("id"));
                    //txtSen.setText(n.getString("id"));
                    //Date c = Calendar.getInstance().getTime();
                    //SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    //String format = df.format(c);

                    Calendar c = Calendar.getInstance(TimeZone.getDefault());
                    int d = c.get(Calendar.DAY_OF_MONTH);
                    int m = (c.get(Calendar.MONTH)+1);
                    int y = (int) c.get(Calendar.YEAR);
                    textA = (y+"");
                    String nasc = n.getString("nasc");
                    String dia = nasc.substring(8,10);
                    String mes = nasc.substring(5,7);
                    String ano = nasc.substring(0,4);
                    textD=((dia+"/"+mes+"/"+ano));
                    textN = (n.getString("nome"));
                    textMat =(sg.getId());
                    String anoT  = n.getString("anoAtual");
                    String T = n.getString("turma");
                    textTurma = ((anoT+" "+T));
                    int ai = y;
                    int bi = Integer.parseInt(ano);
                    int anoI = ai-bi;
                    int bbi = m;
                    if(bbi<Integer.parseInt(mes) || d<Integer.parseInt(dia))
                        anoI = anoI - 1;

                    textI=(anoI)+"";
                }
            } catch (Exception e) {
                e.printStackTrace();
                }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            TextView txtN = findViewById(R.id.txtNome);
            txtN.setText(textN);
            TextView txtMat = findViewById(R.id.txtId);
            txtMat.setText(textMat);
            TextView txtD = findViewById(R.id.txtData);
            txtD.setText(textD);
            TextView txtI = findViewById(R.id.txtIdade);
            txtI.setText(textI);
            TextView txtTurma = findViewById(R.id.txtTurma);
            txtTurma.setText(textTurma);
            TextView txtA = findViewById(R.id.txtAno);
            txtA.setText(textA);
            vd.hideDialog();
        }
    }
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
    String foto, path, textD, textN, textMat, textTurma, textI, textA;

}