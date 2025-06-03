package com.codingclemo.weatherfinder.ui

import android.Manifest
import android.os.Build
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.codingclemo.weatherfinder.MainActivity
import com.codingclemo.weatherfinder.data.local.WeatherDatabase
import com.codingclemo.weatherfinder.data.repository.LocationsRepository
import com.codingclemo.weatherfinder.data.repository.WeatherRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject


@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class WeatherForecastTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @get:Rule(order = 2)
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.INTERNET,
        Manifest.permission.ACCESS_NETWORK_STATE
    )

    private val testDispatcher = StandardTestDispatcher()

    @Inject
    lateinit var locationsRepo: LocationsRepository

    @Inject
    lateinit var weatherRepo: WeatherRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        hiltRule.inject()

        runBlocking {
            WeatherDatabase.initialLocations.forEach { locationsRepo.addLocation(it) }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            composeTestRule.mainClock.autoAdvance = false
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun verifyLocationListIsVisible() {
        val activity = InstrumentationRegistry.getInstrumentation().startActivitySync(
            InstrumentationRegistry.getInstrumentation().targetContext.packageManager
                .getLaunchIntentForPackage(InstrumentationRegistry.getInstrumentation().targetContext.packageName)
        ) as MainActivity

        WeatherForecastRobot(composeTestRule)
            .verifyLocationListIsVisible()
    }
}

class WeatherForecastRobot(
    private val composeTestRule: ComposeTestRule
) {
    fun verifyLocationListIsVisible(): WeatherForecastRobot {
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Locations").fetchSemanticsNodes().isNotEmpty()
        }
        return this
    }
} 