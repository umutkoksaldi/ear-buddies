package ch.epfl.sweng.project.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.InputStream;
import java.util.Map;


class DownloadImageMarker extends AsyncTask<String, Void, Bitmap> {
    private MarkerOptions mMarker;
    private Bitmap bitmap;
    private Map<Long, Bitmap> mImages;
    private long mId;


    public DownloadImageMarker(MarkerOptions marker, Map<Long, Bitmap> images, long id){
        mMarker = marker;
        mImages = images;
        mId = id;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String url = params[0];
        Bitmap image = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            image = BitmapFactory.decodeStream(in);
        } catch (Exception e){
            Log.e("Error", e.getMessage() +"");
        }
        if (image != null){
            image = scaleDown(image, 100, true);
        }
        mImages.put(mId, image);
        return image;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        bitmap = result;
        mMarker.icon(BitmapDescriptorFactory.fromBitmap(result));
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                maxImageSize / realImage.getWidth(),
                maxImageSize / realImage.getHeight());
        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }
}
