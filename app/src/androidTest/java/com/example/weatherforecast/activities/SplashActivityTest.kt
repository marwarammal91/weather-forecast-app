package com.example.weatherforecast.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import com.example.weatherforecast.utils.PermissionUtils
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SplashActivityTest {

    lateinit var activity: Activity

    @get:Rule
    var mActivityTestRule = ActivityTestRule(SplashActivity::class.java)

    @get:Rule
    var mRuntimePermissionRule =
        GrantPermissionRule.grant(Manifest.permission.ACCESS_COARSE_LOCATION)

    @Test
    fun checkPermissionTest() {
        activity = mActivityTestRule.activity
        assert(
            PermissionUtils.isPermissionGranted(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    @Before
    fun setup() {
        val intent = Intent(Intent.ACTION_MAIN)
        mActivityTestRule.launchActivity(intent)
    }

    @Test
    fun navigateToCitiesActivityTest() {
        val intent = Intent(mActivityTestRule.activity, MainActivity::class.java)
        mActivityTestRule.launchActivity(intent)
    }
}
