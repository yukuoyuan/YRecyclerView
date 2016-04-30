package yuan.kuo.yu.load;

import android.animation.Animator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import java.util.List;

/**
 * Created by yukuo on 2016/4/29.
 */
public abstract class BaseProgressController {


    private View mTarget;

    private List<Animator> mAnimators;


    public void setTarget(View target) {
        this.mTarget = target;
    }

    public View getTarget() {
        return mTarget;
    }


    public int getWidth() {
        return mTarget.getWidth();
    }

    public int getHeight() {
        return mTarget.getHeight();
    }

    public void postInvalidate() {
        mTarget.postInvalidate();
    }

    /**
     * 绘制
     *
     * @param canvas 画布
     * @param paint  画笔
     */
    public abstract void draw(Canvas canvas, Paint paint);

    /**
     * 创建一个动画或者动画集合
     */
    public abstract List<Animator> createAnimation();

    public void initAnimation() {
        mAnimators = createAnimation();
    }

    /**
     * make animation to start or end when target
     * view was be Visible or Gone or Invisible.
     * make animation to cancel when target view
     * be onDetachedFromWindow.
     *
     * @param animStatus
     */
    public void setAnimationStatus(AnimStatus animStatus) {
        if (mAnimators == null) {
            return;
        }
        int count = mAnimators.size();
        for (int i = 0; i < count; i++) {
            Animator animator = mAnimators.get(i);
            boolean isRunning = animator.isRunning();
            switch (animStatus) {
                case START:
                    if (!isRunning) {
                        animator.start();
                    }
                    break;
                case END:
                    if (isRunning) {
                        animator.end();
                    }
                    break;
                case CANCEL:
                    if (isRunning) {
                        animator.cancel();
                    }
                    break;
            }
        }
    }


    public enum AnimStatus {
        START, END, CANCEL
    }


}
