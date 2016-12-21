package ch.epfl.sweng.project.view.util_view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.InputStream;
import java.util.Map;


public class DownloadImageMarker extends AsyncTask<String, Void, Bitmap> {
    private MarkerOptions mMarker;
    private Bitmap bitmap;
    private Map<Long, Bitmap> mImages;
    private long mId;


    public DownloadImageMarker(MarkerOptions marker, Map<Long, Bitmap> images, long id) {
        mMarker = marker;
        mImages = images;
        mId = id;
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                maxImageSize / realImage.getWidth(),
                maxImageSize / realImage.getHeight());
        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());

        return Bitmap.createScaledBitmap(realImage, width,
                height, filter);
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String url = params[0];
        Bitmap image = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            image = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage() + "");
        }
        if (image != null) {
            image = scaleDown(image, 100, true);
        }
        mImages.put(mId, getCircleBitmap(image));
        return image;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        bitmap = result;
        mMarker.icon(BitmapDescriptorFactory.fromBitmap(result));
        //mMarker.icon(BitmapDescriptorFactory.fromBitmap(getCircleBitmap(result)));
    }

    private Bitmap getCircleBitmap(Bitmap bitmap) {

/*
        Bitmap bitmap = getthebitmapyouwanttoshowinacirclefromsomewhere;
        Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        BitmapShader shader = new BitmapShader (bitmap,  TileMode.CLAMP, TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        paint.setAntiAlias(true);
        Canvas c = new Canvas(circleBitmap);
        c.drawCircle(bitmap.getWidth()/2, bitmap.getHeight()/2, bitmap.getWidth()/2, paint);

        myImageView.setImageBitmap(circleBitmap);
*/

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);


        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        paint.setAntiAlias(true);
        Canvas c = new Canvas(output);
        c.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);


        return output;
    }
}


