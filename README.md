composite-dao
=============

Demonstrates the [Composite DAO pattern](http://davejoyce.github.io/projects/2013/04/24/composite-dao-pattern) using the Spring Framework.

What does it do?
----------------

This demo app uses the [Spring Social Twitter](http://static.springframework.org/spring-social-twitter/docs/1.0.x/reference/html) extension and a small application-local RDBMS to show how a composite DAO works in a _local vs. remote_ scenario.

* Performs RESTful interactions via the [Twitter API](https://dev.twitter.com) for remote operations
* Performs CRUD operations on the RDBMS via JPA / [Hibernate](http://www.hibernate.org/)

Continuous integration
----------------------

Continuous integration for this project is provided by **TBD**.

License
-------

This project is licensed under the [Apache Software License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt)
