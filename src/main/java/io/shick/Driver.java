package io.shick;

import java.util.stream.IntStream;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

public class Driver {
	public static void main(String[] args) {
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		
		Session session = sessionFactory.openSession();
		IntStream.range(0, 100).forEach(i -> session.save(new Pet("Pet " + i)));
		session.close();
		
		QueryStreamer<Pet> petStreamer = new QueryStreamer<>(sessionFactory, Pet.class);
		
		System.out.println("Collect All ----------------------");
		petStreamer.stream(Restrictions.isNotNull("name"))
			.toList()
			.forEach(System.out::println);

		System.out.println("Take 5 ---------------------------");
		petStreamer.stream(Restrictions.isNotNull("name"))
			.take(5)
			.forEach(System.out::println);
		System.out.flush();
		sessionFactory.close();
	}
}
