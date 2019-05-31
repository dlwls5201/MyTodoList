package com.tistory.blackjin.mytodolist

import com.tistory.blackjin.mytodolist.ui.MainContract
import com.tistory.blackjin.mytodolist.ui.MainPresenter
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InOrder
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class MainPresenterTest {

    @Mock
    lateinit var view: MainContract.View

    @Mock
    lateinit var repository: MainContract.Repository

    private lateinit var presenter: MainPresenter

    private lateinit var inOrder: InOrder

    @Before
    fun setUp() {

        //presenter = MainPresenter(view, repository)

        inOrder = Mockito.inOrder(view, repository)
    }

    @Test
    fun test_onClickInsert() {


    }
}