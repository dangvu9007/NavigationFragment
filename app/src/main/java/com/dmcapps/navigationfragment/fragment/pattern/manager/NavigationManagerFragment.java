package com.dmcapps.navigationfragment.fragment.pattern.manager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.dmcapps.navigationfragment.R;
import com.dmcapps.navigationfragment.fragment.pattern.NavigationFragment;
import com.dmcapps.navigationfragment.fragment.pattern.helper.RetainedChildFragmentManagerFragment;

import java.util.Stack;

/**
 * This Fragment manages the stack of single navigation on fragments.
 * The class allows for easy adding and removing of fragments as the user
 * traverses the screens. A self-contained class that requires no resources
 * in order to function. Each time a new manager is made a separate stack will be created
 * and no overlap will occur in the class.
 */
public class NavigationManagerFragment extends RetainedChildFragmentManagerFragment implements INavigationManager {

    private static int mUniqueViewIdGenerator;
    private int mViewId;

    private FrameLayout mFragmentFrame;
    private Stack<String> mFragmentTags;
    private NavigationFragment mRootFragment;

    // TODO: Instatiate with the rootFragment?
    public static NavigationManagerFragment newInstance() {
        return new NavigationManagerFragment();
    }

    public NavigationManagerFragment() {
        mViewId = ++mUniqueViewIdGenerator;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mFragmentFrame == null) {
            mFragmentFrame = new FrameLayout(getActivity());
            mFragmentFrame.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mFragmentFrame.setId(mViewId);
        }
        return mFragmentFrame;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getFragmentTags().size() == 0) {
            pushFragment(mRootFragment);
        }
        else {
            // TODO: attach top fragment?
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        // TODO: detach top fragment?
    }

    public void setRootFragment(NavigationFragment rootFragment) {
        mRootFragment = rootFragment;
    }

    public void pushFragment(NavigationFragment navFragment) {
        pushFragment(navFragment, R.anim.slide_in_from_right, R.anim.slide_out_to_left);
    }

    @Override
    public void pushFragment(NavigationFragment navFragment, int animationIn, int animationOut) {
        navFragment.setNavigationManager(this);
        FragmentManager childFragManager = getRetainedChildFragmentManager();
        FragmentTransaction childFragTrans = childFragManager.beginTransaction();

        // TODO: Better way to do this?
        if (getFragmentTags().size() > 0) {
            childFragTrans.setCustomAnimations(animationIn, animationOut);
            Fragment topFrag = childFragManager.findFragmentByTag(getFragmentTags().peek());

            // Detach the top fragment such that it is kept in the stack and can be shown again without lose of state.
            if (topFrag != null) {
                childFragTrans.detach(topFrag);
            }
        }

        // Add in the new fragment that we are presenting and add it's navigation tag to the stack.
        childFragTrans.add(mFragmentFrame.getId(), navFragment, navFragment.getNavTag());
        addFragmentToStack(navFragment);

        childFragTrans.commit();

    }

    public void popFragment() {
        popFragment(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

    public void popFragment(int animationIn, int animationOut) {
        if (getFragmentTags().size() > 1) {
            FragmentManager childFragManager = getRetainedChildFragmentManager();
            FragmentTransaction childFragTrans = childFragManager.beginTransaction();
            childFragTrans.setCustomAnimations(animationIn, animationOut);
            childFragTrans.remove(childFragManager.findFragmentByTag(getFragmentTags().pop()));
            childFragTrans.attach(childFragManager.findFragmentByTag(getFragmentTags().peek()));
            childFragTrans.commit();
        }
        else {
            // TODO: Nothing to dismiss ... Exception? Call activity onBackPressed()? what to do?
            // TODO: Dismiss root and self?
        }
    }

    @Override
    public NavigationFragment topFragment() {
        return (NavigationFragment)getRetainedChildFragmentManager().findFragmentByTag(getFragmentTags().peek());
    }

    public boolean onBackPressed() {
        if (getFragmentTags().size() > 1) {
            popFragment();
            return true;
        }

        return false;
    }

    private void addFragmentToStack(NavigationFragment navFragment) {
        getFragmentTags().add(navFragment.getNavTag());
    }

    private Stack<String> getFragmentTags() {
        if (mFragmentTags == null) {
            mFragmentTags = new Stack<>();
        }
        return mFragmentTags;
    }
}
