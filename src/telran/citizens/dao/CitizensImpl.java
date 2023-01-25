package telran.citizens.dao;
import java.util.Comparator;
import java.util.List;

import java.util.TreeSet;

import telran.citizens.interfaces.Citizens;
import telran.citizens.model.Person;

public class CitizensImpl implements Citizens {
	private TreeSet<Person> idList;
	private TreeSet<Person> lastNameList;
	private TreeSet<Person> ageList;
	private static Comparator<Person> lastNameComparator;
	private static Comparator<Person> ageComparator;
	static {
		lastNameComparator = (p1, p2) -> {
			int res = p1.getLastName().compareTo(p2.getLastName());
			return res != 0 ? res : p1.compareTo(p2);
		};
		ageComparator = (p1, p2) -> {
			int res = Integer.compare(p1.getAge(), p2.getAge());
			return res != 0 ? res : p1.compareTo(p2);
		};
	}

	public CitizensImpl() {
		idList = new TreeSet<>();
		ageList = new TreeSet<>(ageComparator);
		lastNameList = new TreeSet<>(lastNameComparator);
	}

	public CitizensImpl(List<Person> citizens) {
		this();
		for (Person person : citizens) {
			add(person);
		}
	}

	@Override
	// Было O(n) -> Стало -> O(1)
	public boolean add(Person person) {
		if (person == null) {
			return false;
		}
		if(idList.add(person) == false) {
		return false;
		}
		ageList.add(person);
		lastNameList.add(person);
		return true;
	}

	@Override
	// Было O(n) -> Стало - O(log(n)) из за find(id)
	public boolean remove(int id) {
		Person victim = find(id);
		if (victim == null) {
			return false;
		}
		idList.remove(victim);
		ageList.remove(victim);
		lastNameList.remove(victim);
		return true;
	}

	@Override
	// O(log(n)) осталось так же
	public Person find(int id) {
		Person pattern = new Person(id, "", "", 0);
		Person temp = idList.ceiling(pattern);
		if (temp != null && temp.getId() == id) {
			return temp;
		}
		return null;
	}

	@Override
	// O(log(n)) осталось так же
	public Iterable<Person> find(int minAge, int maxAge) {
		Person min = new Person(Integer.MIN_VALUE, null, null, minAge);
		Person max = new Person(Integer.MAX_VALUE, null, null, maxAge);

		return ageList.headSet(max).tailSet(min);
		
	}

	@Override
	// O(log(n)) осталось так же
	public Iterable<Person> find(String lastName) {
		Person min = new Person(Integer.MIN_VALUE, null, lastName, 0);
		Person max = new Person(Integer.MAX_VALUE, null, lastName, 0);

		return lastNameList.headSet(max).tailSet(min);
	}

	@Override
	// O(1) осталось так же
	public Iterable<Person> getAllPersonSortedById() {
		return idList;
	}

	@Override
	// O(1) осталось так же
	public Iterable<Person> getAllPersonSortedByLastName() {
		return lastNameList;
	}

	@Override
	// O(1) осталось так же
	public Iterable<Person> getAllPersonSortedByAge() {
		return ageList;
	}

	@Override
	// O(1) осталось так же
	public int size() {
		return idList.size();
	}


}
