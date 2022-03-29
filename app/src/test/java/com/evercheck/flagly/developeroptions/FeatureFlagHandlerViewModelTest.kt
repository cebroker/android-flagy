package com.evercheck.flagly.developeroptions

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.evercheck.flagly.domain.model.FeatureFlag
import com.evercheck.flagly.domain.usecase.GetFeatureFlagUseCase
import com.evercheck.flagly.domain.usecase.SetFeatureFlagUseCase
import com.evercheck.flagly.utils.CoroutineContextProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Random

@ExperimentalCoroutinesApi
class FeatureFlagHandlerViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val getFeatureFlagUseCase = mockk<GetFeatureFlagUseCase>(relaxed = true)
    private val setFeatureFlagUseCase = mockk<SetFeatureFlagUseCase>(relaxed = true)
    private val flags = listOf<FeatureFlag>(mockk(), mockk(), mockk(), mockk(), mockk())

    private lateinit var viewModel: FeatureFlagHandlerViewModel

    init {
        flags.forEachIndexed { index, it ->
            every {
                it.name
            } answers {
                "Flag$index"
            }
        }
    }

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        val coroutineContextProvider = object : CoroutineContextProvider {
            override val io: CoroutineDispatcher
                get() = testDispatcher
        }

        viewModel = FeatureFlagHandlerViewModel(
            getFeatureFlagUseCase,
            setFeatureFlagUseCase,
            coroutineContextProvider
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given a list of feature flags when a random name then filter the list`() = runTest {
        val values = flags.map { FeatureFlagValue(it) }.toMutableList()
        values[0] = FeatureFlagValue(values[0].featureFlag, true)

        every {
            getFeatureFlagUseCase(QUERY_SEARCH_TEST)
        } answers {
            values
        }

        viewModel.filterFeatureFlagsByName(QUERY_SEARCH_TEST)

        verify(exactly = 1) {
            getFeatureFlagUseCase(QUERY_SEARCH_TEST)
        }

        Assert.assertEquals(values, viewModel.featureFlagState.value?.featureFlagValues)
    }

    @Test
    fun `given a feature flag when the override is true then call invoke`() = runTest {
        val flag = mockk<FeatureFlag>()

        viewModel.onFeatureFlagValueChanged(flag, true)

        verify(exactly = 1) {
            setFeatureFlagUseCase(flag, true)
        }
    }

    @Test
    fun `given a feature flag, override true and remote false when the invoke is called then the update and get invokes are executed`() = runTest {
        val flag = mockk<FeatureFlag>()

        viewModel.onOverrideValueChange(flag, override = true, remoteValue = false)

        verify(exactly = 1) {
            setFeatureFlagUseCase(flag, override = true, remoteValue = false)
            getFeatureFlagUseCase()
        }
    }
}
private const val QUERY_SEARCH_TEST = "fl"