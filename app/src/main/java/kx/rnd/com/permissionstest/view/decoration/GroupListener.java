package kx.rnd.com.permissionstest.view.decoration;

import android.view.View;

public interface GroupListener{
    String getGroupName(int position);
    View getGroupView(int position);
}