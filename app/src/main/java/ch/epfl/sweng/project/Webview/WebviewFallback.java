package ch.epfl.sweng.project.Webview;

/**
 * Created by Antoine Merino on 30/11/2016.
 */

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import ch.epfl.sweng.project.WebviewActivity;

/**
 * A Fallback that opens a Webview when Custom Tabs is not available
 */
public class WebviewFallback implements CustomTabActivityHelper.CustomTabFallback {
    @Override
    public void openUri(Activity activity, Uri uri) {
        Intent intent = new Intent(activity, WebviewActivity.class);
        intent.putExtra(WebviewActivity.EXTRA_URL, uri.toString());
        activity.startActivity(intent);
    }
}