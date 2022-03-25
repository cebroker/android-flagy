package com.evercheck.flagly.developeroptions

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.evercheck.flagly.MainCoroutineRule
import com.evercheck.flagly.domain.model.FeatureFlag
import com.evercheck.flagly.domain.usecase.GetFeatureFlagUseCase
import com.evercheck.flagly.domain.usecase.SetFeatureFlagUseCase
import com.evercheck.flagly.utils.CoroutineContextProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.Random
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FeatureFlagHandlerViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule(testDispatcher)

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val getFeatureFlagUseCase = mockk<GetFeatureFlagUseCase>(relaxed = true)
    private val setFeatureFlagUseCase = mockk<SetFeatureFlagUseCase>(relaxed = true)

    private lateinit var viewModel: FeatureFlagHandlerViewModel

    private val characters = ('a'..'z') + ('A'..'Z')
    private fun generateFeatureFlag(): List<FeatureFlag> = (0..Random().nextInt(6)).map {
        characters.shuffled().get(it).toString()
    }.toSet()
        .map {
            object : FeatureFlag {
                override val name: String
                    get() = it
            }
        }

    @Before
    fun setup() {
        val coroutineContextProvider = object : CoroutineContextProvider {
            override val mainDispatcher: CoroutineDispatcher
                get() = testDispatcher
            override val backgroundDispatcher: CoroutineDispatcher
                get() = testDispatcher
        }

        viewModel = FeatureFlagHandlerViewModel(
            getFeatureFlagUseCase,
            setFeatureFlagUseCase,
            coroutineContextProvider
        )
    }

    @Test
    fun filterFeatureFlagsByName() {
        val featureFlagsValues = generateFeatureFlag()
        val numberRandom = Random().nextInt(featureFlagsValues.size)
        val featureFlagRandom = featureFlagsValues[numberRandom]
        val featureFlagsFilter = featureFlagsValues.filter {
            it.name.contains(
                featureFlagRandom.name,
                true
            )
        }.map {
            FeatureFlagValue(it)
        }

        every {
            getFeatureFlagUseCase(featureFlagRandom.name)
        } answers {
            featureFlagsFilter
        }

        viewModel.filterFeatureFlagsByName(featureFlagRandom.name)

        verify(exactly = 1) {
            getFeatureFlagUseCase(featureFlagRandom.name)
        }

        Assert.assertEquals(featureFlagsFilter, viewModel.featureFlagState.value?.featureFlagValues)
    }

    @Test
    fun onFeatureFlagValueChanged() {
        val flag = mockk<FeatureFlag>()

        viewModel.onFeatureFlagValueChanged(flag, true)

        verify(exactly = 1) {
            setFeatureFlagUseCase(flag, true)
        }
    }

    @Test
    fun onOverrideValueChange() {
        val flag = mockk<FeatureFlag>()

        viewModel.onOverrideValueChange(flag, override = true, remoteValue = false)

        verify(exactly = 1) {
            setFeatureFlagUseCase(flag, override = true, remoteValue = false)
        }
    }
}