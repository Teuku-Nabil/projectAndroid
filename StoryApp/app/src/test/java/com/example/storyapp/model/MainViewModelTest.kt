package com.example.storyapp.model

import android.util.Log
import android.widget.Toast
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.storyapp.DataDummy
import com.example.storyapp.DataDummy.generateDummyToken
import com.example.storyapp.DiffCallback
import com.example.storyapp.MainDispatcherRule
import com.example.storyapp.dataapp.UserRepository
import com.example.storyapp.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: UserRepository
    private val token = generateDummyToken()

    @Test
    fun `when Get Story Should Not Null and Return Data`() = runTest {
        val dummyQuote = DataDummy.generateDummyQuoteResponse()
        val data: PagingData<StoryModel> = QuotePagingSource.snapshot(dummyQuote)
        //val data: PagingData<StoryModel> = PagingData.from(dummyQuote)
        val expectedQuote = MutableLiveData<PagingData<StoryModel>>()
        expectedQuote.value = data
        Mockito.`when`(storyRepository.getUserStoryList(token)).thenReturn(expectedQuote)

        val mainViewModel = MainViewModel(storyRepository)
        val actualQuote: PagingData<StoryModel> =
            mainViewModel.listUserStory(token).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = DiffCallback(),
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualQuote)

        assertNotNull(differ.snapshot())
        assertEquals(dummyQuote.size, differ.snapshot().size)
        assertEquals(dummyQuote[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Story Empty Should Return No Data`() = runTest {
        val data: PagingData<StoryModel> = PagingData.from(emptyList())
        val expectedStory = MutableLiveData<PagingData<StoryModel>>()
        expectedStory.value = data
        Mockito.`when`(storyRepository.getUserStoryList(token)).thenReturn(expectedStory)

        val mainViewModel = MainViewModel(storyRepository)
        val actualStory: PagingData<StoryModel> =
            mainViewModel.listUserStory(token).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = DiffCallback(),
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        assertEquals(0, differ.snapshot().size)
    }
}

class QuotePagingSource : PagingSource<Int, LiveData<List<StoryModel>>>() {
    companion object {
        fun snapshot(items: List<StoryModel>): PagingData<StoryModel> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryModel>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryModel>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}