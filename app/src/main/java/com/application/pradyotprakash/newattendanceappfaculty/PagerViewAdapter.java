package com.application.pradyotprakash.newattendanceappfaculty;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by pradyotprakash on 07/03/18.
 */
class PagerViewAdapter extends FragmentPagerAdapter {

    public PagerViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                String classValueMonday = FacultySubjectTeacherDetails.getClassValue();
                Bundle bundleMonday = new Bundle();
                bundleMonday.putString("class", classValueMonday);
                TimetableMondayFragment mondayFragment = new TimetableMondayFragment();
                mondayFragment.setArguments(bundleMonday);
                return mondayFragment;
            case 1:
                String classValueTuesday = FacultySubjectTeacherDetails.getClassValue();
                Bundle bundleTuesday = new Bundle();
                bundleTuesday.putString("class", classValueTuesday);
                TimetableTuesdayFragment tuesdayFragment = new TimetableTuesdayFragment();
                tuesdayFragment.setArguments(bundleTuesday);
                return tuesdayFragment;
            case 2:
                String classValueWednesday = FacultySubjectTeacherDetails.getClassValue();
                Bundle bundleWednesday = new Bundle();
                bundleWednesday.putString("class", classValueWednesday);
                TimetableWednesdayFragment wednesdayFragment = new TimetableWednesdayFragment();
                wednesdayFragment.setArguments(bundleWednesday);
                return wednesdayFragment;
            case 3:
                String classValueThursday = FacultySubjectTeacherDetails.getClassValue();
                Bundle bundleThursday = new Bundle();
                bundleThursday.putString("class", classValueThursday);
                TimetableThursdayFragment thursdayFragment = new TimetableThursdayFragment();
                thursdayFragment.setArguments(bundleThursday);
                return thursdayFragment;
            case 4:
                String classValueFriday = FacultySubjectTeacherDetails.getClassValue();
                Bundle bundleFriday = new Bundle();
                bundleFriday.putString("class", classValueFriday);
                TimetableFridayFragment fridayFragment = new TimetableFridayFragment();
                fridayFragment.setArguments(bundleFriday);
                return fridayFragment;
            case 5:
                String classValueSaturday = FacultySubjectTeacherDetails.getClassValue();
                Bundle bundleSaturday = new Bundle();
                bundleSaturday.putString("class", classValueSaturday);
                TimetableSaturdayFragment saturdayFragment = new TimetableSaturdayFragment();
                saturdayFragment.setArguments(bundleSaturday);
                return saturdayFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 6;
    }
}

