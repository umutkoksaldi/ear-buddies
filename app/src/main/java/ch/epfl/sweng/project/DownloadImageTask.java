package ch.epfl.sweng.project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;


class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private final ImageView mImage;

    public DownloadImageTask(ImageView image){
        this.mImage = image;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String url = params[0];
        Bitmap image = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            image = BitmapFactory.decodeStream(in);
        } catch (Exception e){
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return image;
    }

    protected void onPostExecute(Bitmap result) {
        mImage.setImageBitmap(result);
    }
}
