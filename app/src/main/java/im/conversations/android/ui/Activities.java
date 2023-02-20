package im.conversations.android.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.elevation.SurfaceColors;

public final class Activities {

    private Activities() {}

    public static void setStatusAndNavigationBarColors(
            final AppCompatActivity activity, final View view) {
        final var isLightMode = isLightMode(activity);
        final var window = activity.getWindow();
        final var flags = view.getSystemUiVisibility();
        // an elevation of 4 matches the MaterialToolbar elevation
        window.setStatusBarColor(SurfaceColors.SURFACE_0.getColor(activity));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.setNavigationBarColor(SurfaceColors.SURFACE_1.getColor(activity));
            if (isLightMode) {
                view.setSystemUiVisibility(
                        flags
                                | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                                | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            }
        } else if (isLightMode) {
            view.setSystemUiVisibility(flags | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    private static boolean isLightMode(final Context context) {
        final int nightModeFlags =
                context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags != Configuration.UI_MODE_NIGHT_YES;
    }
}