package hylib.toolkits;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;

public class temp {

//	 public <T> Class<T> getGenericType(T instance, int index) {
//	 	Type genType = instance.getClass().getGenericSuperclass();
//	 	if (!(genType instanceof ParameterizedType)) return null;
//	  
//	 	Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
//	 	if (index >= params.length || index < 0) throw new RuntimeException("Index outof bounds");
//	 	if (!(params[index] instanceof Class)) return null;
//	  
//	 	return (Class) params[index];
//	 }

//	private <T extends View> void getGroupViews(List<T> list, ViewGroup viewGroup, Class<T> type) {
//		if (viewGroup == null) return;
//		int count = viewGroup.getChildCount();
//		for (int i = 0; i < count; i++) {
//			View view = viewGroup.getChildAt(i);
//			if (view instanceof getGenericType(T, 0)) {
//				list.add(view);
//			} else if (view instanceof ViewGroup) {
//				// 若是布局控件（LinearLayout或RelativeLayout）,继续查询子View
//				getGroupViews(list, (ViewGroup)list, view, T.class);
//			}
//	}
}
