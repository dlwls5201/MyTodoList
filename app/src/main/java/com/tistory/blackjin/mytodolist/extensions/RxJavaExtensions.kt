package com.tistory.blackjin.mytodolist.extensions

import io.reactivex.Completable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

fun runOnIoScheduler(func: () -> Unit): Disposable
        = Completable.fromCallable(func).subscribeOn(Schedulers.io()).subscribe()