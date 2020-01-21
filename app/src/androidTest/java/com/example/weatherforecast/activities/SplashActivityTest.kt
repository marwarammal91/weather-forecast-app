package com.example.weatherforecast.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import com.example.weatherforecast.utils.PermissionUtils
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.nio.charset.StandardCharsets


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
        assert(PermissionUtils.isPermissionGranted(activity, Manifest.permission.ACCESS_COARSE_LOCATION))
    }

    @Before
    fun setup() {
        val intent = Intent(Intent.ACTION_PICK)
        mActivityTestRule.launchActivity(intent)
    }

    @Test
    fun navigateToCitiesActivityTest() {
        val intent = Intent(mActivityTestRule.activity, MainActivity::class.java)
        mActivityTestRule.launchActivity(intent)
    }

    @Test
    fun readJSONFromAssetTest() {
        val `is` = mActivityTestRule.activity.assets.open("city.list.json")
        val size = `is`.available()
        val buffer = ByteArray(size)
        `is`.read(buffer)
        `is`.close()
        String(buffer, StandardCharsets.UTF_8)

    }
}