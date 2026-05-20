package com.afkanerd.smswithoutborders_libsmsmms

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.getNativesLoaded
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.setNativesLoaded
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.isDefault
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.isSecondaryUser
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.getCurrentLocale
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ContextExtensionsTest {

    private lateinit var context: android.content.Context

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun setNativesLoadedTrueAndReadBack() {
        context.setNativesLoaded(true)
        val result = context.getNativesLoaded()
        assertTrue(result)
    }

    @Test
    fun setNativesLoadedFalseAndReadBack() {
        context.setNativesLoaded(false)
        val result = context.getNativesLoaded()
        assertFalse(result)
    }

    @Test
    fun getNativesLoadedDefaultIsFalse() {
        context.setNativesLoaded(false)
        val result = context.getNativesLoaded()
        assertFalse(result)
    }

    @Test
    fun isDefaultReturnsBooleanWithoutCrashing() {
        val result = context.isDefault()
        assertNotNull(result)
    }

    @Test
    fun isSecondaryUserReturnsBooleanWithoutCrashing() {
        val result = context.isSecondaryUser()
        assertNotNull(result)
    }

    @Test
    fun getCurrentLocaleIsNotNull() {
        val result = context.getCurrentLocale()
        assertNotNull(result)
    }

    @Test
    fun getCurrentLocaleHasLanguage() {
        val result = context.getCurrentLocale()
        assertNotNull(result?.language)
    }
}