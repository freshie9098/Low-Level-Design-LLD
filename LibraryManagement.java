import java.util.*;

class Book {
    String bookId;
    String title;
    String author;
    String isbn;
    String category;
}
class BookCopy {
    String copyId;
    String bookId;
    CopyStatus status;
}
enum CopyStatus {
    AVAILABLE,
    ISSUED
}
class User {
    String userId;
    String name;
    List<String> activeLoanIds;
}
class Loan {
    String loanId;

    String userId;
    String copyId;

    LocalDate issueDate;
    LocalDate dueDate;
    LocalDate returnDate;

    LoanStatus status;
}
enum LoanStatus {
    ACTIVE,
    CLOSED
}
// Reservation (Extensible)
class Reservation {
    String reservationId;

    String userId;
    String bookId;

    LocalDate reservationDate;

    ReservationStatus status;
}
enum ReservationStatus {
    ACTIVE,
    FULFILLED,
    CANCELLED
}
class Reservation {
    String reservationId;

    String userId;
    String bookId;

    LocalDate reservationDate;

    ReservationStatus status;
}
enum ReservationStatus {
    ACTIVE,
    FULFILLED,
    CANCELLED
}
6. Catalog

Fast searching.

class Catalog {

    Map<String, List<Book>> titleIndex;

    Map<String, List<Book>> authorIndex;

    Map<String, Book> isbnIndex;
}
//Methods
List<Book> searchByTitle(String title);

List<Book> searchByAuthor(String author);

Book searchByISBN(String isbn);
public class Library {
	List<Book>books;
	List<BookCopy>BookCopy;
	Map<String, List<String>> titleIndex;   // title -> bookIds
	Map<String, List<String>> authorIndex;  // author -> bookIds
	Map<String, String> isbnIndex;          // isbn -> bookId
	Map<String, Book> books;                // bookId -> Book

	public BookCopy getAvailableBookCopy(String title,String author,String category) {
		//getBooks
		for(Book book :Books) {
			if(book.title = title && author && category) {
				return availableCopies[bookId].peek();
			}
		}
		return null;
	}

	public Loan IssueBookCopy(User,bookcopy) {
		bookcopy.Status = ISSUED;
		availableCopies[bookId].poll()
		Loan loan = new loan(user,bookcopy,issueDate,dueDate);
		return loan;
	}
}

7. Library

Stores data structures.

class Library {

    Map<String, Book> books;

    Map<String, BookCopy> copies;

    Map<String, User> users;

    Map<String, Loan> loans;

    Map<String, Queue<BookCopy>> availableCopies;

    Map<String, Queue<Reservation>> reservations;

    Catalog catalog;
}
8. LoanService

Main business logic.

class LoanService {

    Loan borrowBook(
        String userId,
        String bookId
    );

    void returnBook(
        String copyId
    );
}
borrowBook()
Loan borrowBook(
    String userId,
    String bookId
)
Flow
1. Validate user
2. Find available copy
3. Mark copy ISSUED
4. Create Loan
5. Store Loan
6. Return Loan
returnBook()
void returnBook(
    String copyId
)
Flow
1. Find active Loan
2. Update returnDate
3. Calculate fine
4. Close Loan
5. Mark copy AVAILABLE
6. If reservation exists:
      notify next user
   else:
      push copy to availableCopies queue
Important Data Structures
Fast Search
Map<String, List<Book>> titleIndex;

Map<String, List<Book>> authorIndex;

Map<String, Book> isbnIndex;
Fast Available Copy Lookup
Map<String, Queue<BookCopy>>
availableCopies;

Example:

B1 -> [C3, C4, C7]

Borrow:

availableCopies.get(bookId).poll();

Return:

availableCopies.get(bookId).offer(copy);
Reservation Queue
Map<String, Queue<Reservation>>
reservations;

Example:

B1 -> [U5, U8, U10]

FIFO reservation handling.

Interview Class Diagram (Quick Revision)
Book
 |
 | 1
 |
 | *
BookCopy
 |
 |
Loan
 |
 |
User

Book ------ Catalog

BookCopy ---- availableCopies Queue

Reservation ---- reservation Queue










