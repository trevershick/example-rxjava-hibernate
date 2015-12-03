package io.shick;

import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.criterion.Criterion;

import rx.Observable;
import rx.Subscriber;

public class QueryStreamer<T> {

	private final SessionFactory factory;
	private final int fetchSize;
	private final Class<T> clazz;
	
	public QueryStreamer(SessionFactory factory, Class<T> what) {
		this(factory, what, 20);
	}
	
	public QueryStreamer(SessionFactory factory, Class<T> what, int fetchSize) {
		this.factory = factory;
		this.fetchSize = fetchSize;
		this.clazz = what;
	}
	
    public Observable<T> stream(Criterion criteria) {
    	final StatelessSession session = factory.openStatelessSession();
    	
    	return Observable.create(new Observable.OnSubscribe<T>() {
	        @Override
	        public void call(final Subscriber<? super T> subscriber) {
	          try {
	              subscriber.onStart();
	              ScrollableResults results = session.createCriteria(clazz)
	            		  .add(criteria)
	            		  .setReadOnly(true)
	            		  .setFetchSize(fetchSize)
	            		  .scroll(ScrollMode.FORWARD_ONLY);
	              while (results.next() && !subscriber.isUnsubscribed()) {
	            	  T obj = clazz.cast(results.get(0));
	            	  subscriber.onNext(obj);
	              }
	              if (!subscriber.isUnsubscribed()) {
	                subscriber.onCompleted();
	              }
	              results.close();
	          }
	          catch (Exception e) {
	            subscriber.onError(e);
	          } finally {
	        	  session.close();
	          }
	        }
	      });
    }
}
