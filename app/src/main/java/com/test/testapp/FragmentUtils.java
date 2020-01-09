package com.test.testapp;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;


/**
 * @author Fallwater潘建波 on 2018/4/10
 * @mail 1667376033@qq.com
 * 功能描述:
 */
public class FragmentUtils {

    private static final String TAG = "FragmentUtils";

    public static void showFragmentWithOutAnimation(FragmentManager manager, Fragment fragment,
            boolean addToBackStack) {
        showFragment(manager, fragment, addToBackStack, R.id.flContainer, false);
    }

    public static void showFragmentWithOutAnimation(FragmentManager manager, Fragment fragment, @IdRes int resId,
            boolean addToBackStack) {
        showFragment(manager, fragment, addToBackStack, resId, false);
    }

    public static void showFragmentWithOutAnimation(FragmentManager manager, Fragment fragment,
            boolean addToBackStack, boolean commitStateLoss) {
        showFragment(manager, fragment, addToBackStack, R.id.flContainer, commitStateLoss);
    }

    public static void showFragmentWithCustomAnimation(FragmentManager manager, Fragment fragment,
            boolean addToBackStack, int... animations) {
        showFragment(manager, fragment, addToBackStack, R.id.flContainer, false, animations);
    }

    /**
     * @param manager        fragment的事物管理者
     * @param fragment       切换的fragment
     * @param addToBackStack 是否加入回退栈
     * @param resId          fragment的 container
     * @param animations     fragment切换动画(请按照此顺序添加动画:enter->exit->popEnter->popExit)
     */
    private static void showFragment(FragmentManager manager, Fragment fragment,
            boolean addToBackStack, int resId, boolean commitStateLoss, int... animations) {
        if (manager == null) {
            return;
        }
        FragmentTransaction ft = manager.beginTransaction();
        if (animations.length > 0) {
            ft.setCustomAnimations(animations[0], animations[1], animations[2], animations[3]);
        }
        String fragmentName = fragment.getClass().getName();
        Log.d("showFragment", "fragmentName = " + fragmentName);
        ft.replace(resId, fragment, fragmentName);
        if (addToBackStack) {
            ft.addToBackStack(fragmentName);
        }

        try {
            if (commitStateLoss) {
                ft.commitAllowingStateLoss();
            } else {
                ft.commit();
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception:" + e.getMessage());
        }

    }

    public static void addFragmentWithOutAnimation(FragmentManager manager, Fragment fragment,
            boolean addToBackStack) {
        addFragment(manager, fragment, addToBackStack, R.id.flContainer, false);
    }

    public static void addFragmentWithCustomAnimation(FragmentManager manager, Fragment fragment,
            boolean addToBackStack, int... animations) {
        addFragment(manager, fragment, addToBackStack, R.id.flContainer, false, animations);
    }

    public static void addFragmentWithCustomAnimation(FragmentManager manager, Fragment fragment,
            boolean addToBackStack, boolean commitStateLoss) {
        addFragment(manager, fragment, addToBackStack, R.id.flContainer, commitStateLoss);
    }

    private static void addFragment(FragmentManager manager, Fragment fragment,
            boolean addToBackStack, int resId, boolean commitStateLoss, int... animations) {
        FragmentTransaction ft = manager.beginTransaction();
        if (animations.length > 0) {
            ft.setCustomAnimations(animations[0], animations[1], animations[2], animations[3]);
        }
        String fragmentName = fragment.getClass().getName();
        ft.add(resId, fragment, fragmentName);
        if (addToBackStack) {
            ft.addToBackStack(fragmentName);
        }
        try {
            if (commitStateLoss) {
                ft.commitAllowingStateLoss();
            } else {
                ft.commit();
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception:" + e.getMessage());
        }
    }

    public static Fragment getCurrentFragment(FragmentManager manager) {
        return manager.findFragmentById(R.id.flContainer);
    }

    public static String getCurrentFragmentTag(FragmentManager manager) {
        int backStackEntryCount = manager.getBackStackEntryCount();
        if (backStackEntryCount <= 0) {
            return null;
        }
        String fragmentTag = manager.getBackStackEntryAt(
                backStackEntryCount - 1).getName();
        return fragmentTag;
    }

    /**
     * 清除某个Fragment以及其以上的所有Fragment(这里需要注意的是会清除fragmentClass本身)
     *
     * @param fragmentClass 某个fragment class
     */
    public static void clearFragment(FragmentManager manager, Class fragmentClass) {
        int num = manager.getBackStackEntryCount();
        for (int i = 0; i < num; i++) {
            FragmentManager.BackStackEntry stackEntry = manager.getBackStackEntryAt(i);
            if (stackEntry.getName().equals(fragmentClass.getName())) {
                manager.popBackStack(stackEntry.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
            }
        }
    }

    /**
     * 清除fragment栈除了最初的其他所有fragment
     */
    public static void clearFragmentAll(FragmentManager manager) {
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public static boolean containFragment(FragmentManager manager, Class fragmentClass) {
        int num = manager.getBackStackEntryCount();
        for (int i = 0; i < num; i++) {
            FragmentManager.BackStackEntry stackEntry = manager.getBackStackEntryAt(i);
            if (stackEntry.getName().equals(fragmentClass.getName())) {
                return true;
            }
        }
        return false;
    }

    public interface OnBackPressedListener {

        boolean onBackPressed();
    }
}
