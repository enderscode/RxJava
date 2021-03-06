/**
 * Copyright 2014 Netflix, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rx.observables;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * An {@link Observable} that has been grouped by key, the value of which can be obtained with
 * {@link #getKey()}.
 * <p>
 * <em>Note:</em> A {@link GroupedObservable} will cache the items it is to emit until such time as it
 * is subscribed to. For this reason, in order to avoid memory leaks, you should not simply ignore those
 * {@code GroupedObservable}s that do not concern you. Instead, you can signal to them that they may
 * discard their buffers by applying an operator like {@link Observable#take}{@code (0)} to them.
 * 
 * @param <K>
 *            the type of the key
 * @param <T>
 *            the type of the items emitted by the {@code GroupedObservable}
 * @see Observable#groupBy(Func1)
 * @see <a href="https://github.com/Netflix/RxJava/wiki/Transforming-Observables#groupby-and-groupbyuntil">RxJava Wiki: groupBy() and groupByUntil()</a>
 */
public class GroupedObservable<K, T> extends Observable<T> {
    private final K key;

    /**
     * Converts an {@link Observable} into a {@code GroupedObservable} with a particular key.
     *
     * @param key
     *          the key to identify the group of items emitted by this {@code GroupedObservable}
     * @param o
     *          the {@link Observable} to convert
     * @return a {@code GroupedObservable} representation of {@code o}, with key {@code key}
     * @deprecated Use Observable.groupBy with element selector instead. 
     */
    @Deprecated
    public static <K, T> GroupedObservable<K, T> from(K key, final Observable<T> o) {
        return new GroupedObservable<K, T>(key, new OnSubscribe<T>() {

            @Override
            public void call(Subscriber<? super T> s) {
                o.unsafeSubscribe(s);
            }
        });
    }

    public GroupedObservable(K key, OnSubscribe<T> onSubscribe) {
        super(onSubscribe);
        this.key = key;
    }

    /**
     * Returns the key that identifies the group of items emited by this {@code GroupedObservable}
     * 
     * @return the key that the items emitted by this {@code GroupedObservable} were grouped by
     */
    public K getKey() {
        return key;
    }
}
