package com.dmcapps.navigationfragment.manager.micromanagers.stack;

import com.dmcapps.navigationfragment.fragments.INavigationFragment;
import com.dmcapps.navigationfragment.manager.NavigationManagerFragment;
import com.dmcapps.navigationfragment.manager.micromanagers.ManagerConfig;
import com.dmcapps.navigationfragment.manager.micromanagers.ManagerState;

/**
 * Created by dcarmo on 2016-02-25.
 */
public interface IStackManager {

    void pushFragment(NavigationManagerFragment manager, ManagerState state, ManagerConfig config, INavigationFragment navFragment, int animIn, int animOut);

    void popFragment(NavigationManagerFragment manager, ManagerState state, ManagerConfig config, int animIn, int animOut);

    void clearNavigationStackToPosition(NavigationManagerFragment manager, ManagerState state, int stackPosition);

}