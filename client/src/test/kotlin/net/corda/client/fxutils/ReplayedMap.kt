package net.corda.client.fxutils

import javafx.collections.MapChangeListener
import javafx.collections.ObservableMap
import kotlin.test.assertEquals

/**
 * [ReplayedMap] simply replays changes done to the source map. Used for testing changes.
 */
class ReplayedMap<K, A>(sourceMap: ObservableMap<K, A>) : ReadOnlyBackedObservableMapBase<K, A, Unit>() {
    init {
        sourceMap.forEach {
            backingMap.set(it.key, Pair(it.value, Unit))
        }
        sourceMap.addListener { change: MapChangeListener.Change<out K, out A> ->
            if (change.wasRemoved()) {
                assertEquals(backingMap.remove(change.key)!!.first, change.valueRemoved)
            }
            if (change.wasAdded()) {
                backingMap.set(change.key, Pair(change.valueAdded, Unit))
            }
            fireChange(change)
        }
    }
}
