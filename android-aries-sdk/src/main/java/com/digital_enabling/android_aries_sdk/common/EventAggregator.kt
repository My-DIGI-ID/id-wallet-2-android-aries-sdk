package com.digital_enabling.android_aries_sdk.common

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.ofType
import io.reactivex.rxjava3.subjects.PublishSubject

class EventAggregator : IEventAggregator {
    private val subject: PublishSubject<Any> = PublishSubject.create()

    /**
     * @see IEventAggregator
     */
    override fun <TEvent : Any> getEventByType(): Observable<TEvent> {
        return subject.ofType<Any>() as Observable<TEvent>
    }

    /**
     * @see IEventAggregator
     */
    override fun <TEvent : Any> publish(eventToPublish: TEvent) {
        subject.onNext(eventToPublish)
    }
}