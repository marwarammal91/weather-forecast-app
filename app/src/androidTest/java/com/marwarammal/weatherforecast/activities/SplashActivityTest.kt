// package com.marwarammal.weatherforecast.activities
//
// import android.Manifest
// import android.app.Activity
// import android.content.Intent
// import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
// import androidx.test.rule.ActivityTestRule
// import androidx.test.rule.GrantPermissionRule
// import com.marwarammal.weatherforecast.utils.PermissionUtils
// import org.junit.Before
// import org.junit.Rule
// import org.junit.Test
// import org.junit.runner.RunWith
//
// @RunWith(AndroidJUnit4ClassRunner::class)
// class SplashActivityTest {
//
//    lateinit var activity: Activity
//
//    @get:Rule
//    var mActivityTestRule = ActivityTestRule(SplashActivity::class.java)
//
//    @get:Rule
//    var mRuntimePermissionRule =
//        GrantPermissionRule.grant(Manifest.permission.ACCESS_COARSE_LOCATION)
//
//    @Before
//    fun setup() {
//        val intent = Intent(Intent.ACTION_MAIN)
//        mActivityTestRule.launchActivity(intent)
//    }
//
//    @Test
//    fun navigateToCitiesActivityTest() {
//        val intent = Intent(mActivityTestRule.activity, MainActivity::class.java)
//        mActivityTestRule.launchActivity(intent)
//    }
//
//    @Test
//    fun checkPermissionTest() {
//        activity = mActivityTestRule.activity
//        assert(
//            PermissionUtils.isPermissionGranted(
//                activity,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            )
//        )
//    }
// }
