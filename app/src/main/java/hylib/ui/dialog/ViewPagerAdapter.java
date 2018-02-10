package hylib.ui.dialog;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class ViewPagerAdapter extends PagerAdapter {
	public View[] views;

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return views == null ? 0 : views.length;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup view, int position, Object object) {
//        super.destroyItem(container, position, object);
//        view.removeViewAt(position);
        view.removeView(views[position]);
        
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        view.addView(views[position]);
        return views[position];
    }
}