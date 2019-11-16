package t.br.tcc;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.util.ArrayList;

public class HTTPServer extends AsyncTask <Void, Void, Void> {

    Bitmap img;
    String nome;
    String code;
    public HTTPServer(String name, String encoded)
    {
        nome = name;
        code = encoded;
    }

    @Override
    protected Void doInBackground(Void... params) {
        HttpHandler sh = new HttpHandler();
        ArrayList<NameValuePair> dataToSend = new ArrayList<>();
        dataToSend.add(new BasicNameValuePair("image", code));dataToSend.add(new BasicNameValuePair("name", nome));
        HttpParams httpRequestParams = getHttpRequestParams();
        DefaultHttpClient client = new DefaultHttpClient(httpRequestParams);
        HttpPost post = new HttpPost("http://" + sh.ip + ":81/saveFoto.php");
        try {
            post.setEntity(new UrlEncodedFormEntity(dataToSend));
            client.execute(post);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage().toString());
        }
        return null;
    }

    @Override
    public void onPostExecute(Void aVoid)
    {
        super.onPostExecute(aVoid);
    }

    private HttpParams getHttpRequestParams()
    {
        HttpParams httpRequestParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpRequestParams, 1000*30);
        HttpConnectionParams.setSoTimeout(httpRequestParams, 1000*30);
        return httpRequestParams;

    }
}
