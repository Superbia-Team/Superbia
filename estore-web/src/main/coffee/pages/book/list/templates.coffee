# modular template loading
define (require) ->
    book: require("text!templates/books/book.htm")
    books: require("text!templates/books/booklist.htm")
    layout: require("text!templates/books/layout.htm")
    bookdetail:require("text!templates/books/bookdetail.htm")
