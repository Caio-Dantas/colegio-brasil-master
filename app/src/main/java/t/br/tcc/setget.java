package t.br.tcc;

import android.graphics.Bitmap;

public class setget {
    public static String id;
    public static Bitmap bm;
    public static String d;
    public static String dia;

    public void setId(String val)
    {
        id = val;
    }
    public String getId()
    {
        return id;
    }

    public void setBm(Bitmap bit) {
        bm = bit;
    }

    public Bitmap getBm() {
        return bm;
    }

    public void setDesc(String desc)
    {
        d = desc;
    }
    public String getDesc()
    {
        return d;
    }

    public void setDia(String date)
    {
        dia = date;
    }
    public String getDia()
    {
        return dia;
    }}

