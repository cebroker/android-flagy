package com.evercheck.flagly.featureFlag

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.evercheck.flagly.MainCoroutineRule
import com.evercheck.flagly.developeroptions.FeatureFlagHandlerViewModel
import com.evercheck.flagly.developeroptions.FeatureFlagValue
import com.evercheck.flagly.domain.usecase.GetFeatureFlagUseCase
import com.evercheck.flagly.domain.usecase.SetFeatureFlagUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private const val SEARCH_TEXT_TEST = "five"

@ExperimentalCoroutinesApi
class FeatureFlagHandlerViewModelTest {

    /*// Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule(testDispatcher)*/

    /*private val getFeatureFlagUseCase = mockk<GetFeatureFlagUseCase>()
    private val setFeatureFlagUseCase = mockk<SetFeatureFlagUseCase>()*/

    private lateinit var viewModel: FeatureFlagHandlerViewModel

    @Before
    fun setUp() {
        //viewModel = FeatureFlagHandlerViewModel(getFeatureFlagUseCase, setFeatureFlagUseCase)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `when getFeautereFlagUseCase is called then should modify liveData`() {
       /* val featureFlagValues = mockk<List<FeatureFlagValue>>(relaxed = true)
        every {
            getFeatureFlagUseCase(SEARCH_TEXT_TEST)
        } answers {
            arrayListOf()
        }

        viewModel.filterFeatureFlagsByName(SEARCH_TEXT_TEST)

        verifyAll { getFeatureFlagUseCase(SEARCH_TEXT_TEST) }
        val rpta1 = featureFlagValues
        val rpta2 = viewModel.featureFlagState.value?.featureFlagValues
        Assert.assertEquals(true, true)*/
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        //testDispatcher.cleanupTestCoroutines()
    }
}