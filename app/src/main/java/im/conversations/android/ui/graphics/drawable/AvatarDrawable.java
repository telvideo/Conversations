/*
 * Copyright 2019-2023 Daniel Gultsch
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package im.conversations.android.ui.graphics.drawable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import com.google.android.material.elevation.SurfaceColors;
import com.google.common.base.Strings;
import im.conversations.android.R;
import im.conversations.android.database.model.AddressWithName;
import im.conversations.android.util.ConsistentColorGeneration;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jxmpp.jid.Jid;

public class AvatarDrawable extends ColorDrawable {

    // pattern from @cketti (K-9 Mail)
    private static final Pattern LETTER_PATTERN = Pattern.compile("\\p{L}\\p{M}*");

    private final Paint paint;
    private final Paint textPaint;
    private final String letter;
    private final int intrinsicHeight;
    private final int intrinsicWidth;

    private final Context context;

    public AvatarDrawable(final Context context, final AddressWithName addressWithName) {
        this.context = context;
        final String name = addressWithName.name;
        final Jid key = addressWithName.address;
        this.paint = getPaint(addressWithName.address);
        this.textPaint = getTextPaint();
        final Matcher matcher = LETTER_PATTERN.matcher(Strings.nullToEmpty(name));
        this.letter = matcher.find() ? matcher.group().toUpperCase(Locale.ROOT) : null;
        final int avatarDrawableSize =
                context.getResources().getDimensionPixelSize(R.dimen.avatar_drawable_size);
        this.intrinsicHeight = avatarDrawableSize;
        this.intrinsicWidth = avatarDrawableSize;
    }

    private Paint getPaint(final Jid key) {
        final Paint paint = new Paint();
        paint.setColor(
                key == null ? 0xff757575 : ConsistentColorGeneration.harmonized(context, key));
        paint.setAntiAlias(true);
        return paint;
    }

    private Paint getTextPaint() {
        final Paint textPaint = new Paint();
        textPaint.setColor(SurfaceColors.SURFACE_0.getColor(context));
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        return textPaint;
    }

    @Override
    public void draw(final Canvas canvas) {
        final float midX = getBounds().width() / 2.0f;
        final float midY = getBounds().height() / 2.0f;
        final float radius = Math.min(getBounds().width(), getBounds().height()) / 2.0f;
        textPaint.setTextSize(radius);
        final Rect r = new Rect();
        canvas.getClipBounds(r);
        final int cHeight = r.height();
        final int cWidth = r.width();
        canvas.drawCircle(midX, midY, radius, paint);
        if (letter == null) {
            return;
        }
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.getTextBounds(letter, 0, letter.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(letter, x, y, textPaint);
    }

    @Override
    public int getIntrinsicHeight() {
        return intrinsicHeight;
    }

    @Override
    public int getIntrinsicWidth() {
        return intrinsicWidth;
    }

    public Bitmap toBitmap() {
        final Bitmap bitmap =
                Bitmap.createBitmap(
                        getIntrinsicWidth(), getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        this.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        this.draw(canvas);
        return bitmap;
    }
}
