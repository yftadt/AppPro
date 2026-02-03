package test.app.ui.view.images;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;


import test.app.ui.activity.R;


public class RoundedImageView extends ImageView {
    private Paint paint;
    private Path path;
    private RectF rectF;
    private boolean isCircle;
    //圆半径
    private int cornerRadius;
    float[] radii = new float[]{0, 0,   // 左上角X和Y半径
            0, 0,   // 右上角X和Y半径
            0, 0,   // 右下角X和Y半径
            0, 0    // 左下角X和Y半径
    };

    public RoundedImageView(Context context) {
        this(context, null, R.attr.RadiusImageViewStyle);
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.RadiusImageViewStyle);
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAttrs(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE); // 设置背景色（可选）
        paint.setStyle(Paint.Style.FILL);
        //
        path = new Path();
        rectF = new RectF();

    }

    private void setAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RadiusImageView, defStyleAttr, 0);
        isCircle = array.getBoolean(R.styleable.RadiusImageView_is_circle, false);
        if (!isCircle) {
            int leftTop = array.getDimensionPixelSize(R.styleable.RadiusImageView_corner_left_radius_top, 0);
            int leftBto = array.getDimensionPixelSize(R.styleable.RadiusImageView_corner_left_radius_bto, 0);

            int rightTop = array.getDimensionPixelSize(R.styleable.RadiusImageView_corner_right_radius_top, 0);
            int rightBto = array.getDimensionPixelSize(R.styleable.RadiusImageView_corner_right_radius_bto, 0);

            int all = array.getDimensionPixelSize(R.styleable.RadiusImageView_corner_all_radius, 0);
            if (leftTop == 0 && leftBto == 0 && rightTop == 0 && rightBto == 0) {
                leftTop = all;
                leftBto = all;
                rightTop = all;
                rightBto = all;
            }
            radii = new float[]{leftTop, leftTop,   // 左上角X和Y半径
                    rightTop, rightTop,   // 右上角X和Y半径
                    rightBto, rightBto,   // 右下角X和Y半径
                    leftBto, leftBto    // 左下角X和Y半径
            };

        } else {
            setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rectF.set(0, 0, w, h);
    }


    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 绘制圆角背景（如果需要）
        // canvas.drawPath(path, paint);
        // 裁剪Canvas以应用圆角效果
        path.reset();
        if (isCircle) {
            //圆（一般是圆形头像）
            cornerRadius = getWidth() / 2;
            path.addRoundRect(rectF, cornerRadius, cornerRadius, Path.Direction.CW);
        } else {
            // 圆角
            path.addRoundRect(rectF, radii, Path.Direction.CW);
        }

        canvas.clipPath(path);
        super.onDraw(canvas);
    }

}
