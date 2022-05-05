package com.digital_enabling.android_aries_sdk.common

import io.reactivex.rxjava3.core.Observable

/**
 * Event Aggregator
 */
interface IEventAggregator {
    /**
     * Gets an observable for an event type.
     */
    fun <TEvent : Any> getEventByType(): Observable<TEvent>

    /**
     * Publishes a particular event.
     * @param eventToPublish Event to publish
     */
    fun <TEvent : Any> publish(eventToPublish: TEvent)
}